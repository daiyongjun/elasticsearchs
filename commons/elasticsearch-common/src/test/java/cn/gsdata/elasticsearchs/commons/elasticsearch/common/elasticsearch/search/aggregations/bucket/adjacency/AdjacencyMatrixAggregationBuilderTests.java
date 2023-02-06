package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.adjacency;


import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.AbstractSearchTestCase;

import java.io.IOException;

public class AdjacencyMatrixAggregationBuilderTests extends AbstractSearchTestCase {
    @Test
    public void builder() throws IOException {
        String query = "{\n" +
                "  \"aggs\" : {\n" +
                "    \"interactions\" : {\n" +
                "      \"adjacency_matrix\" : {\n" +
                "        \"filters\" : {\n" +
                "          \"grpA\" : { \"terms\" : { \"accounts\" : [\"hillary\", \"sidney\"] }},\n" +
                "          \"grpB\" : { \"terms\" : { \"accounts\" : [\"donald\", \"mitt\"] }},\n" +
                "          \"grpC\" : { \"terms\" : { \"accounts\" : [\"vladimir\", \"nigel\"] }}\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        System.out.println("返回结果:\n" + searchSourceBuilder.toString());
    }
}
