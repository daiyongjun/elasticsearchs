package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.stats.extended;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;

import java.io.IOException;

/**
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-metrics-extendedstats-aggregation.html
 *
 * @author daiyongjun
 */
public class ExtendedStatsAggregationBuilder extends MetricsAggregationBuilder<ExtendedStatsAggregationBuilder> {
    public static final String NAME = "extended_stats";

    public static final ParseField SIGMA_FIELD = new ParseField("sigma");
    private double sigma = 2.0;

    private static final ObjectParser<Void, ExtendedStatsAggregationBuilder> PARSER;

    static {
        PARSER = new ObjectParser<>(ExtendedStatsAggregationBuilder.NAME);
        ValuesSourceAggregationBuilder.declareValuesSourceFields(PARSER, true, true);
        PARSER.declareDouble(ExtendedStatsAggregationBuilder::sigma, SIGMA_FIELD);
    }

    /**
     * 构造一个新的聚合构建器。
     *
     * @param name 当前聚合的名称
     */
    protected ExtendedStatsAggregationBuilder(String name) {
        super(name);
    }


    public static AggregationBuilder parse(String aggregationName, XContentParser parser) throws IOException {
        return PARSER.parse(parser, null, new ExtendedStatsAggregationBuilder(aggregationName));
    }

    public ExtendedStatsAggregationBuilder sigma(double sigma) {
        if (sigma < 0.0) {
            throw new IllegalArgumentException("[sigma] must be greater than or equal to 0. Found [" + sigma + "] in [" + name + "]");
        }
        this.sigma = sigma;
        return this;
    }

    @Override
    public String getType() {
        return NAME;
    }

    @Override
    protected XContentBuilder doXContentBody(XContentBuilder builder) throws IOException {
        builder.field(SIGMA_FIELD.getPreferredName(), sigma);
        return builder;
    }
}
