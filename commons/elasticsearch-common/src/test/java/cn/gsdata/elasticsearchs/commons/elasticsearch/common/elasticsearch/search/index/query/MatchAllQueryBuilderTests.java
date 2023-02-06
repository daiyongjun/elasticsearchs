package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import org.junit.Test;

import java.io.IOException;

public class MatchAllQueryBuilderTests extends AbstractQueryTestCase<MatchAllQueryBuilder> {


    @Test
    public void query() throws IOException {
        String query = "{\n" +
                "        \"match_all\": { \"boost\" : 1.2 }\n" +
                "}";

        MatchAllQueryBuilder builder = parseQuery(query);
        System.out.println("返回结果:\n" + builder.toString());
    }
}
