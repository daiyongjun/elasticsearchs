package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.missing;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.AbstractSearchTestCase;

import java.io.IOException;

public class MissingAggregationBuilderTests extends AbstractSearchTestCase {
    @Test
    public void builder() throws IOException {
        String query = "{\n" +
                "    \"aggs\" : {\n" +
                "        \"products_without_a_price\" : {\n" +
                "            \"missing\" : { \"field\" : \"price\" }\n" +
                "        }\n" +
                "    }\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        System.out.println("返回结果:\n" + searchSourceBuilder.toString());
    }
}
