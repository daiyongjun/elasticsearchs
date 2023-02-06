package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.cardinality;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.AbstractSearchTestCase;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

public class CardinalityAggregationBuilderTests extends AbstractSearchTestCase {

    @Test
    public void cardinality() throws IOException {
        String query = "{\n" +
                "    \"aggs\" : {\n" +
                "        \"type_count\" : {\n" +
                "            \"cardinality\" : {\n" +
                "                \"field\" : \"type\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        System.out.println("返回结果:\n" + searchSourceBuilder.toString());
    }


    @Test
    public void script() throws IOException {
        String query = "{\n" +
                "    \"aggs\" : {\n" +
                "        \"type_promoted_count\" : {\n" +
                "            \"cardinality\" : {\n" +
                "                \"script\": {\n" +
                "                    \"lang\": \"painless\",\n" +
                "                    \"source\": \"doc['type'].value + ' ' + doc['promoted'].value\"\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        System.out.println("返回结果:\n" + searchSourceBuilder.toString());
    }
}
