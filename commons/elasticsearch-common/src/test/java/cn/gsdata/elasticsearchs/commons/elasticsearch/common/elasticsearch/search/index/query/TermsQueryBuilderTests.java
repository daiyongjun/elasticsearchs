package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import org.junit.Test;

import java.io.IOException;

public class TermsQueryBuilderTests extends AbstractQueryTestCase<TermsQueryBuilder> {
    @Test
    public void query() throws IOException {
        String query =
                "{\n" +
                        "  \"terms\" : {\n" +
                        "    \"user\" : [ \"kimchy\", \"elasticsearch\" ],\n" +
                        "    \"boost\" : 1.0\n" +
                        "  }\n" +
                        "}";
        TermsQueryBuilder builder = parseQuery(query);
        System.out.println("返回结果:\n" + builder.toString());
    }
}
