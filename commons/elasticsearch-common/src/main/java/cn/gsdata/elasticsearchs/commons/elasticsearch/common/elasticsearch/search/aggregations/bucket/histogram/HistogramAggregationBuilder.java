package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.histogram;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.order.BucketOrder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.order.BucketOrderParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.order.BucketOrders;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.order.InternalOrder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;

import java.io.IOException;
import java.util.Collections;

/**
 * 直方图聚合桶builder类
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-bucket-histogram-aggregation.html
 *
 * @author daiyongjun
 */
public class HistogramAggregationBuilder extends ValuesSourceAggregationBuilder<HistogramAggregationBuilder> {
    public static final String NAME = "histogram";

    public static final ParseField INTERVAL_FIELD = new ParseField("interval");
    public static final ParseField OFFSET_FIELD = new ParseField("offset");
    public static final ParseField KEYED_FIELD = new ParseField("keyed");
    public static final ParseField MIN_DOC_COUNT_FIELD = new ParseField("min_doc_count");
    static final ParseField EXTENDED_BOUNDS_FIELD = new ParseField("extended_bounds");
    public static final ParseField ORDER_FIELD = new ParseField("order");

    /**
     * 直方图聚合桶是动态构建固定大小（也称为间隔）的桶，interval为动态构建固定大小间隔的大小
     */
    private double interval;
    /**
     * 桶边界的偏移量，如果如果有 10 个文档的值从 5 到 14，[5,6,7,8,9,10,11,12,13,14,15]当设置间隔为interval 10，则直方图会分为两个桶{0-10}[5,6,7,8,9,10],{11-20}[11,12,13,14,15]
     * 当我们设置offset=5,也就是偏移量为5的时候，则直方图由原来的{0,10},{11-20}进行偏移5个单位{5,15}[5,6,7,8,9,10,11,12,13,14,15]
     */
    private double offset = 0;
    /**
     * 将keyed标志设置为true，设置buckets返回的值为哈希buckets{key1:{},key2:{}};keyed标志为false,设置buckets返回是数组buckets:[{key:xxx},{key:xx}]
     */
    private boolean keyed = false;
    /**
     * 过滤小于minDocCount的值的桶，如minDocCount = 1，则{0-interval}[x],{interval-2interval}[xx,xxx,xxxx] 则由于{0-interval}只有一个元素因此被过滤
     */
    private long minDocCount = 0;
    /**
     * 通过extended_bounds设置，内置的minBound，
     * 由于extended_bouds结构简单抽象成new double[] {minBound,maxBound}
     */
    private double minBound = Double.POSITIVE_INFINITY;
    /**
     * 通过extended_bounds设置，内置的maxBound，
     * 由于extended_bouds结构简单抽象成new double[] {minBound,maxBound}
     */
    private double maxBound = Double.NEGATIVE_INFINITY;
    /**
     * BucketOrder
     */
    private BucketOrder order = new BucketOrders(Collections.singletonList(BucketOrder.count(false)));

    /**
     * 定制double[]  extended_bounds对象ObjectParser<double[], Void>
     */
    private static final ObjectParser<Void, double[]> EXTENDED_BOUNDS_PARSER = new ObjectParser<>(
            EXTENDED_BOUNDS_FIELD.getPreferredName(),
            () -> new double[]{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY});

    static {
        EXTENDED_BOUNDS_PARSER.declareDouble((bounds, d) -> bounds[0] = d, new ParseField("min"));
        EXTENDED_BOUNDS_PARSER.declareDouble((bounds, d) -> bounds[1] = d, new ParseField("max"));
    }


    private static final ObjectParser<Void, HistogramAggregationBuilder> PARSER;

    static {
        PARSER = new ObjectParser<>(HistogramAggregationBuilder.NAME);
        ValuesSourceAggregationBuilder.declareValuesSourceFields(PARSER, true, true);
        PARSER.declareDouble(HistogramAggregationBuilder::interval, INTERVAL_FIELD);
        PARSER.declareDouble(HistogramAggregationBuilder::offset, OFFSET_FIELD);
        PARSER.declareBoolean(HistogramAggregationBuilder::keyed, KEYED_FIELD);
        PARSER.declareLong(HistogramAggregationBuilder::minDocCount, MIN_DOC_COUNT_FIELD);
        PARSER.declareField((histogram, extendedBounds) -> {
            histogram.extendedBounds(extendedBounds[0], extendedBounds[1]);
        }, parser -> EXTENDED_BOUNDS_PARSER.apply(parser, null), EXTENDED_BOUNDS_FIELD, ObjectParser.ValueType.OBJECT);


        PARSER.declareField(HistogramAggregationBuilder::order, BucketOrderParser::parse, ORDER_FIELD, ObjectParser.ValueType.OBJECT_ARRAY);
    }

    /**
     * 直方图聚合桶builder类构造器
     *
     * @param name 当前聚合的名称
     */
    protected HistogramAggregationBuilder(String name) {
        super(name);
    }

    public static HistogramAggregationBuilder parse(String aggregationName, XContentParser parser) throws IOException {
        return PARSER.parse(parser, null, new HistogramAggregationBuilder(aggregationName));
    }

    @Override
    public String getType() {
        return NAME;
    }

    /**
     * 设置HistogramAggregationBuilder属性interval的值
     *
     * @param interval double
     * @return HistogramAggregationBuilder
     */
    public HistogramAggregationBuilder interval(double interval) {
        if (interval <= 0) {
            throw new IllegalArgumentException("[interval] must be >0 for histogram aggregation [" + name + "]");
        }
        this.interval = interval;
        return this;
    }

    /**
     * 设置HistogramAggregationBuilder属性offset的值
     *
     * @param offset double
     * @return HistogramAggregationBuilder
     */
    public HistogramAggregationBuilder offset(double offset) {
        this.offset = offset;
        return this;
    }

    /**
     * 设置HistogramAggregationBuilder属性keyed的值
     *
     * @param keyed boolean
     * @return HistogramAggregationBuilder
     */
    public HistogramAggregationBuilder keyed(boolean keyed) {
        this.keyed = keyed;
        return this;
    }

    /**
     * 设置HistogramAggregationBuilder属性minDocCount的值
     *
     * @param minDocCount long
     * @return HistogramAggregationBuilder
     */
    public HistogramAggregationBuilder minDocCount(long minDocCount) {
        if (minDocCount < 0) {
            throw new IllegalArgumentException(
                    "[minDocCount] must be greater than or equal to 0. Found [" + minDocCount + "] in [" + name + "]");
        }
        this.minDocCount = minDocCount;
        return this;
    }


    public HistogramAggregationBuilder extendedBounds(double minBound, double maxBound) {
        if (!Double.isFinite(minBound)) {
            throw new IllegalArgumentException("minBound must be finite, got: " + minBound);
        }
        if (!Double.isFinite(maxBound)) {
            throw new IllegalArgumentException("maxBound must be finite, got: " + maxBound);
        }
        if (maxBound < minBound) {
            throw new IllegalArgumentException("maxBound [" + maxBound + "] must be greater than minBound [" + minBound + "]");
        }
        this.minBound = minBound;
        this.maxBound = maxBound;
        return this;
    }

    public HistogramAggregationBuilder order(BucketOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("[order] must not be null: [" + name + "]");
        }
        if (order instanceof BucketOrders || InternalOrder.isKeyOrder(order)) {
            this.order = order;
        } else {
            this.order = new BucketOrders(Collections.singletonList(order));
        }
        return this;
    }

    @Override
    protected XContentBuilder doXContentBody(XContentBuilder builder) throws IOException {
        builder.field(INTERVAL_FIELD.getPreferredName(), interval);
        builder.field(OFFSET_FIELD.getPreferredName(), offset);
        if (order != null) {
            builder.field(ORDER_FIELD.getPreferredName());
            order.toXContent(builder);
        }
        builder.field(KEYED_FIELD.getPreferredName(), keyed);

        builder.field(MIN_DOC_COUNT_FIELD.getPreferredName(), minDocCount);

        if (Double.isFinite(minBound) || Double.isFinite(maxBound)) {
            builder.startObject(EXTENDED_BOUNDS_FIELD.getPreferredName());
            if (Double.isFinite(minBound)) {
                builder.field("min", minBound);
            }
            if (Double.isFinite(maxBound)) {
                builder.field("max", maxBound);
            }
            builder.endObject();
        }
        return builder;
    }
}
