package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.range;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AggregationBuilder;

import java.io.IOException;

/**
 * DateRange桶Builder类
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-bucket-daterange-aggregation.html
 *
 * @author daiyongjun
 */
public class DateRangeAggregationBuilder extends AbstractRangeAggregationBuilder<DateRangeAggregationBuilder, RangeBuilder> {
    public static final String NAME = "date_range";


    private static final ObjectParser<Void, DateRangeAggregationBuilder> PARSER;

    static {

        PARSER = new ObjectParser<>(DateRangeAggregationBuilder.NAME);
        ValuesSourceAggregationBuilder.declareValuesSourceFields(PARSER, true, true);
        PARSER.declareField(DateRangeAggregationBuilder::keyed, XContentParser::booleanValue, KEYED_FIELD, ObjectParser.ValueType.BOOLEAN);
        PARSER.declareObjectArray((agg, ranges) -> {
            for (RangeBuilder range : ranges) {
                agg.addRange(range);
            }
        }, (p, c) -> DateRangeAggregationBuilder.parseRange(p), RANGES_FIELD);
    }

    /**
     * DateRange桶Builder类的构造方法
     *
     * @param name 当前聚合的名称
     */
    protected DateRangeAggregationBuilder(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return NAME;
    }

    public static AggregationBuilder parse(String aggregationName, XContentParser parser) throws IOException {
        return PARSER.parse(parser, null, new DateRangeAggregationBuilder(aggregationName));
    }

    private static RangeBuilder parseRange(XContentParser parser) throws IOException {
        return RangeBuilder.fromXContent(parser);
    }
}
