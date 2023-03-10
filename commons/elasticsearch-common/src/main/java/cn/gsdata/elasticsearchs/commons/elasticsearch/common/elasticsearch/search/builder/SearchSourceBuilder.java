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
 * ??????SearchSourceBuilder????????????queryBuilder??????aggregationsBuilder
 * ???????????????https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-request-body.html
 *
 * @author daiyongjun
 */
public class SearchSourceBuilder implements ToXContent {
    /**
     * ???????????????
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
     * ?????????????????????????????????????????????????????????????????????
     */
    private Long startStamp = DEFAULT_STAMP;
    /**
     * ?????????????????????????????????????????????????????????????????????
     */
    private long endStamp = DEFAULT_STAMP;

    public long startStamp() {
        return this.startStamp;
    }

    public long endStamp() {
        return this.endStamp;
    }

    /**
     * ???????????????
     */
    private QueryBuilder queryBuilder;
    /**
     * ??????????????????????????????????????????0
     */
    private int from = -1;
    /**
     * ?????????????????????????????????10
     */
    private int size = -1;
    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    private TimeValue timeout = null;
    /**
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????????terminateAfter??????
     */
    private int terminateAfter = SearchContext.DEFAULT_TERMINATE_AFTER;
    /**
     * ?????????????????????
     */
    private List<SortBuilder<?>> sorts;
    /**
     * ??????Source?????????Lucene??????????????????????????? ???????????????k-d tree????????????????????????doc-values?????????????????????store fields?????????source????????????????????????
     * ????????????????????????mapping?????????
     * "_source": {
     * "excludes": [
     * "news_content"
     * ]
     * }
     */
    private FetchSourceContext fetchSourceContext;
    /**
     * Lucene ??? stored fields ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? Java ??????????????? StoredField ???????????????????????????????????????????????? Document ??????
     * stored_fields?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????Source????????????
     * <p>
     * ???????????????https://blog.csdn.net/weixin_45526640/article/details/113679337
     */
    private StoredFieldsContext storedFieldsContext;
    /**
     * ?????????????????????????????????
     */
    private List<ScriptField> scriptFields;
    /**
     * ??????doc_values???????????????Lucene ?????????????????????????????????????????????block k-d tree????????????????????????doc values?????????????????? (store fields)  source?????????????????????
     * ????????????(term vectors???
     * ??????????????? https://blog.csdn.net/a745233700/article/details/117915118
     */
    private List<String> docValueFields;
    /**
     * post-filter??????????????????????????????query????????????????????????????????????query??????????????????query???????????????????????????query????????????
     * ?????????postQuery,??????Query
     * ???????????????https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-request-post-filter.html
     */
    private QueryBuilder postQueryBuilder;
    /**
     * ????????????
     * ???????????????https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-request-highlighting.html
     */
    private HighlightBuilder highlightBuilder;
    /**
     * ????????????
     */
    private List<RescorerBuilder> rescoreBuilders;

    /**
     * aggregationsBuilder?????????aggs???aggregations
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
     * SearchSourceBuilder ?????????
     *
     * @param parser XContentParser
     * @return SearchSourceBuilder
     * @throws IOException ????????????
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
                //token.isValue() ??????
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
                //START_OBJECT Object??????
            } else if (token == XContentParser.Token.START_OBJECT) {
                if (QUERY_FIELD.match(currentFieldName)) {
                    queryBuilder = AbstractQueryBuilder.parseInnerQueryBuilder(parser, context);
                    this.startStamp = queryBuilder.startStamp();
                    this.endStamp = queryBuilder.endStamp();
                    //???????????????????????????????????????????????????
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
                    //???????????????
                    //highlightBuilder = HighlightBuilder.fromXContent(parser);
                } else if (RESCORE_FIELD.match(currentFieldName)) {
                    rescoreBuilders = new ArrayList<>();
                    //???????????????
                    //rescoreBuilders.add(RescorerBuilder.parseFromXContent(parser));
                } else if (AGGREGATIONS_FIELD.match(currentFieldName)
                        || AGGS_FIELD.match(currentFieldName)) {
                    //????????????
                    aggregationsBuilder = AggregationsBuilderFactory.parseAggregators(parser);
                }
                //START_ARRAY ARRAY??????
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
     * QueryBuilder ?????????String
     *
     * @return String
     */
    @Override
    public final String toString() {
        return Strings.toString(this);
    }


}
