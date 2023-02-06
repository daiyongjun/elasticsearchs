package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.cardinality;


import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AggregationBuilder;

import java.io.IOException;


/**
 * Cardinality的指标，字段的基数值，统计某个字段的高基数情况，如性别的基数为2，男或者女
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-metrics-cardinality-aggregation.html
 *
 * @author daiyongjun
 */
public class CardinalityAggregationBuilder extends MetricsAggregationBuilder<CardinalityAggregationBuilder> {
    public static final String NAME = "cardinality";
    public static final ParseField PRECISION_THRESHOLD_FIELD = new ParseField("precision_threshold");
    private static final ParseField REHASH = new ParseField("rehash").withAllDeprecated("no replacement - values will always be rehashed");

    private Long precisionThreshold = null;

    private static final ObjectParser<Void, CardinalityAggregationBuilder> PARSER;

    static {
        PARSER = new ObjectParser<>(CardinalityAggregationBuilder.NAME);
        declareValuesSourceFields(PARSER, true, false);
        PARSER.declareLong(CardinalityAggregationBuilder::precisionThreshold, PRECISION_THRESHOLD_FIELD);
        PARSER.declareLong((b, v) -> {/*ignore*/}, REHASH);
    }

    /**
     * 构造一个新的聚合构建器。
     *
     * @param name 当前聚合的名称
     */
    protected CardinalityAggregationBuilder(String name) {
        super(name);
    }

    public static AggregationBuilder parse(String aggregationName, XContentParser parser) throws IOException {
        return PARSER.parse(parser, null, new CardinalityAggregationBuilder(aggregationName));
    }


    public CardinalityAggregationBuilder precisionThreshold(long precisionThreshold) {
        if (precisionThreshold < 0) {
            throw new IllegalArgumentException(
                    "[precisionThreshold] must be greater than or equal to 0. Found [" + precisionThreshold + "] in [" + name + "]");
        }
        this.precisionThreshold = precisionThreshold;
        return this;
    }


    @Override
    protected XContentBuilder doXContentBody(XContentBuilder builder) throws IOException {
        if (precisionThreshold != null) {
            builder.field(PRECISION_THRESHOLD_FIELD.getPreferredName(), precisionThreshold);
        }
        return builder;
    }

    @Override
    public String getType() {
        return NAME;
    }

}
