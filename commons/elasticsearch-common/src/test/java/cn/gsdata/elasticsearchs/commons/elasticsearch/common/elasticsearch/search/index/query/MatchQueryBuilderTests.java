package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import org.junit.Test;

import java.io.IOException;

public class MatchQueryBuilderTests extends AbstractQueryTestCase<MatchQueryBuilder> {

    @Test
    public void query() throws IOException {
        String query = "{\n" +
                "        \"match\" : {\n" +
                "            \"message\" : \"this is a test\"\n" +
                "        }\n" +
                "    }";
        MatchQueryBuilder builder = parseQuery(query);
        System.out.println("返回结果:\n" + builder.toString());
    }
}
