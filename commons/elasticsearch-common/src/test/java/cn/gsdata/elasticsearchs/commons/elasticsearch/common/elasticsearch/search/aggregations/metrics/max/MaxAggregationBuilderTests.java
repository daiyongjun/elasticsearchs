package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.max;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.AbstractSearchTestCase;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MaxAggregationBuilderTests extends AbstractSearchTestCase {

    @Test
    public void max() throws IOException {
        String query = "{\n" +
                "    \"aggs\" : {\n" +
                "        \"max_price\" : { \"max\" : { \"field\" : \"price\" } }\n" +
                "    }\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        System.out.println("返回结果:\n" + searchSourceBuilder.toString());
    }

    @Test
    public void script() throws IOException {
        String query = "{\n" +
                "    \"aggs\" : {\n" +
                "        \"max_price_in_euros\" : {\n" +
                "            \"max\" : {\n" +
                "                \"field\" : \"price\",\n" +
                "                \"script\" : {\n" +
                "                    \"source\" : \"_value * params.conversion_rate\",\n" +
                "                    \"params\" : {\n" +
                "                        \"conversion_rate\" : 1.2\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        //System.out.println("返回结果:\n" + searchSourceBuilder.toString());
    }
}
