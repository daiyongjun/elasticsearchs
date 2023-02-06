package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import org.junit.Test;

import java.io.IOException;

public class RegexpQueryBuilderTests extends AbstractQueryTestCase<RegexpQueryBuilder> {
    @Test
    public void query() throws IOException {
        String query = "{\n" +
                "        \"regexp\":{\n" +
                "            \"name.first\": {\n" +
                "                \"value\": \"s.*y\",\n" +
                "                \"flags\" : \"INTERSECTION|COMPLEMENT|EMPTY\",\n" +
                "                \"max_determinized_states\": 20000\n" +
                "            }\n" +
                "        }\n" +
                "    }";
        RegexpQueryBuilder builder = parseQuery(query);
        System.out.println("返回结果:\n" + builder.toString());
    }
}

