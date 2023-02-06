package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import org.junit.Test;

import java.io.IOException;

public class TermQueryBuilderTests extends AbstractQueryTestCase<TermQueryBuilder> {


    @Test
    public void query() throws IOException {
        String query =
                "{\n" +
                        "  \"term\" : {\n" +
                        "    \"exact_value\" : {\n" +
                        "      \"value\" : \"Quick Foxes!\",\n" +
                        "      \"boost\" : 1.0\n" +
                        "    }\n" +
                        "  }\n" +
                        "}";
        TermQueryBuilder builder = parseQuery(query);
        System.out.println("返回结果:\n" + builder.toString());
    }

//
//    @Test
//    public void TermArray() throws IOException {
//        String query = "{\n" +
//                "    \"term\": {\n" +
//                "        \"age\": [34, 35]\n" +
//                "    }\n" +
//                "}";
//        try {
//            TermQueryBuilder parsed = (TermQueryBuilder) parseQuery(query);
//            System.out.println("返回结果:\n" + parsed.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(e.getMessage());
//        }
//
//    }
}
