package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.nested;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.AbstractSearchTestCase;

import java.io.IOException;

public class NestedAggregationBuilderTests extends AbstractSearchTestCase {
    @Test
    public void builder() throws IOException {
        String query = "{\n" +
                "    \"query\" : {\n" +
                "        \"term\" : { \"name\" : \"led tv\" }\n" +
                "    },\n" +
                "    \"aggs\" : {\n" +
                "        \"resellers\" : {\n" +
                "            \"nested\" : {\n" +
                "                \"path\" : \"resellers\"\n" +
                "            },\n" +
                "            \"aggs\" : {\n" +
                "                \"avg_price\" : { \"avg\" : { \"field\" : \"resellers.price\" } }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        System.out.println("返回结果:\n" + searchSourceBuilder.toString());
    }
}
