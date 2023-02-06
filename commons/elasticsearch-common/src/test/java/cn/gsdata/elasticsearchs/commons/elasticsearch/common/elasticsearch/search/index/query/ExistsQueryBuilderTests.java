package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import org.junit.Test;

import java.io.IOException;

public class ExistsQueryBuilderTests  extends AbstractQueryTestCase<ExistsQueryBuilder> {
    @Test
    public void query() throws IOException {
        String query = "{\n" +
                "        \"exists\" : { \"field\" : \"user\" }\n" +
                "    }";
        ExistsQueryBuilder builder = parseQuery(query);
        System.out.println("返回结果:\n" + builder.toString());
    }
}
