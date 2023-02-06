//package cn.yanwei.study.elasticsearch.search.aggregations.bucket.geogrid;
//
//import cn.yanwei.study.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;
//import cn.yanwei.study.elasticsearch.search.common.ParseField;
//import cn.yanwei.study.elasticsearch.search.common.xcontent.ObjectParser;
//import cn.yanwei.study.elasticsearch.search.common.xcontent.XContentBuilder;
//import cn.yanwei.study.elasticsearch.search.common.xcontent.XContentParser;
//
//import java.io.IOException;
//
///**
// * 参考文档： https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-bucket-geohashgrid-aggregation.html
// * https://www.elastic.co/guide/cn/elasticsearch/guide/current/geohash-grid-agg.html
// * @author daiyongjun
// */
//public class GeoGridAggregationBuilder extends ValuesSourceAggregationBuilder<GeoGridAggregationBuilder> {
//    public static final String NAME = "geohash_grid";
//
//    static final ParseField FIELD_PRECISION = new ParseField("precision");
//    static final ParseField FIELD_SIZE = new ParseField("size");
//    static final ParseField FIELD_SHARD_SIZE = new ParseField("shard_size");
//
//
//    public static final int DEFAULT_PRECISION = 5;
//    public static final int DEFAULT_MAX_NUM_CELLS = 10000;
//
//    private int precision = DEFAULT_PRECISION;
//    private int requiredSize = DEFAULT_MAX_NUM_CELLS;
//    private int shardSize = -1;
//
//    private static final ObjectParser<Void, GeoGridAggregationBuilder> PARSER;
//
//    static {
//        PARSER = new ObjectParser<>(GeoGridAggregationBuilder.NAME);
//        ValuesSourceAggregationBuilder.declareValuesSourceFields(PARSER, false, false);
//        PARSER.declareField((parser, builder, context) -> {
//            XContentParser.Token token = parser.currentToken();
//            if (token.equals(XContentParser.Token.VALUE_NUMBER)) {
//                builder.precision(XContentMapValues.nodeIntegerValue(parser.intValue()));
//            } else {
//                String precision = parser.text();
//                try {
//                    // we want to treat simple integer strings as precision levels, not distances
//                    builder.precision(XContentMapValues.nodeIntegerValue(Integer.parseInt(precision)));
//                } catch (NumberFormatException e) {
//                    // try to parse as a distance value
//                    try {
//                        builder.precision(GeoUtils.geoHashLevelsForPrecision(precision));
//                    } catch (NumberFormatException e2) {
//                        // can happen when distance unit is unknown, in this case we simply want to know the reason
//                        throw e2;
//                    } catch (IllegalArgumentException e3) {
//                        // this happens when distance too small, so precision > 12. We'd like to see the original string
//                        throw new IllegalArgumentException("precision too high [" + precision + "]", e3);
//                    }
//                }
//            }
//        }, FIELD_PRECISION, ObjectParser.ValueType.INT);
//    }
//
//    /**
//     * 构造一个新的聚合构建器。
//     *
//     * @param name 当前聚合的名称
//     */
//    protected GeoGridAggregationBuilder(String name) {
//        super(name);
//    }
//
//
//    public GeoGridAggregationBuilder precision(int precision) {
//        this.precision = checkPrecision(precision);
//        return this;
//    }
//
//
//    static int checkPrecision(int precision) {
//        if ((precision < 1) || (precision > 12)) {
//            throw new IllegalArgumentException("Invalid geohash aggregation precision of " + precision
//                    + ". Must be between 1 and 12.");
//        }
//        return precision;
//    }
//
//    @Override
//    public String getType() {
//        return NAME;
//    }
//
//    @Override
//    protected XContentBuilder doXContentBody(XContentBuilder builder) throws IOException {
//        return null;
//    }
//}
