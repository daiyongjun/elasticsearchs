package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.avg;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;

import java.io.IOException;

/**
 * 基于avg的指标
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-metrics-avg-aggregation.html
 * @author daiyongjun
 */
public class AvgAggregationBuilder extends MetricsAggregationBuilder<AvgAggregationBuilder> {
    /**
     * 当前指标名称
     */
    public static final String NAME = "avg";
    private static final ObjectParser<Void, AvgAggregationBuilder> PARSER;

    static {
        PARSER = new ObjectParser<>(AvgAggregationBuilder.NAME);
        //声明ValuesSourceAggregationBuilder的Field
        ValuesSourceAggregationBuilder.declareValuesSourceFields(PARSER, true, true);
    }

    /**
     * 构造一个新的聚合构建器。
     *
     * @param name 当前聚合的名称
     */
    protected AvgAggregationBuilder(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return NAME;
    }


    public static AggregationBuilder parse(String aggregationName, XContentParser parser) throws IOException {
        return PARSER.parse(parser, null, new AvgAggregationBuilder(aggregationName));
    }


    @Override
    protected XContentBuilder doXContentBody(XContentBuilder builder) throws IOException {
        return builder;
    }
}
