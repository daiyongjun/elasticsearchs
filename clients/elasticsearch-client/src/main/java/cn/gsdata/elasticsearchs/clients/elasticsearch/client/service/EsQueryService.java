package cn.gsdata.elasticsearchs.clients.elasticsearch.client.service;

import java.util.*;
import java.util.Map.Entry;

import cn.gsdata.elasticsearchs.clients.elasticsearch.client.client.ContentRegistry;
import cn.gsdata.elasticsearchs.clients.elasticsearch.client.client.DynamicCustomRestClient;
import cn.gsdata.elasticsearchs.clients.elasticsearch.client.configs.EsSearchClientConfig;
import cn.gsdata.elasticsearchs.commons.base.common.exception.CustomException;
import cn.gsdata.elasticsearchs.commons.base.common.model.es.*;
import cn.gsdata.elasticsearchs.commons.base.common.utils.DateUtil;
import cn.gsdata.elasticsearchs.commons.clients.common.annotation.Source;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.SearchSourceBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.json.JsonXContent;
import com.netflix.ribbon.proxy.annotation.Http;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service服务类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/10/8 14:11
 */
@Service
@Slf4j
public class EsQueryService {
    private final DynamicCustomRestClient client;
    private final EsSearchClientConfig clientConfig;
    private final ContentRegistry registry;

    @Value("${spring.custom.elasticsearch.operator.filters.names}")
    private String filter = "";

    private static Map<String, String> versions;

    {
        versions = new HashMap<>();
        versions.put("twentytwo", "7.x");
    }

    public EsQueryService(@Qualifier("client") DynamicCustomRestClient client, EsSearchClientConfig clientConfig, @Qualifier("registry") ContentRegistry registry) {
        this.client = client;
        this.clientConfig = clientConfig;
        this.registry = registry;
    }


    @Source
    public String insert(InsertShardingCondition condition, @SuppressWarnings("unused") String source) throws Exception {
        List<String> filters = Arrays.asList(filter.split(","));
        if (filters.contains(source)) {
            String report = this.parseIndex(condition.getStartStamp(), condition.getEndStamp(), condition.getIndex(), source);
            return this.insert(report, condition.getStatement(), versions.get(source));
        }
        throw new CustomException("输入的source不支持除查询以外操作", 400);
    }


    @Source
    public String update(PutShardingCondition condition, @SuppressWarnings("unused") String source) throws Exception {
        List<String> filters = Arrays.asList(filter.split(","));
        if (filters.contains(source)) {
            String report = this.parseIndex(condition.getStartStamp(), condition.getEndStamp(), condition.getIndex(), source);
            return this.put(report, condition.getDocId(), condition.getStatement(), versions.get(source));
        }
        throw new CustomException("输入的source不支持除查询以外操作", 400);
    }

    @Source
    public String query(Condition condition, @SuppressWarnings("unused") String source) throws Exception {
        XContentParser parser = JsonXContent.JSON_X_CONTENT.createParser(registry.getContentRegistry(), condition.getStatement());
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.fromXContent(parser, "news_posttime");
        String query = searchSourceBuilder.toString();
        log.info("query" + query + "\t" + searchSourceBuilder.startStamp() + "\t" + searchSourceBuilder.endStamp());
        String repo = this.parseIndex(searchSourceBuilder.startStamp(), searchSourceBuilder.endStamp(), condition.getIndex(), source);
        if (condition.getFlag() == null) {
            return this.get(repo, query);
        }
        return this.agg(repo, query);
    }


    @Source
    public String analyzeQuery(EsAnalyzeCondition condition, @SuppressWarnings("unused") String source) throws Exception {
        return this.analyze(condition.getIndex(), condition.getStatement());
    }

    private String parseIndex(Long startStamp, Long endStamp, String index, String source) throws Exception {
        String regex = ".*(_[0-9]{6})$";
        String format = "yyyyMM";
        if (EsSearchClientConfig.indexes.containsKey(source)) {
            Map<String, String> sources = EsSearchClientConfig.indexes.get(source);
            String key = "format";
            if (sources.containsKey(key)) {
                format = sources.get(key);
            }
        }
        if (index.matches(regex)) {
            return index;
        } else {
            String startTime = DateUtil.dateToString(new Date(startStamp));
            String endTime = DateUtil.dateToString(new Date(endStamp));
            List<String> dates = DateUtil.getDates(startTime, endTime, "yyyy-MM-dd", format);
            StringBuilder reports = new StringBuilder();
            String[] var9 = index.split(",");
            String current = DateUtil.getCurrentDate(format);
            for (String date : dates) {
                if (DateUtil.compareDate(date, current, format)) {
                    break;
                }
                for (String report : var9) {
                    reports.append(String.format(report + "_%s,", date));
                }
            }
            String report = reports.toString();
            report = report.replaceAll(",$", "");
            return report;
        }
    }

    private String analyze(String index, String query) throws Exception {
        // 拼接URL
        String url = "/" + index + "/_analyze";
        return this.search(query, url);
    }

    private String get(String index, String query) throws Exception {
        String url = "/" + index + "/_search?ignore_unavailable=true";
        return this.search(query, url);
    }

    private String put(String index, String id, String query, String version) throws Exception {
        String versions = "7.x";
        String url;
        if (versions.equals(version)) {
            url = String.format("/%s/_update/%s?pretty=true", index, id);
        } else {
            url = String.format("/%s/documents/%s/_update", index, id);
        }
        return this.operate(query, url);
    }

    private String insert(String index, String statement, String version) throws Exception {
        String versions = "7.x";
        String url;
        if (versions.equals(version)) {
            url = String.format("/%s/_bulk", index);
        } else {
            url = String.format("/%s/documents/_bulk", index);
        }
        return this.operate(statement, url);
    }

    private String agg(String index, String query) throws Exception {
        String url = "/" + index + "/_search?ignore_unavailable=true&" + "typed_keys=true";
        return EntityUtils.toString(execute(query, url, Http.HttpMethod.GET.name()).getEntity());
    }


    private String search(String query, String url) throws Exception {
        return EntityUtils.toString(execute(query, url, Http.HttpMethod.GET.name()).getEntity());
    }

    private String operate(String query, String url) throws Exception {
        return EntityUtils.toString(execute(query, url, Http.HttpMethod.POST.name()).getEntity());
    }

    private Response execute(String query, String url, String method) throws Exception {
        Map<String, String> paramMap = new HashMap<>(10);
        paramMap.put("pretty", "true");
        HttpEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);
        Request request = new Request(method, url);
        for (Entry<String, String> stringEntry : paramMap.entrySet()) {
            request.addParameter((String) ((Entry) stringEntry).getKey(), (String) ((Entry) stringEntry).getValue());
        }
        request.setEntity(entity);
        RestClient restClient = this.client.targetContainer().getRestClient();
        return restClient.performRequest(request);
    }
}
