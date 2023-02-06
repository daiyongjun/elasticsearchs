package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.min;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.AbstractSearchTestCase;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

public class MinAggregationBuilderTests extends AbstractSearchTestCase {

    @Test
    public void builder() throws IOException {
        String query = "{\n" +
                "    \"aggs\" : {\n" +
                "        \"min_price\" : { \"min\" : { \"field\" : \"price\" } }\n" +
                "    }\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        System.out.println("返回结果:\n" + searchSourceBuilder.toString());
    }

    //脚本执行
}
