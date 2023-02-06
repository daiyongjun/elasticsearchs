package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.sum;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;

import java.io.IOException;

/**
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-metrics-sum-aggregation.html
 *
 * @author daiyongjun
 */
public class SumAggregationBuilder extends MetricsAggregationBuilder<SumAggregationBuilder> {
    public static final String NAME = "sum";

    private static final ObjectParser<Void, SumAggregationBuilder> PARSER;

    static {
        PARSER = new ObjectParser<>(SumAggregationBuilder.NAME);
        ValuesSourceAggregationBuilder.declareValuesSourceFields(PARSER, true, true);
    }

    /**
     * 构造一个新的聚合构建器。
     *
     * @param name 当前聚合的名称
     */
    protected SumAggregationBuilder(String name) {
        super(name);
    }

    public static AggregationBuilder parse(String aggregationName, XContentParser parser) throws IOException {
        return PARSER.parse(parser, null, new SumAggregationBuilder(aggregationName));
    }

    @Override
    public String getType() {
        return NAME;
    }

    @Override
    protected XContentBuilder doXContentBody(XContentBuilder builder) throws IOException {
        return builder;
    }
}
