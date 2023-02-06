package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.terms;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.AbstractSearchTestCase;

import java.io.IOException;

public class TermsAggregationBuilderTests extends AbstractSearchTestCase {

    public void builder(String query) throws IOException {
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        System.out.println("返回结果:\n" + searchSourceBuilder.toString());
    }

    @Test
    public void terms() throws IOException {
        String agg = "{\n" +
                "  \"query\": {\n" +
                "    \"terms\": {\n" +
                "      \"user\": [\n" +
                "        \"kimchy\",\n" +
                "        \"elasticsearch\"\n" +
                "      ],\n" +
                "      \"boost\": 1\n" +
                "    }\n" +
                "  },\n" +
                "  \"aggs\": {\n" +
                "    \"高级桶\": {\n" +
                "      \"terms\": {\n" +
                "        \"field\": \"bucket\",\n" +
                "        \"size\": 10\n" +
                "      },\n" +
                "      \"aggs\": {\n" +
                "        \"次级桶\": {\n" +
                "          \"terms\": {\n" +
                "            \"field\": \"bucket\",\n" +
                "            \"size\": 10\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        builder(agg);
    }
}
