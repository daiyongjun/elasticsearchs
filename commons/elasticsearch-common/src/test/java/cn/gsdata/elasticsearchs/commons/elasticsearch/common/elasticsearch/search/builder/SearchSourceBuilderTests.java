package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder;

import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class SearchSourceBuilderTests extends AbstractSearchTestCase {
    /**
     * 测试多层嵌套的情况获取stamp数据
     * 在DSL语句中我们可以逻辑查询的数量分为单逻辑(QueryBuilder),和多逻辑(BoolQueryBuilder).
     */
    @Test
    public void searchStamp() throws Exception {
        //单逻辑(QueryBuilder)
        String search = "{\n" +
                "  \"query\": {\n" +
                "    \"range\": {\n" +
                "      \"news_posttime\": {\n" +
                "        \"from\": \"2022-07-20 00:00:00\",\n" +
                "        \"to\": \"2022-07-21 00:00:00\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(search, "news_posttime");
        assertEquals(1658246400000L, searchSourceBuilder.startStamp());
        assertEquals(1658332800000L, searchSourceBuilder.endStamp());

        //多逻辑(BoolQueryBuilder)  仅有逻辑与 (must/filter),
        search = "{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"must\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"news_posttime\": {\n" +
                "              \"from\": \"2022-07-20 00:00:00\",\n" +
                "              \"to\": \"2022-07-21 00:00:00\"\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"news_posttime\": {\n" +
                "              \"from\": \"2022-07-20 00:00:00\",\n" +
                "              \"to\": \"2022-07-23 00:00:00\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"must_not\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"news_posttime\": {\n" +
                "              \"from\": \"2022-07-23 00:00:00\",\n" +
                "              \"to\": \"2022-07-24 00:00:00\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        searchSourceBuilder = parseSearch(search, "news_posttime");
        assertEquals(1658246400000L, searchSourceBuilder.startStamp());
        assertEquals(1658332800000L, searchSourceBuilder.endStamp());

        //多逻辑(BoolQueryBuilder)  仅有逻辑或 (should/must_not)
        search = "{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"should\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"news_posttime\": {\n" +
                "              \"from\": \"2022-07-19 00:00:00\",\n" +
                "              \"to\": \"2022-07-21 00:00:00\"\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"news_posttime\": {\n" +
                "              \"from\": \"2022-07-20 00:00:00\",\n" +
                "              \"to\": \"2022-07-23 00:00:00\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"must_not\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"news_posttime\": {\n" +
                "              \"from\": \"2022-07-23 00:00:00\",\n" +
                "              \"to\": \"2022-07-24 00:00:00\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}\n";
        searchSourceBuilder = parseSearch(search, "news_posttime");
        assertEquals(1658160000000L, searchSourceBuilder.startStamp());
        assertEquals(1658592000000L, searchSourceBuilder.endStamp());

        //多逻辑(BoolQueryBuilder)  仅有逻辑或 (should/must_not)
        search = "{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"should\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"news_posttime\": {\n" +
                "              \"from\": \"2022-07-19 00:00:00\",\n" +
                "              \"to\": \"2022-07-21 00:00:00\"\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"news_posttime\": {\n" +
                "              \"from\": \"2022-07-20 00:00:00\",\n" +
                "              \"to\": \"2022-07-23 00:00:00\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"must_not\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"news_posttime\": {\n" +
                "              \"from\": \"2022-07-23 00:00:00\",\n" +
                "              \"to\": \"2022-07-24 00:00:00\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}\n";
        searchSourceBuilder = parseSearch(search, "news_posttime");
        assertEquals(1658160000000L, searchSourceBuilder.startStamp());
        assertEquals(1658592000000L, searchSourceBuilder.endStamp());

        //多逻辑(BoolQueryBuilder)  逻辑与(minimum_should_match=2)
        search = "{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"should\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"news_posttime\": {\n" +
                "              \"from\": \"2022-07-19 00:00:00\",\n" +
                "              \"to\": \"2022-07-21 00:00:00\"\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"news_posttime\": {\n" +
                "              \"from\": \"2022-07-20 00:00:00\",\n" +
                "              \"to\": \"2022-07-23 00:00:00\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"minimum_should_match\": 2\n" +
                "    }\n" +
                "  }\n" +
                "}";
        searchSourceBuilder = parseSearch(search, "news_posttime");
        assertEquals(1658246400000L, searchSourceBuilder.startStamp());
        assertEquals(1658332800000L, searchSourceBuilder.endStamp());

        //多逻辑(BoolQueryBuilder)  逻辑与和逻辑或(must/must_not)
        search = "{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"must\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"news_posttime\": {\n" +
                "              \"from\": \"2022-07-20 00:00:00\",\n" +
                "              \"to\": \"2022-07-21 00:00:00\"\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"news_posttime\": {\n" +
                "              \"from\": \"2022-07-20 00:00:00\",\n" +
                "              \"to\": \"2022-07-23 00:00:00\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"must_not\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"news_posttime\": {\n" +
                "              \"from\": \"2022-07-20 00:00:00\",\n" +
                "              \"to\": \"2022-07-24 00:00:00\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        searchSourceBuilder = parseSearch(search, "news_posttime");
        assertEquals(1658246400000L, searchSourceBuilder.startStamp());
        assertEquals(1658332800000L, searchSourceBuilder.endStamp());
    }

    /**
     * Search  基于from，size字段测试
     */
    @Test
    public void fromSizeSearch() throws Exception {
        String search = "{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"should\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"news_posttime\": {\n" +
                "              \"from\": \"2022-07-20 00:00:00\",\n" +
                "              \"to\": \"now\"\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"news_posttime\": {\n" +
                "              \"from\": \"2022-07-22 00:00:00\",\n" +
                "              \"to\": \"now\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"minimum_should_match\": 1\n" +
                "    }\n" +
                "  }\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(search, "news_posttime");

    }

    @Test
    public void sortModeSearch() throws Exception {
        String search = "{\n" +
                "   \"query\" : {\n" +
                "      \"term\" : { \"product\" : \"chocolate\" }\n" +
                "   },\n" +
                "   \"sort\" : [\n" +
                "      {\"price\" : {\"order\" : \"asc\", \"mode\" : \"avg\"}}\n" +
                "   ]\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(search, "news_posttime");
        assertEquals("{\"query\":{\"term\":{\"product\":{\"value\":\"chocolate\",\"boost\":1.0}}},\"sort\":[{\"price\":{\"order\":\"asc\",\"mode\":\"AVG\"}}]}", searchSourceBuilder.toString());
    }

    @Test
    public void nestedSortSearch() throws Exception {
        String search = "{\n" +
                "   \"query\" : {\n" +
                "      \"term\" : { \"product\" : \"chocolate\" }\n" +
                "   },\n" +
                "   \"sort\" : [\n" +
                "       {\n" +
                "          \"offer.price\" : {\n" +
                "             \"mode\" :  \"avg\",\n" +
                "             \"order\" : \"asc\",\n" +
                "             \"nested\": {\n" +
                "                \"path\": \"offer\",\n" +
                "                \"filter\": {\n" +
                "                   \"term\" : { \"offer.color\" : \"blue\" }\n" +
                "                }\n" +
                "             }\n" +
                "          }\n" +
                "       }\n" +
                "    ]\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = parseSearch(search, "news_posttime");
    }

    @Test
    public void sortSearch() throws Exception {

    }


    ///**
    // * termsSetQuery
    // *
    // * @throws IOException 解析异常
    // */
    //@Test
    //public void termsSetQuery() throws IOException {
    //    String query = "{\n" +
    //            "    \"query\": {\n" +
    //            "        \"terms_set\": {\n" +
    //            "            \"codes\" : {\n" +
    //            "                \"terms\" : [\"abc\", \"def\", \"ghi\"],\n" +
    //            "                \"minimum_should_match_script\": {\n" +
    //            "                   \"source\": \"Math.min(params.num_terms, doc['required_matches'].value)\"\n" +
    //            "                }\n" +
    //            "            }\n" +
    //            "        }\n" +
    //            "    }\n" +
    //            "}";
    //    Search(query);
    //}
    //
    ///**
    // * rangeQuery
    // *
    // * @throws IOException 解析异常
    // */
    //@Test
    //public void rangeQuery() throws IOException {
    //    String query = "{\n" +
    //            "    \"query\": {\n" +
    //            "        \"range\" : {\n" +
    //            "            \"timestamp\" : {\n" +
    //            "                \"gte\": \"2015-01-01 00:00:00\", \n" +
    //            "                \"lte\": \"now\", \n" +
    //            "                \"time_zone\": \"+01:00\"\n" +
    //            "            }\n" +
    //            "        }\n" +
    //            "    }\n" +
    //            "}";
    //    Search(query);
    //}
    //
    //
    ///**
    // * existsQuery
    // *
    // * @throws IOException 解析异常
    // */
    //@Test
    //public void existsQuery() throws IOException {
    //    String query = "{\n" +
    //            "    \"query\": {\n" +
    //            "        \"exists\" : { \"field\" : \"user\" }\n" +
    //            "    }\n" +
    //            "}";
    //    Search(query);
    //}
    //
    ///**
    // * existsQuery
    // *
    // * @throws IOException 解析异常
    // */
    //@Test
    //public void prefixQuery() throws IOException {
    //    String query = "{ \"query\": {\n" +
    //            "    \"prefix\" : { \"user\" :  { \"value\" : \"ki\", \"boost\" : 2.0 } }\n" +
    //            "  }\n" +
    //            "}";
    //    Search(query);
    //}
    //
    ///**
    // * wildcardQuery
    // *
    // * @throws IOException 解析异常
    // */
    //@Test
    //public void wildcardQuery() throws IOException {
    //    String query = "{\n" +
    //            "    \"query\": {\n" +
    //            "        \"wildcard\" : { \"user\" : { \"wildcard\" : \"ki*y\", \"boost\" : 2.0 } }\n" +
    //            "    }\n" +
    //            "}";
    //    Search(query);
    //}
    //
    //
    ///**
    // * regexpQuery
    // *
    // * @throws IOException 解析异常
    // */
    //@Test
    //public void regexpQuery() throws IOException {
    //    String query = "{\n" +
    //            "    \"query\": {\n" +
    //            "        \"regexp\":{\n" +
    //            "            \"name.first\": {\n" +
    //            "                \"value\": \"s.*y\",\n" +
    //            "                \"flags\" : \"INTERSECTION|COMPLEMENT|EMPTY\",\n" +
    //            "                \"max_determinized_states\": 20000\n" +
    //            "            }\n" +
    //            "        }\n" +
    //            "    }\n" +
    //            "}";
    //    Search(query);
    //}
    //
    ///**
    // * fuzzyQuery
    // *
    // * @throws IOException 解析异常
    // */
    //@Test
    //public void fuzzyQuery() throws IOException {
    //    String query = "{\n" +
    //            "    \"query\": {\n" +
    //            "        \"fuzzy\" : {\n" +
    //            "            \"user\" : {\n" +
    //            "                \"value\": \"ki\",\n" +
    //            "                \"boost\": 1.0,\n" +
    //            "                \"fuzziness\": 2,\n" +
    //            "                \"prefix_length\": 0,\n" +
    //            "                \"max_expansions\": 100\n" +
    //            "            }\n" +
    //            "        }\n" +
    //            "    }\n" +
    //            "}";
    //    Search(query);
    //}
    //
    ///**
    // * typeQuery
    // *
    // * @throws IOException 解析异常
    // */
    //@Test
    //public void typeQuery() throws IOException {
    //    String query = "{\n" +
    //            "    \"query\": {\n" +
    //            "        \"type\" : {\n" +
    //            "            \"value\" : \"my_type\"\n" +
    //            "        }\n" +
    //            "    }\n" +
    //            "}";
    //    Search(query);
    //}
    //
    //@Test
    //public void idsQuery() throws IOException {
    //    String query = "{\n" +
    //            "    \"query\": {\n" +
    //            "        \"ids\" : {\n" +
    //            "            \"type\" : \"my_type\",\n" +
    //            "            \"values\" : [\"1\", \"4\", \"100\"]\n" +
    //            "        }\n" +
    //            "    }\n" +
    //            "}";
    //    Search(query);
    //}
    //
    //@Test
    //public void termsAggregation() throws IOException {
    //    String query = "{\n" +
    //            "  \"query\": {\n" +
    //            "    \"terms\": {\n" +
    //            "      \"user\": [\n" +
    //            "        \"kimchy\",\n" +
    //            "        \"elasticsearch\"\n" +
    //            "      ],\n" +
    //            "      \"boost\": 1\n" +
    //            "    }\n" +
    //            "  },\n" +
    //            "  \"aggs\": {\n" +
    //            "    \"高级桶\": {\n" +
    //            "      \"terms\": {\n" +
    //            "        \"field\": \"bucket\",\n" +
    //            "        \"size\": 10\n" +
    //            "      },\n" +
    //            "      \"aggs\": {\n" +
    //            "        \"次级桶\": {\n" +
    //            "          \"terms\": {\n" +
    //            "            \"field\": \"bucket\",\n" +
    //            "            \"size\": 10\n" +
    //            "          }\n" +
    //            "        }\n" +
    //            "      }\n" +
    //            "    }\n" +
    //            "  }\n" +
    //            "}";
    //    Search(query);
    //}
}
