package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.filter;


import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.AbstractSearchTestCase;

import java.io.IOException;

public class FiltersAggregationBuilderTest extends AbstractSearchTestCase {
    @Test
    public void builder() throws IOException {
        String query = "{\n" +
                "  \"aggs\" : {\n" +
                "    \"messages\" : {\n" +
                "      \"filters\" : {\n" +
                "        \"other_bucket_key\": \"other_messages\",\n" +
                "        \"filters\" : {\n" +
                "          \"errors\" :   { \"term\" : { \"body\" : \"error\"   }},\n" +
                "          \"warnings\" : { \"term\" : { \"body\" : \"warning\" }}\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        System.out.println("返回结果:\n" + searchSourceBuilder.toString());
    }
}
