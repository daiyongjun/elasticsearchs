package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import org.junit.Test;

import java.io.IOException;

public class RangeQueryBuilderTests extends AbstractQueryTestCase<RangeQueryBuilder> {
    @Test
    public void query() throws IOException {
        String query = "{\n" +
                "        \"range\" : {\n" +
                "            \"timestamp\" : {\n" +
                "                \"gte\": \"2015-01-01 00:00:00\", \n" +
                "                \"lte\": \"now\" \n" +
                "            }\n" +
                "        }\n" +
                "    }";
        RangeQueryBuilder builder = parseQuery(query);
        System.out.println("返回结果:\n" + builder.toString());
    }
}
