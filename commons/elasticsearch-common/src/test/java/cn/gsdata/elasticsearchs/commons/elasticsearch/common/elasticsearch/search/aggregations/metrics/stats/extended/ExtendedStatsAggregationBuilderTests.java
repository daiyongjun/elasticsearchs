package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.stats.extended;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.AbstractSearchTestCase;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

public class ExtendedStatsAggregationBuilderTests extends AbstractSearchTestCase {

    @Test
    public void builder() throws IOException {
        String query = "{\n" +
                "    \"aggs\" : {\n" +
                "        \"grades_stats\" : { \"extended_stats\" : { \"field\" : \"grade\" } }\n" +
                "    }\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        System.out.println("返回结果:\n" + searchSourceBuilder.toString());
    }
}
