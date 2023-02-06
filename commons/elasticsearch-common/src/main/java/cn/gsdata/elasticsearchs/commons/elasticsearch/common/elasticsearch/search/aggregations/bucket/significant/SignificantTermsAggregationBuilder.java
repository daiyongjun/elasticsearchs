//package cn.yanwei.study.elasticsearch.search.aggregations.bucket.significant;
//
//import cn.yanwei.study.elasticsearch.search.aggregations.AggregationBuilderParser;
//import cn.yanwei.study.elasticsearch.search.aggregations.bucket.MultiBucketAggregationBuilder;
//import cn.yanwei.study.elasticsearch.search.aggregations.bucket.terms.BucketCount;
//import cn.yanwei.study.elasticsearch.search.aggregations.bucket.terms.IncludeExclude;
//import cn.yanwei.study.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
//import cn.yanwei.study.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;
//import cn.yanwei.study.elasticsearch.search.common.ParseField;
//import cn.yanwei.study.elasticsearch.search.common.xcontent.ObjectParser;
//import cn.yanwei.study.elasticsearch.search.common.xcontent.XContentBuilder;
//import cn.yanwei.study.elasticsearch.search.index.query.QueryBuilder;
//
//import java.io.IOException;
//
//import static cn.yanwei.study.elasticsearch.search.index.query.AbstractQueryBuilder.parseInnerQueryBuilder;
//
///**
// * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-bucket-significantterms-aggregation.html
// * 重要术语聚合，基于特定关键词下聚合，如上例中关键词是《英国交通警察》的crime_type《犯罪类型》的聚合结果。（会计算词频的）
// * 并且关键词必须使用分词的字段
// *
// * @author daiyongjun
// */
//public class SignificantTermsAggregationBuilder extends ValuesSourceAggregationBuilder<SignificantTermsAggregationBuilder> implements MultiBucketAggregationBuilder {
//    public static final String NAME = "significant_terms";
//
//    public static final ParseField REQUIRED_SIZE_FIELD_NAME = new ParseField("size");
//    public static final ParseField SHARD_SIZE_FIELD_NAME = new ParseField("shard_size");
//    public static final ParseField MIN_DOC_COUNT_FIELD_NAME = new ParseField("min_doc_count");
//    public static final ParseField SHARD_MIN_DOC_COUNT_FIELD_NAME = new ParseField("shard_min_doc_count");
//    public static final ParseField SHOW_TERM_DOC_COUNT_ERROR = new ParseField("show_term_doc_count_error");
//
//    static final ParseField BACKGROUND_FILTER = new ParseField("background_filter");
//    static final ParseField HEURISTIC = new ParseField("significance_heuristic");
//
//    /**
//     * 缺省值的设置 terms桶聚合默认使用聚合函数BUCKET_COUNT
//     * 默认情况下桶计数的值
//     */
//    private static final BucketCount DEFAULT_BUCKET_COUNT = new BucketCount(1, 0, 10, -1);
//
//    private IncludeExclude includeExclude = null;
//    private String executionHint = null;
//    private QueryBuilder filterBuilder = null;
//    /**
//     * TermsAggregationBuilder，内置的属性Bucket分组信息{} 有点类似于SearchSourceBuilder内置的QueryBuilder和aggregationsBuilder
//     * 基础的terms聚合{"size":0,"aggs":{"统计聚合统计":{"terms":{"field":"news_media","size":10}}}}内置了doc_count的聚合内容，其语法内容相同与
//     * {"size":0,"aggs":{"统计聚合统计":{"terms":{"field":"news_media","size":10},"aggs":{"doc_count":{"value_count":{"field":"news_propgation_level"}}}}}}的语法内容
//     */
//    private BucketCount bucketCount = new BucketCount(DEFAULT_BUCKET_COUNT);
//
//
//    private static final
//
//    public static AggregationBuilderParser<SignificantTermsAggregationBuilder> getParser(ParseFieldRegistry<SignificanceHeuristicParser> significanceHeuristicParserRegistry) {
//        ObjectParser<Void, SignificantTermsAggregationBuilder> aggregationParser = new ObjectParser<>(SignificantTermsAggregationBuilder.NAME);
//        ValuesSourceAggregationBuilder.declareValuesSourceFields(aggregationParser, true, true);
//        aggregationParser.declareInt(SignificantTermsAggregationBuilder::shardSize, SHARD_SIZE_FIELD_NAME);
//        aggregationParser.declareLong(SignificantTermsAggregationBuilder::minDocCount, MIN_DOC_COUNT_FIELD_NAME);
//        aggregationParser.declareLong(SignificantTermsAggregationBuilder::shardMinDocCount, SHARD_MIN_DOC_COUNT_FIELD_NAME);
//        aggregationParser.declareInt(SignificantTermsAggregationBuilder::size, REQUIRED_SIZE_FIELD_NAME);
//        aggregationParser.declareString(SignificantTermsAggregationBuilder::executionHint,
//                TermsAggregationBuilder.EXECUTION_HINT_FIELD_NAME);
//        aggregationParser.declareObject(SignificantTermsAggregationBuilder::backgroundFilter,
//                (p, context) -> parseInnerQueryBuilder(p),
//                SignificantTermsAggregationBuilder.BACKGROUND_FILTER);
//        aggregationParser.declareField((b, v) -> b.includeExclude(IncludeExclude.merge(v, b.includeExclude())),
//                IncludeExclude::parseInclude, IncludeExclude.INCLUDE_FIELD, ObjectParser.ValueType.OBJECT_ARRAY_OR_STRING);
//        aggregationParser.declareField((b, v) -> b.includeExclude(IncludeExclude.merge(b.includeExclude(), v)),
//                IncludeExclude::parseExclude, IncludeExclude.EXCLUDE_FIELD, ObjectParser.ValueType.STRING_ARRAY);
//        for (String name : significanceHeuristicParserRegistry.getNames()) {
//            aggregationParser.declareObject(SignificantTermsAggregationBuilder::significanceHeuristic,
//                    (p, context) -> {
//                        SignificanceHeuristicParser significanceHeuristicParser = significanceHeuristicParserRegistry
//                                .lookupReturningNullIfNotFound(name);
//                        return significanceHeuristicParser.parse(p);
//                    },
//                    new ParseField(name));
//        }
//    }
//
//    /**
//     * 构造一个新的聚合构建器。
//     *
//     * @param name 当前聚合的名称
//     */
//    protected SignificantTermsAggregationBuilder(String name) {
//        super(name);
//    }
//
//
//    public SignificantTermsAggregationBuilder shardSize(int shardSize) {
//        if (shardSize <= 0) {
//            throw new IllegalArgumentException(
//                    "[shardSize] must be greater than  0. Found [" + shardSize + "] in [" + name + "]");
//        }
//        bucketCount.setShardSize(shardSize);
//        return this;
//    }
//
//    public SignificantTermsAggregationBuilder minDocCount(long minDocCount) {
//        if (minDocCount < 0) {
//            throw new IllegalArgumentException(
//                    "[minDocCount] must be greater than or equal to 0. Found [" + minDocCount + "] in [" + name + "]");
//        }
//        bucketCount.setMinDocCount(minDocCount);
//        return this;
//    }
//
//    public SignificantTermsAggregationBuilder shardMinDocCount(long shardMinDocCount) {
//        if (shardMinDocCount < 0) {
//            throw new IllegalArgumentException(
//                    "[shardMinDocCount] must be greater than or equal to 0. Found [" + shardMinDocCount + "] in [" + name + "]");
//        }
//        bucketCount.setShardMinDocCount(shardMinDocCount);
//        return this;
//    }
//
//    public SignificantTermsAggregationBuilder size(int size) {
//        if (size <= 0) {
//            throw new IllegalArgumentException("[size] must be greater than 0. Found [" + size + "] in [" + name + "]");
//        }
//        bucketCount.setRequiredSize(size);
//        return this;
//    }
//
//    public SignificantTermsAggregationBuilder executionHint(String executionHint) {
//        this.executionHint = executionHint;
//        return this;
//    }
//
//    public SignificantTermsAggregationBuilder backgroundFilter(QueryBuilder backgroundFilter) {
//        if (backgroundFilter == null) {
//            throw new IllegalArgumentException("[backgroundFilter] must not be null: [" + name + "]");
//        }
//        this.filterBuilder = backgroundFilter;
//        return this;
//    }
//
//    public SignificantTermsAggregationBuilder includeExclude(IncludeExclude includeExclude) {
//        this.includeExclude = includeExclude;
//        return this;
//    }
//
//    /**
//     * Get terms to include and exclude from the aggregation results
//     */
//    public IncludeExclude includeExclude() {
//        return includeExclude;
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
