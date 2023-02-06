package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.sum;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.AbstractSearchTestCase;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

public class SumAggregationBuilderTests extends AbstractSearchTestCase {

    @Test
    public void builder() throws IOException {
        String query = "{\n" +
                "  \"aggs\" : {\n" +
                "        \"hat_prices\" : { \"sum\" : { \"field\" : \"price\" } }\n" +
                "    }\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        System.out.println("返回结果:\n" + searchSourceBuilder.toString());
    }
}
