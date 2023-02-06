package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.range;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AggregationBuilder;

import java.io.IOException;

/**
 * Range桶Builder类
 *
 * @author daiyongjun
 */
public class RangeAggregationBuilder extends AbstractRangeAggregationBuilder<RangeAggregationBuilder, RangeBuilder> {
    public static final String NAME = "range";

    private static final ObjectParser<Void, RangeAggregationBuilder> PARSER;

    static {
        PARSER = new ObjectParser<>(RangeAggregationBuilder.NAME);
        ValuesSourceAggregationBuilder.declareValuesSourceFields(PARSER, true, true);
        PARSER.declareField(RangeAggregationBuilder::keyed, XContentParser::booleanValue, KEYED_FIELD, ObjectParser.ValueType.BOOLEAN);
        PARSER.declareObjectArray((agg, ranges) -> {
            for (RangeBuilder range : ranges) {
                agg.addRange(range);
            }
        }, (p, c) -> RangeAggregationBuilder.parseRange(p), RANGES_FIELD);
    }

    /**
     * Range桶Builder类的构造方法
     *
     * @param name 当前聚合的名称
     */
    protected RangeAggregationBuilder(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return NAME;
    }

    public static AggregationBuilder parse(String aggregationName, XContentParser parser) throws IOException {
        return PARSER.parse(parser, null, new RangeAggregationBuilder(aggregationName));
    }

    private static RangeBuilder parseRange(XContentParser parser) throws IOException {
        return RangeBuilder.fromXContent(parser);
    }
}
