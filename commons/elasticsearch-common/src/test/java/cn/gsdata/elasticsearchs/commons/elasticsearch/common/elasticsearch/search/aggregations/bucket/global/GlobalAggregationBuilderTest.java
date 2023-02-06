package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.global;


import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.AbstractSearchTestCase;

import java.io.IOException;

public class GlobalAggregationBuilderTest extends AbstractSearchTestCase {
    @Test
    public void builder() throws IOException {
        String query = "{\n" +
                "    \"aggs\" : {\n" +
                "        \"all_products\" : {\n" +
                "            \"global\" : {}, \n" +
                "            \"aggs\" : { \n" +
                "                \"avg_price\" : { \"avg\" : { \"field\" : \"price\" } }\n" +
                "            }\n" +
                "        },\n" +
                "        \"t_shirts\": { \"avg\" : { \"field\" : \"price\" } }\n" +
                "    }\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        System.out.println("返回结果:\n" + searchSourceBuilder.toString());
    }
}
