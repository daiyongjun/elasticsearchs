package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import org.junit.Test;

import java.io.IOException;

public class TypeQueryBuilderTests extends AbstractQueryTestCase<TypeQueryBuilder>{
    @Test
    public void query() throws IOException {
        String query =  "{\n" +
                "        \"type\" : {\n" +
                "            \"value\" : \"my_type\"\n" +
                "        }\n" +
                "    }";
        TypeQueryBuilder builder = parseQuery(query);
        System.out.println("θΏεη»ζ:\n" + builder.toString());
    }
}
