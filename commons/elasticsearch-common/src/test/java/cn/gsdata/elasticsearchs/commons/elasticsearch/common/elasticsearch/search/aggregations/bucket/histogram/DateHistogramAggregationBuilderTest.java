package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.histogram;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.AbstractSearchTestCase;

import java.io.IOException;

public class DateHistogramAggregationBuilderTest extends AbstractSearchTestCase {
    @Test
    public void builder() throws IOException {
        String query = "{\n" +
                "  \"aggs\": {\n" +
                "    \"sales\": {\n" +
                "      \"date_histogram\": {\n" +
                "        \"field\": \"time\",\n" +
                "        \"format\": \"yyyy-MM-dd\",\n" +
                "        \"min_doc_count\": 2,\n" +
                "        \"extended_bounds\": {\n" +
                "          \"min\": \"2014-01-01\",\n" +
                "          \"max\": \"2018-12-31\"\n" +
                "        },\n" +
                "        \"time_zone\": \"Asia/Shanghai\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        System.out.println("返回结果:\n" + searchSourceBuilder.toString());
    }
}
