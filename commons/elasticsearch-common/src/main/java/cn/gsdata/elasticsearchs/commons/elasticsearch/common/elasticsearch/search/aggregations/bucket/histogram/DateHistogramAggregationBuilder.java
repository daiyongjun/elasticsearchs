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
 * 日期直方图聚合桶builder类，日期直方图和直方图类型，它只能应用于日期值
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-bucket-datehistogram-aggregation.html
 *
 * @author daiyongjun
 */
public class DateHistogramAggregationBuilder extends ValuesSourceAggregationBuilder<HistogramAggregationBuilder> {
    public static final String NAME = "date_histogram";

    public static final ParseField INTERVAL_FIELD = new ParseField("interval");
    public static final ParseField OFFSET_FIELD = new ParseField("offset");
    public static final ParseField KEYED_FIELD = new ParseField("keyed");
    public static final ParseField MIN_DOC_COUNT_FIELD = new ParseField("min_doc_count");
    public static final ParseField ORDER_FIELD = new ParseField("order");

    /**
     * 直方图的日期值间隔，单位ms
     */
    private long interval;

    /**
     * 直方图的日期值间隔，year (1y), quarter (1q), month (1M), week (1w), day (1d), hour (1h), minute (1m), second (1s)
     */
    private DateHistogramInterval dateHistogramInterval;

    /**
     * 时间桶边界的偏移量，基于long类型
     */
    private long offset = 0;

    /**
     * 时间桶边界的偏移量，基于String类型
     */
    private String stringOffset;

    /**
     * 将keyed标志设置为true，设置buckets返回的值为哈希buckets{key1:{},key2:{}};keyed标志为false,设置buckets返回是数组buckets:[{key:xxx},{key:xx}]
     */
    private boolean keyed = false;
    /**
     * 过滤小于minDocCount的值的桶，如minDocCount = 1，则{0-interval}[x],{interval-2interval}[xx,xxx,xxxx] 则由于{0-interval}只有一个元素因此被过滤
     */
    private long minDocCount = 0;

    /**
     * 通过extended_bounds设置，您现在可以“强制”直方图聚合以开始在特定 min值上构建存储桶
     */
    private ExtendedBounds extendedBounds;


    /**
     * BucketOrder
     */
    private BucketOrder order = new BucketOrders(Collections.singletonList(BucketOrder.count(false)));


    private static final ObjectParser<Void, DateHistogramAggregationBuilder> PARSER;

    static {
        PARSER = new ObjectParser<>(DateHistogramAggregationBuilder.NAME);
        ValuesSourceAggregationBuilder.declareValuesSourceFields(PARSER, true, true);
        PARSER.declareField((histogram, interval) -> {
            if (interval instanceof Long) {
                histogram.interval((long) interval);
            } else {
                histogram.dateHistogramInterval((DateHistogramInterval) interval);
            }
        }, p -> {
            if (p.currentToken() == XContentParser.Token.VALUE_NUMBER) {
                return p.longValue();
            } else {
                return new DateHistogramInterval(p.text());
            }
        }, INTERVAL_FIELD, ObjectParser.ValueType.LONG);
        PARSER.declareField((histogram, offset) -> {
            if (offset instanceof Long) {
                histogram.offset((Long) offset);
            } else {
                histogram.stringOffset((String) offset);
            }
        }, p -> {
            if (p.currentToken() == XContentParser.Token.VALUE_NUMBER) {
                return p.longValue();
            } else {
                return p.text();
            }
        }, OFFSET_FIELD, ObjectParser.ValueType.LONG);
        PARSER.declareBoolean(DateHistogramAggregationBuilder::keyed, KEYED_FIELD);
        PARSER.declareLong(DateHistogramAggregationBuilder::minDocCount, MIN_DOC_COUNT_FIELD);
        PARSER.declareField(DateHistogramAggregationBuilder::extendedBounds, parser -> ExtendedBounds.PARSER.apply(parser, null),
                ExtendedBounds.EXTENDED_BOUNDS_FIELD, ObjectParser.ValueType.OBJECT);
        PARSER.declareField(DateHistogramAggregationBuilder::order, BucketOrderParser::parse, ORDER_FIELD, ObjectParser.ValueType.OBJECT_ARRAY);
    }

    /**
     * 直方图聚合桶builder类构造器
     *
     * @param name 当前聚合的名称
     */
    protected DateHistogramAggregationBuilder(String name) {
        super(name);
    }

    public static DateHistogramAggregationBuilder parse(String aggregationName, XContentParser parser) throws IOException {
        return PARSER.parse(parser, null, new DateHistogramAggregationBuilder(aggregationName));
    }

    @Override
    public String getType() {
        return NAME;
    }

    /**
     * Set the offset on this builder, which is a number of milliseconds, and
     * return the builder so that calls can be chained.
     */
    public DateHistogramAggregationBuilder offset(long offset) {
        this.offset = offset;
        return this;
    }


    /**
     * Set the offset on this builder, which is a number of milliseconds, and
     * return the builder so that calls can be chained.
     */
    public DateHistogramAggregationBuilder stringOffset(String stringOffset) {
        this.stringOffset = stringOffset;
        return this;
    }


    /**
     * 设置基于ms为单位的时间间隔
     *
     * @param interval long
     * @return DateHistogramAggregationBuilder
     */
    public DateHistogramAggregationBuilder interval(long interval) {
        if (interval < 1) {
            throw new IllegalArgumentException("[interval] must be 1 or greater for histogram aggregation [" + name + "]");
        }
        this.interval = interval;
        return this;
    }

    /**
     * 设置基于日期直方图为单位的时间间隔，其间隔为year (1y), quarter (1q), month (1M), week (1w), day (1d), hour (1h), minute (1m), second (1s)
     *
     * @param dateHistogramInterval 日期直方图
     * @return DateHistogramAggregationBuilder
     */
    public DateHistogramAggregationBuilder dateHistogramInterval(DateHistogramInterval dateHistogramInterval) {
        if (dateHistogramInterval == null) {
            throw new IllegalArgumentException("[dateHistogramInterval] must not be null: [" + name + "]");
        }
        this.dateHistogramInterval = dateHistogramInterval;
        return this;
    }

    public DateHistogramAggregationBuilder keyed(boolean keyed) {
        this.keyed = keyed;
        return this;
    }

    public DateHistogramAggregationBuilder minDocCount(long minDocCount) {
        if (minDocCount < 0) {
            throw new IllegalArgumentException(
                    "[minDocCount] must be greater than or equal to 0. Found [" + minDocCount + "] in [" + name + "]");
        }
        this.minDocCount = minDocCount;
        return this;
    }

    /**
     * 设置extendedBounds的值
     *
     * @param extendedBounds ExtendedBounds设置待拓展边界
     * @return DateHistogramAggregationBuilder
     */
    public DateHistogramAggregationBuilder extendedBounds(ExtendedBounds extendedBounds) {
        if (extendedBounds == null) {
            throw new IllegalArgumentException("[extendedBounds] must not be null: [" + name + "]");
        }
        this.extendedBounds = extendedBounds;
        return this;
    }

    public DateHistogramAggregationBuilder order(BucketOrder order) {
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
        if (dateHistogramInterval == null) {
            builder.field(INTERVAL_FIELD.getPreferredName(), interval);
        } else {
            builder.field(INTERVAL_FIELD.getPreferredName(), dateHistogramInterval.toString());
        }
        if (stringOffset == null) {
            builder.field(OFFSET_FIELD.getPreferredName(), offset);
        } else {
            builder.field(OFFSET_FIELD.getPreferredName(), stringOffset);
        }
        if (order != null) {
            builder.field(ORDER_FIELD.getPreferredName());
            order.toXContent(builder);
        }

        builder.field(KEYED_FIELD.getPreferredName(), keyed);

        builder.field(MIN_DOC_COUNT_FIELD.getPreferredName(), minDocCount);
        if (extendedBounds != null) {
            extendedBounds.toXContent(builder);
        }
        return builder;
    }
}
