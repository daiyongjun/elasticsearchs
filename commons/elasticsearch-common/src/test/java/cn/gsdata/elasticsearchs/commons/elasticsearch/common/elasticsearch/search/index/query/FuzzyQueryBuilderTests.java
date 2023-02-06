package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import org.junit.Test;

import java.io.IOException;

public class FuzzyQueryBuilderTests extends AbstractQueryTestCase<FuzzyQueryBuilder> {
    @Test
    public void query() throws IOException {
        String query = "{\n" +
                "       \"fuzzy\" : { \"user\" : \"ki\" }\n" +
                "    }";
        FuzzyQueryBuilder builder = parseQuery(query);
        System.out.println("返回结果:\n" + builder.toString());
    }
}
