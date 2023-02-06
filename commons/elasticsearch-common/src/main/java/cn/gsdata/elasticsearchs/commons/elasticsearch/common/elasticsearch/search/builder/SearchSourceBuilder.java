package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.builder;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.common.unit.TimeValue;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ToXContent;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.fetch.StoredFieldsContext;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.fetch.subphase.FetchSourceContext;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query.AbstractQueryBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query.QueryBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.internal.SearchContext;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.sort.ScoreSortBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.sort.SortBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.sort.SortBuilders;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.rescore.RescorerBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.Strings;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.script.ScriptField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AggregationsBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AggregationsBuilderFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 定义SearchSourceBuilder类，内置queryBuilder以及aggregationsBuilder
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-request-body.html
 *
 * @author daiyongjun
 */
public class SearchSourceBuilder implements ToXContent {
    /**
     * 默认时间戳
     */
    private static final Long DEFAULT_STAMP = System.currentTimeMillis();

    public static final ParseField QUERY_FIELD = new ParseField("query");
    public static final ParseField FROM_FIELD = new ParseField("from");
    public static final ParseField SIZE_FIELD = new ParseField("size");
    public static final ParseField TIMEOUT_FIELD = new ParseField("timeout");
    public static final ParseField TERMINATE_AFTER_FIELD = new ParseField("terminate_after");
    public static final ParseField SORT_FIELD = new ParseField("sort");

    public static final ParseField _SOURCE_FIELD = new ParseField("_source");
    public static final ParseField STORED_FIELDS_FIELD = new ParseField("stored_fields");
    public static final ParseField SCRIPT_FIELDS_FIELD = new ParseField("script_fields");
    public static final ParseField DOCVALUE_FIELDS_FIELD = new ParseField("docvalue_fields");


    public static final ParseField POST_FILTER_FIELD = new ParseField("post_filter");
    public static final ParseField HIGHLIGHT_FIELD = new ParseField("highlight");
    public static final ParseField RESCORE_FIELD = new ParseField("rescore");


    public static final ParseField AGGS_FIELD = new ParseField("aggs");
    public static final ParseField AGGREGATIONS_FIELD = new ParseField("aggregations");
    /**
     * 基于待解析字段获取数据的时间范围，默认当前时间
     */
    private Long startStamp = DEFAULT_STAMP;
    /**
     * 基于待解析字段获取数据的时间范围，默认当前时间
     */
    private long endStamp = DEFAULT_STAMP;

    public long startStamp() {
        return this.startStamp;
    }

    public long endStamp() {
        return this.endStamp;
    }

    /**
     * 查询构建器
     */
    private QueryBuilder queryBuilder;
    /**
     * 从某个偏移量检索命中。默认为0
     */
    private int from = -1;
    /**
     * 要返回的命中数。默认为10
     */
    private int size = -1;
    /**
     * 搜索超时，限制要在指定时间值内执行的搜索请求，默认情况不限制超时时间
     */
    private TimeValue timeout = null;
    /**
     * 为每个分片收集的最大文档数，达到该数量后查询执行将提前终止。默认情况没有terminateAfter的值
     */
    private int terminateAfter = SearchContext.DEFAULT_TERMINATE_AFTER;
    /**
     * 文档的排序规则
     */
    private List<SortBuilder<?>> sorts;
    /**
     * 设置Source资源，Lucene内置了五种数据存储 倒排索引、k-d tree存储、列式存储（doc-values）、行式存储（store fields）其中source就是基于行式存储
     * 字段不存储可以在mapping中定义
     * "_source": {
     * "excludes": [
     * "news_content"
     * ]
     * }
     */
    private FetchSourceContext fetchSourceContext;
    /**
     * Lucene 的 stored fields 在存储的时候，会把文档的字段按照某种形式编码后存储，并且会按块进行压缩。所以获取存储字段的时候，先会对字段所在的块解压缩，然后将对应的字段值反序列化为 Java 对象，放到 StoredField 对象中，文档的所有字段组装成一个 Document 对象
     * stored_fields参数是关于被显式标记为存储在映射中的字段，默认情况下是关闭的，通常不推荐使用，可以使用Source字段代替
     * <p>
     * 参考文档：https://blog.csdn.net/weixin_45526640/article/details/113679337
     */
    private StoredFieldsContext storedFieldsContext;
    /**
     * 基于脚本定义的字段名称
     */
    private List<ScriptField> scriptFields;
    /**
     * 获取doc_values的字段值，Lucene 内置了五种数据存储：倒排索引、block k-d tree存储、列式存储（doc values）、行式存储 (store fields)  source就是其中之一、
     * 短语位置(term vectors）
     * 参考文档： https://blog.csdn.net/a745233700/article/details/117915118
     */
    private List<String> docValueFields;
    /**
     * post-filter，我们知道聚合是基于query内容，但是我们又要聚合按query的语法，但是query的的结果又不仅仅是query中的结果
     * 就有的postQuery,后置Query
     * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-request-post-filter.html
     */
    private QueryBuilder postQueryBuilder;
    /**
     * 高亮实现
     * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-request-highlighting.html
     */
    private HighlightBuilder highlightBuilder;
    /**
     * 重新记分
     */
    private List<RescorerBuilder> rescoreBuilders;

    /**
     * aggregationsBuilder的别称aggs和aggregations
     */
    private AggregationsBuilder aggregationsBuilder;


    public SearchSourceBuilder sort(String name) {
        if (name.equals(ScoreSortBuilder.NAME)) {
            return sort(SortBuilders.scoreSort());
        }
        return sort(SortBuilders.fieldSort(name));
    }

    /**
     * Adds a sort builder.
     */
    public SearchSourceBuilder sort(SortBuilder<?> sort) {
        if (sorts == null) {
            sorts = new ArrayList<>();
        }
        sorts.add(sort);
        return this;
    }

    /**
     * SearchSourceBuilder 解析类
     *
     * @param parser XContentParser
     * @return SearchSourceBuilder
     * @throws IOException 解析异常
     */
    public static SearchSourceBuilder fromXContent(XContentParser parser, Object context) throws IOException {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.parseXContent(parser, context);
        return builder;
    }


    public void parseXContent(XContentParser parser, Object context) throws IOException {
        XContentParser.Token token = parser.currentToken();
        String currentFieldName = null;
        if (token != XContentParser.Token.START_OBJECT && (token = parser.nextToken()) != XContentParser.Token.START_OBJECT) {
            throw new ParsingException(parser.getTokenLocation(), "Expected [" + XContentParser.Token.START_OBJECT +
                    "] but found [" + token + "]", parser.getTokenLocation());
        }
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
                //token.isValue() 单值
            } else if (token.isValue()) {
                if (FROM_FIELD.match(currentFieldName)) {
                    from = parser.intValue();
                } else if (SIZE_FIELD.match(currentFieldName)) {
                    size = parser.intValue();
                } else if (TIMEOUT_FIELD.match(currentFieldName)) {
                    timeout = TimeValue.parseTimeValue(parser.text(), null, TIMEOUT_FIELD.getPreferredName());
                } else if (TERMINATE_AFTER_FIELD.match(currentFieldName)) {
                    terminateAfter = parser.intValue();
                } else if (SORT_FIELD.match(currentFieldName)) {
                    sort(parser.text());
                } else {
                    throw new ParsingException(parser.getTokenLocation(), "Unknown key for a " + token + " in [" + currentFieldName + "].",
                            parser.getTokenLocation());
                }
                //START_OBJECT Object对象
            } else if (token == XContentParser.Token.START_OBJECT) {
                if (QUERY_FIELD.match(currentFieldName)) {
                    queryBuilder = AbstractQueryBuilder.parseInnerQueryBuilder(parser, context);
                    this.startStamp = queryBuilder.startStamp();
                    this.endStamp = queryBuilder.endStamp();
                    //根据传入的字段获取检索范围的时间戳
                    queryBuilder.getName();
                } else if (SORT_FIELD.match(currentFieldName)) {
                    sorts = new ArrayList<>(SortBuilder.fromXContent(parser));
                } else if (_SOURCE_FIELD.match(currentFieldName)) {
                    fetchSourceContext = FetchSourceContext.fromXContent(parser);
                } else if (SCRIPT_FIELDS_FIELD.match(currentFieldName)) {
                    scriptFields = new ArrayList<>();
                    while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                        scriptFields.add(new ScriptField(parser));
                    }
                } else if (POST_FILTER_FIELD.match(currentFieldName)) {
                    postQueryBuilder = AbstractQueryBuilder.parseInnerQueryBuilder(parser, context);
                } else if (HIGHLIGHT_FIELD.match(currentFieldName)) {
                    //待处理逻辑
                    //highlightBuilder = HighlightBuilder.fromXContent(parser);
                } else if (RESCORE_FIELD.match(currentFieldName)) {
                    rescoreBuilders = new ArrayList<>();
                    //待处理逻辑
                    //rescoreBuilders.add(RescorerBuilder.parseFromXContent(parser));
                } else if (AGGREGATIONS_FIELD.match(currentFieldName)
                        || AGGS_FIELD.match(currentFieldName)) {
                    //聚合逻辑
                    aggregationsBuilder = AggregationsBuilderFactory.parseAggregators(parser);
                }
                //START_ARRAY ARRAY对象
            } else if (token == XContentParser.Token.START_ARRAY) {
                if (STORED_FIELDS_FIELD.match(currentFieldName)) {
                    storedFieldsContext = StoredFieldsContext.fromXContent(STORED_FIELDS_FIELD.getPreferredName(), parser);
                } else if (DOCVALUE_FIELDS_FIELD.match(currentFieldName)) {
                    docValueFields = new ArrayList<>();
                    while ((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
                        if (token == XContentParser.Token.VALUE_STRING) {
                            docValueFields.add(parser.text());
                        } else {
                            throw new ParsingException(parser.getTokenLocation(), "Expected [" + XContentParser.Token.VALUE_STRING +
                                    "] in [" + currentFieldName + "] but found [" + token + "]", parser.getTokenLocation());
                        }
                    }
                } else if (SORT_FIELD.match(currentFieldName)) {
                    sorts = new ArrayList<>(SortBuilder.fromXContent(parser));
                } else {
                    throw new ParsingException(parser.getTokenLocation(), "Unknown key for a " + token + " in [" + currentFieldName + "].",
                            parser.getTokenLocation());
                }
            } else {
                throw new ParsingException(parser.getTokenLocation(), "Unknown key for a " + token + " in [" + currentFieldName + "].",
                        parser.getTokenLocation());
            }
        }
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        builder.startObject();
        if (from != -1) {
            builder.field(FROM_FIELD.getPreferredName(), from);
        }
        if (size != -1) {
            builder.field(SIZE_FIELD.getPreferredName(), size);
        }
        //
        //if (timeout != null && !timeout.equals(TimeValue.MINUS_ONE)) {
        //    builder.field(TIMEOUT_FIELD.getPreferredName(), timeout.getStringRep());
        //}

        if (terminateAfter != SearchContext.DEFAULT_TERMINATE_AFTER) {
            builder.field(TERMINATE_AFTER_FIELD.getPreferredName(), terminateAfter);
        }

        if (queryBuilder != null) {
            builder.field(QUERY_FIELD.getPreferredName(), queryBuilder);
        }

        if (postQueryBuilder != null) {
            builder.field(POST_FILTER_FIELD.getPreferredName(), postQueryBuilder);
        }

        //if (minScore != null) {
        //    builder.field(MIN_SCORE_FIELD.getPreferredName(), minScore);
        //}

        //if (version != null) {
        //    builder.field(VERSION_FIELD.getPreferredName(), version);
        //}
        //
        //if (explain != null) {
        //    builder.field(EXPLAIN_FIELD.getPreferredName(), explain);
        //}
        //
        //if (profile) {
        //    builder.field("profile", true);
        //}

        if (fetchSourceContext != null) {
            builder.field(_SOURCE_FIELD.getPreferredName(), fetchSourceContext);
        }

        if (storedFieldsContext != null) {
            storedFieldsContext.toXContent(STORED_FIELDS_FIELD.getPreferredName(), builder);
        }

        if (docValueFields != null) {
            builder.startArray(DOCVALUE_FIELDS_FIELD.getPreferredName());
            for (String docValueField : docValueFields) {
                builder.value(docValueField);
            }
            builder.endArray();
        }

        if (scriptFields != null) {
            builder.startObject(SCRIPT_FIELDS_FIELD.getPreferredName());
            for (ScriptField scriptField : scriptFields) {
                scriptField.toXContent(builder);
            }
            builder.endObject();
        }

        if (sorts != null) {
            builder.startArray(SORT_FIELD.getPreferredName());
            for (SortBuilder<?> sort : sorts) {
                sort.toXContent(builder);
            }
            builder.endArray();
        }

        //if (trackScores) {
        //    builder.field(TRACK_SCORES_FIELD.getPreferredName(), true);
        //}
        //
        //if (!trackTotalHits) {
        //    builder.field(TRACK_TOTAL_HITS_FIELD.getPreferredName(), false);
        //}
        //
        //if (searchAfterBuilder != null) {
        //    builder.array(SEARCH_AFTER.getPreferredName(), searchAfterBuilder.getSortValues());
        //}
        //
        //if (sliceBuilder != null) {
        //    builder.field(SLICE.getPreferredName(), sliceBuilder);
        //}
        //
        //if (!indexBoosts.isEmpty()) {
        //    builder.startArray(INDICES_BOOST_FIELD.getPreferredName());
        //    for (IndexBoost ib : indexBoosts) {
        //        builder.startObject();
        //        builder.field(ib.index, ib.boost);
        //        builder.endObject();
        //    }
        //    builder.endArray();
        //}

        if (aggregationsBuilder != null) {
            builder.field(AGGREGATIONS_FIELD.getPreferredName(), aggregationsBuilder);
        }

        if (highlightBuilder != null) {
            builder.field(HIGHLIGHT_FIELD.getPreferredName(), highlightBuilder);
        }
        //
        //if (suggestBuilder != null) {
        //    builder.field(SUGGEST_FIELD.getPreferredName(), suggestBuilder);
        //}
        //
        //if (rescoreBuilders != null) {
        //    builder.startArray(RESCORE_FIELD.getPreferredName());
        //    for (RescorerBuilder<?> rescoreBuilder : rescoreBuilders) {
        //        rescoreBuilder.toXContent(builder, params);
        //    }
        //    builder.endArray();
        //}
        //
        //if (stats != null) {
        //    builder.field(STATS_FIELD.getPreferredName(), stats);
        //}
        //
        //if (extBuilders != null && extBuilders.isEmpty() == false) {
        //    builder.startObject(EXT_FIELD.getPreferredName());
        //    for (SearchExtBuilder extBuilder : extBuilders) {
        //        extBuilder.toXContent(builder, params);
        //    }
        //    builder.endObject();
        //}
        //
        //if (collapse != null) {
        //    builder.field(COLLAPSE.getPreferredName(), collapse);
        //}
        builder.endObject();
        return builder;
    }


    /**
     * QueryBuilder 转换为String
     *
     * @return String
     */
    @Override
    public final String toString() {
        return Strings.toString(this);
    }


}
