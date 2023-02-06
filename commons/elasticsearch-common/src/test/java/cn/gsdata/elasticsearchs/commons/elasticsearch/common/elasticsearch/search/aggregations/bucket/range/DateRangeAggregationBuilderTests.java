package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.range;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.AbstractSearchTestCase;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

public class DateRangeAggregationBuilderTests extends AbstractSearchTestCase {
    @Test
    public void builder() throws IOException {
        String query = "{\n" +
                "  \"aggs\": {\n" +
                "    \"range\": {\n" +
                "      \"date_range\": {\n" +
                "        \"field\": \"date\",\n" +
                "        \"format\": \"MM-yyy\",\n" +
                "        \"ranges\": [\n" +
                "          { \"from\": \"01-2015\", \"to\": \"03-2015\", \"key\": \"quarter_01\" },\n" +
                "          { \"from\": \"03-2015\", \"to\": \"06-2015\", \"key\": \"quarter_02\" }\n" +
                "        ],\n" +
                "        \"keyed\": true\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(query,"");
        System.out.println("返回结果:\n" + searchSourceBuilder.toString());
    }
}
