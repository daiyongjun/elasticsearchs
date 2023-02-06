package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.composite;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.AbstractSearchTestCase;

import java.io.IOException;

public class CompositeAggregationBuilderTests extends AbstractSearchTestCase {
    @Test
    public void builder() throws IOException {
        String query = "{\n" +
                "    \"aggs\" : {\n" +
                "        \"my_buckets\": {\n" +
                "            \"composite\" : {\n" +
                "                 \"sources\" : [\n" +
                "                    { \"date\": { \"date_histogram\": { \"field\": \"timestamp\", \"interval\": \"1d\", \"order\": \"desc\" } } },\n" +
                "                    { \"product\": { \"terms\": {\"field\": \"product\" } } }\n" +
                "                ]\n" +
                "            },\n" +
                "            \"aggregations\": {\n" +
                "                \"the_avg\": {\n" +
                "                    \"avg\": { \"field\": \"price\" }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}\n";
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        System.out.println("返回结果:\n" + searchSourceBuilder.toString());
    }
}
