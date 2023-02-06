package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.terms;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.order.BucketOrder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.order.BucketOrderParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.order.BucketOrders;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.order.InternalOrder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.CollectionMode;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 基于terms的桶聚合
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-bucket-terms-aggregation.html
 * @author daiyongjun
 */
public class TermsAggregationBuilder extends ValuesSourceAggregationBuilder<TermsAggregationBuilder> {
    /**
     * 当前聚合名称
     */
    public static final String NAME = "terms";

    /**
     * 参考：官方文档内容https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-bucket-terms-aggregation.html
     * 定义ParseField字段；size(返回值长度)、shard_size(每个分片返回值长度)、min_doc_count（聚合纳入统计桶所拥有的最小文档的）、shard_min_doc_count(每个分片中返回含有最小匹配的文档数的桶)、
     * show_term_doc_count_error(显示聚合评估准确度的参考值)
     * execution_hint 聚合内存优化参数，（map、global_ordinals、global_ordinals_hash、global_ordinals_low_cardinality）详细见 https://www.liangzl.com/get-article-detail-196836.html
     **/
    public static final ParseField REQUIRED_SIZE_FIELD_NAME = new ParseField("size");
    public static final ParseField SHARD_SIZE_FIELD_NAME = new ParseField("shard_size");
    public static final ParseField MIN_DOC_COUNT_FIELD_NAME = new ParseField("min_doc_count");
    public static final ParseField SHARD_MIN_DOC_COUNT_FIELD_NAME = new ParseField("shard_min_doc_count");
    public static final ParseField SHOW_TERM_DOC_COUNT_ERROR = new ParseField("show_term_doc_count_error");
    /**
     * 使用IncludeExclude内置的INCLUDE_FIELD属性
     * public static final ParseField INCLUDE_FIELD = new ParseField("include");
     */
    public static final ParseField EXECUTION_HINT_FIELD_NAME = new ParseField("execution_hint");
    /**
     * 使用CollectMode内置的KEY的属性
     * public static final ParseField KEY = new ParseField("collect_mode");
     */
    public static final ParseField ORDER_FIELD_NAME = new ParseField("order");
    /**
     * 缺省值的设置 terms桶聚合默认使用聚合函数BUCKET_COUNT
     * 默认情况下桶计数的值
     */
    private static final BucketCount DEFAULT_BUCKET_COUNT = new BucketCount(1, 0, 10, -1);
    /**
     * 当定当前待解析的Object内含的属性以及相关属性的FieldParse
     */
    private static final ObjectParser<Void, TermsAggregationBuilder> PARSER;

    static {
        PARSER = new ObjectParser<>(TermsAggregationBuilder.NAME);
        //声明VBucketAggregationBuilder的Field
        ValuesSourceAggregationBuilder.declareValuesSourceFields(PARSER, true, true);
        //声明BucketCount属性
        PARSER.declareField(TermsAggregationBuilder::size, XContentParser::intValue, REQUIRED_SIZE_FIELD_NAME, ObjectParser.ValueType.INT);
        PARSER.declareField(TermsAggregationBuilder::shardSize, XContentParser::intValue, SHARD_SIZE_FIELD_NAME, ObjectParser.ValueType.INT);
        PARSER.declareField(TermsAggregationBuilder::minDocCount, (p) -> p.longValue(), MIN_DOC_COUNT_FIELD_NAME, ObjectParser.ValueType.LONG);
        PARSER.declareField(TermsAggregationBuilder::shardMinDocCount, (p) -> p.longValue(), SHARD_MIN_DOC_COUNT_FIELD_NAME, ObjectParser.ValueType.LONG);
        //声明TermsAggregationBuilder属性
        PARSER.declareField(TermsAggregationBuilder::showTermDocCountError, XContentParser::booleanValue, SHOW_TERM_DOC_COUNT_ERROR, ObjectParser.ValueType.BOOLEAN);
        PARSER.declareField((b, v) -> b.includeExclude(IncludeExclude.merge(v, b.includeExclude())), IncludeExclude::parseInclude, IncludeExclude.INCLUDE_FIELD, ObjectParser.ValueType.OBJECT_ARRAY_OR_STRING);
        PARSER.declareField((b, v) -> b.includeExclude(IncludeExclude.merge(b.includeExclude(), v)), IncludeExclude::parseExclude, IncludeExclude.EXCLUDE_FIELD, ObjectParser.ValueType.STRING_ARRAY);
        PARSER.declareField(TermsAggregationBuilder::executionHint, XContentParser::text, EXECUTION_HINT_FIELD_NAME, ObjectParser.ValueType.STRING);
        PARSER.declareField(TermsAggregationBuilder::collectMode, (p) -> CollectionMode.parse(p.text()), CollectionMode.KEY, ObjectParser.ValueType.STRING);
        PARSER.declareField(TermsAggregationBuilder::order, BucketOrderParser::parse, ORDER_FIELD_NAME, ObjectParser.ValueType.OBJECT_ARRAY);
    }

    /**
     * TermsAggregationBuilder，内置的属性Bucket分组信息{} 有点类似于SearchSourceBuilder内置的QueryBuilder和aggregationsBuilder
     * 基础的terms聚合{"size":0,"aggs":{"统计聚合统计":{"terms":{"field":"news_media","size":10}}}}内置了doc_count的聚合内容，其语法内容相同与
     * {"size":0,"aggs":{"统计聚合统计":{"terms":{"field":"news_media","size":10},"aggs":{"doc_count":{"value_count":{"field":"news_propgation_level"}}}}}}的语法内容
     */
    private BucketCount bucketCount = new BucketCount(DEFAULT_BUCKET_COUNT);
    /**
     * TermsAggregationBuilder内置属性    showTermDocCountError
     */
    private boolean showTermDocCountError = false;
    /**
     * TermsAggregationBuilder内置属性    includeExclude
     */
    private IncludeExclude includeExclude = null;

    /**
     * TermsAggregationBuilder内置属性    executionHint
     */
    private String executionHint = null;
    /**
     * TermsAggregationBuilder内置属性    collectMode
     */
    private CollectionMode collectMode = null;
    /**
     * TermsAggregationBuilder内置属性    BucketOrder
     */
    private BucketOrder order = new BucketOrders(Collections.singletonList(BucketOrder.count(false)));

    /**
     * 构造一个新的聚合构建器。
     *
     * @param name 当前聚合的名称
     */
    protected TermsAggregationBuilder(String name) {
        super(name);
    }


    @Override
    public String getType() {
        return NAME;
    }

    /**
     * 相当于Query注册fromXContent方法，XContentParser-------->Pojo核心处理类
     *
     * @param aggregationName 聚合名称
     * @param parser          XContentParser
     * @return AggregationBuilder
     * @throws IOException 解析过程异常
     */
    public static AggregationBuilder parse(String aggregationName, XContentParser parser) throws IOException {
        return PARSER.parse(parser, null, new TermsAggregationBuilder(aggregationName));
    }

    public TermsAggregationBuilder size(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("[size] must be greater than 0. Found [" + size + "] in [" + name + "]");

        }
        bucketCount.setRequiredSize(size);
        return this;
    }

    public TermsAggregationBuilder shardSize(int shardSize) {
        if (shardSize <= 0) {
            throw new IllegalArgumentException(
                    "[shardSize] must be greater than 0. Found [" + shardSize + "] in [" + name + "]");
        }
        bucketCount.setShardSize(shardSize);
        return this;
    }

    public TermsAggregationBuilder minDocCount(long minDocCount) {
        if (minDocCount < 0) {
            throw new IllegalArgumentException(
                    "[minDocCount] must be greater than or equal to 0. Found [" + minDocCount + "] in [" + name + "]");
        }
        bucketCount.setMinDocCount(minDocCount);
        return this;
    }

    public TermsAggregationBuilder shardMinDocCount(long shardMinDocCount) {
        if (shardMinDocCount < 0) {
            throw new IllegalArgumentException(
                    "[shardMinDocCount] must be greater than or equal to 0. Found [" + shardMinDocCount + "] in [" + name + "]");
        }
        bucketCount.setShardMinDocCount(shardMinDocCount);
        return this;
    }

    public TermsAggregationBuilder showTermDocCountError(boolean showTermDocCountError) {
        this.showTermDocCountError = showTermDocCountError;
        return this;
    }

    public TermsAggregationBuilder executionHint(String executionHint) {
        this.executionHint = executionHint;
        return this;
    }

    public TermsAggregationBuilder collectMode(CollectionMode collectMode) {
        if (collectMode == null) {
            throw new IllegalArgumentException("[collectMode] must not be null: [" + name + "]");
        }
        this.collectMode = collectMode;
        return this;
    }

    public IncludeExclude includeExclude() {
        return this.includeExclude;
    }

    public TermsAggregationBuilder includeExclude(IncludeExclude includeExclude) {
        this.includeExclude = includeExclude;
        return this;
    }

    public TermsAggregationBuilder order(List<BucketOrder> orders) {
        if (orders == null) {
            throw new IllegalArgumentException("[orders] must not be null: [" + name + "]");
        }
        // if the list only contains one order use that to avoid inconsistent xcontent
        order(orders.size() > 1 ? new BucketOrders(Collections.singletonList(order)) : orders.get(0));
        return this;
    }

    public TermsAggregationBuilder order(BucketOrder order) {
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
        //待处理逻辑
        bucketCount.toXContent(builder);
        builder.field(SHOW_TERM_DOC_COUNT_ERROR.getPreferredName(), showTermDocCountError);
        if (executionHint != null) {
            builder.field(TermsAggregationBuilder.EXECUTION_HINT_FIELD_NAME.getPreferredName(), executionHint);
        }
        builder.field(ORDER_FIELD_NAME.getPreferredName());
        order.toXContent(builder);
        if (collectMode != null) {
            builder.field(CollectionMode.KEY.getPreferredName(), collectMode.parseField().getPreferredName());
        }
        if (includeExclude != null) {
            includeExclude.toXContent(builder);
        }
        return builder;
    }
}
