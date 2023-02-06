package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import org.junit.Test;

import java.io.IOException;

public class TermsSetQueryBuilderTests extends AbstractQueryTestCase<TermsSetQueryBuilder> {
    @Test
    public void query() throws IOException {
        String query = " {\n" +
                "        \"terms_set\": {\n" +
                "            \"codes\" : {\n" +
                "                \"terms\" : [\"abc\", \"def\", \"ghi\"],\n" +
                "                \"minimum_should_match_script\": {\n" +
                "                   \"source\": \"Math.min(params.num_terms, doc['required_matches'].value)\"\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }";
        TermsSetQueryBuilder builder = parseQuery(query);
        System.out.println("返回结果:\n" + builder.toString());
    }
}