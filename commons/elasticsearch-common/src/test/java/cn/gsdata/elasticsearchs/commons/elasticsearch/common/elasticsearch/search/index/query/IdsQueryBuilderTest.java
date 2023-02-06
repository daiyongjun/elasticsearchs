package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import org.junit.Test;

import java.io.IOException;

public class IdsQueryBuilderTest extends AbstractQueryTestCase<IdsQueryBuilder> {
    @Test
    public void query() throws IOException {
        String query = "{\n" +
                "        \"ids\" : {\n" +
                "            \"type\" : \"my_type\",\n" +
                "            \"values\" : [\"1\", \"4\", \"100\"]\n" +
                "        }\n" +
                "    }";
        IdsQueryBuilder builder = parseQuery(query);
        System.out.println("返回结果:\n" + builder.toString());
    }
}
