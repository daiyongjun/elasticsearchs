package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import org.junit.Test;

import java.io.IOException;

public class WildcardQueryBuilderTests extends AbstractQueryTestCase<WildcardQueryBuilder>{
    @Test
    public void query() throws IOException {
        String query = "{\n" +
                "        \"wildcard\" : { \"user\" : { \"value\" : \"ki*y\", \"boost\" : 2.0 } }\n" +
                "    }";
        WildcardQueryBuilder builder = parseQuery(query);
        System.out.println("返回结果:\n" + builder.toString());
    }
}
