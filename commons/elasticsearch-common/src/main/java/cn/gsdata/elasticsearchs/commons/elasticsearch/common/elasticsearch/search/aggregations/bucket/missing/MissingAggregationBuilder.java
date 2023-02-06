package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.missing;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;

import java.io.IOException;

/**
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-bucket-missing-aggregation.html
 * {
 * "aggs" : {
 * "products_without_a_price" : {
 * "missing" : { "field" : "price" }
 * }
 * }
 * }
 *
 * @author daiyongjun
 */
public class MissingAggregationBuilder extends ValuesSourceAggregationBuilder<MissingAggregationBuilder> {
    public static final String NAME = "missing";

    /**
     * 构造一个新的聚合构建器。
     *
     * @param name 当前聚合的名称
     */
    protected MissingAggregationBuilder(String name) {
        super(name);
    }

    private static final ObjectParser<Void, MissingAggregationBuilder> PARSER;

    static {
        PARSER = new ObjectParser<>(MissingAggregationBuilder.NAME);
        ValuesSourceAggregationBuilder.declareValuesSourceFields(PARSER, true, true);
    }

    public static MissingAggregationBuilder parse(String aggregationName, XContentParser parser) throws IOException {
        return PARSER.parse(parser, null, new MissingAggregationBuilder(aggregationName));
    }

    @Override
    protected XContentBuilder doXContentBody(XContentBuilder builder) throws IOException {
        return builder;
    }

    @Override
    public String getType() {
        return NAME;
    }
}
