package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import org.junit.Test;

import java.io.IOException;

public class PrefixQueryBuilderTests extends AbstractQueryTestCase<PrefixQueryBuilder> {
    @Test
    public void query() throws IOException {
        String query = "{\n" +
                "    \"prefix\" : { \"user\" :  { \"value\" : \"ki\", \"boost\" : 2.0 } }\n" +
                "  }";
        PrefixQueryBuilder builder = parseQuery(query);
        System.out.println("返回结果:\n" + builder.toString());
    }
}
