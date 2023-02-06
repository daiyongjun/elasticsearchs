package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.stats;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AggregationBuilder;

import java.io.IOException;

/**
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-metrics-stats-aggregation.html
 * <p>
 * 返回的统计信息包括：min、max、sum和avg
 */
public class StatsAggregationBuilder extends MetricsAggregationBuilder<StatsAggregationBuilder> {
    public static final String NAME = "stats";
    private static final ObjectParser<Void, StatsAggregationBuilder> PARSER;

    static {
        PARSER = new ObjectParser<>(StatsAggregationBuilder.NAME);
        declareValuesSourceFields(PARSER, true, true);
    }

    /**
     * 构造一个新的聚合构建器。
     *
     * @param name 当前聚合的名称
     */
    protected StatsAggregationBuilder(String name) {
        super(name);
    }

    public static AggregationBuilder parse(String aggregationName, XContentParser parser) throws IOException {
        return PARSER.parse(parser, null, new StatsAggregationBuilder(aggregationName));
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
