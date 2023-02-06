package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import org.junit.Test;

import java.io.IOException;

public class BoolQueryBuilderTests extends AbstractQueryTestCase<BoolQueryBuilder> {

    @Test
    public void query() throws IOException {
        String query =
                "{" +
                        "\"bool\" : {" +
                        "  \"must\" : [ {" +
                        "    \"term\" : {" +
                        "      \"user\" : {" +
                        "        \"value\" : \"kimchy\"," +
                        "        \"boost\" : 1.0" +
                        "      }" +
                        "    }" +
                        "  } ]," +
                        "  \"filter\" : [ {" +
                        "    \"term\" : {" +
                        "      \"tag\" : {" +
                        "        \"value\" : \"tech\"," +
                        "        \"boost\" : 1.0" +
                        "      }" +
                        "    }" +
                        "  } ]," +
//                        "  \"must_not\" : [ {" +
//                        "    \"range\" : {" +
//                        "      \"age\" : {" +
//                        "        \"from\" : 10," +
//                        "        \"to\" : 20," +
//                        "        \"include_lower\" : true," +
//                        "        \"include_upper\" : true," +
//                        "        \"boost\" : 1.0" +
//                        "      }" +
//                        "    }" +
//                        "  } ]," +
                        "  \"should\" : [ {" +
                        "    \"term\" : {" +
                        "      \"tag\" : {" +
                        "        \"value\" : \"wow\"," +
                        "        \"boost\" : 1.0" +
                        "      }" +
                        "    }" +
                        "  }, {" +
                        "    \"term\" : {" +
                        "      \"tag\" : {" +
                        "        \"value\" : \"elasticsearch\"," +
                        "        \"boost\" : 1.0" +
                        "      }" +
                        "    }" +
                        "  } ]," +
                        "  \"minimum_should_match\" : \"23\"," +
                        "  \"boost\" : 42.0" +
                        "}" +
                        "}";
        BoolQueryBuilder builder = parseQuery(query);
        System.out.println("返回结果:\n" + builder.toString());
    }
}
