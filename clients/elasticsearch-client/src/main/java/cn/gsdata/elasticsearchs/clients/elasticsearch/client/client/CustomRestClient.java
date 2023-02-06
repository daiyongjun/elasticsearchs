package cn.gsdata.elasticsearchs.clients.elasticsearch.client.client;

import lombok.Data;
import org.elasticsearch.client.RestClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RestClient定义附属定义索引信息
 *
 * @author daiyongjun
 */
@Data
public class CustomRestClient {
    /**
     * 客户端详细信息
     */
    private RestClient restClient;
    /**
     * 定义index相关资源
     */
    private Map<String, Map<String, String>> indexes;
}
