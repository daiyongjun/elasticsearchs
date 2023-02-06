package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.Query;

import java.io.IOException;
import java.util.*;

/**
 * 基于bool的query类
 *
 * @author daiyongjun
 */
public class BoolQueryBuilder extends AbstractQueryBuilder<BoolQueryBuilder> {
    /**
     * 基于bool的query类的名称
     */
    public static final String NAME = "bool";
    /**
     * 定义BoolQuery的字段 must_not、filter、should、must
     */
    private static final String MUST_NOT = "must_not";
    private static final String FILTER = "filter";
    private static final String SHOULD = "should";
    private static final String MUST = "must";

    /**
     * 定义BoolQuery的ParseField字段（MINIMUM_SHOULD_MATCH表示should最低匹配度，支持数字、百分比如2、50%）
     */
    private static final ParseField MINIMUM_SHOULD_MATCH = new ParseField("minimum_should_match");
    /**
     * 定义BoolQuery的字段 must_not、filter、should、must对应的Query内容QueryBuilder
     */
    private final List<QueryBuilder> mustClauses = new ArrayList<>();
    private final List<QueryBuilder> mustNotClauses = new ArrayList<>();
    private final List<QueryBuilder> filterClauses = new ArrayList<>();
    private final List<QueryBuilder> shouldClauses = new ArrayList<>();

    /**
     * minimumShouldMatch表示should最低匹配度，支持数字、百分比如2、50%
     */
    private String minimumShouldMatch;

    /**
     * 设置minimumShouldMatch表示should最低匹配度，支持数字、百分比如2、50%
     *
     * @return String
     */
    public String minimumShouldMatch() {
        return this.minimumShouldMatch;
    }

    /**
     * 获取must中的查询
     *
     * @return List<QueryBuilder>
     */
    public List<QueryBuilder> must() {
        return this.mustClauses;
    }

    /**
     * 获取must not中的查询
     *
     * @return List<QueryBuilder>
     */
    public List<QueryBuilder> mustNot() {
        return this.mustNotClauses;
    }

    /**
     * 获取filter中的查询,不同于must,must not,should出现在匹配文档中但对评分没有贡献的查询
     *
     * @return List<QueryBuilder>
     */
    public List<QueryBuilder> filter() {
        return this.filterClauses;
    }

    /**
     * 获取should中的查询
     *
     * @return List<QueryBuilder>
     */
    public List<QueryBuilder> should() {
        return this.shouldClauses;
    }

    /**
     * 在must中添加一个查询
     *
     * @param queryBuilder 查询
     * @return BoolQueryBuilder
     */
    public BoolQueryBuilder must(QueryBuilder queryBuilder) {
        if (queryBuilder == null) {
            throw new IllegalArgumentException("inner bool query clause cannot be null");
        }
        mustClauses.add(queryBuilder);
        return this;
    }

    /**
     * 在filter中添加一个查询
     *
     * @param queryBuilder 查询
     * @return BoolQueryBuilder
     */
    public BoolQueryBuilder filter(QueryBuilder queryBuilder) {
        if (queryBuilder == null) {
            throw new IllegalArgumentException("inner bool query clause cannot be null");
        }
        filterClauses.add(queryBuilder);
        return this;
    }

    /**
     * 在must not中添加一个查询
     *
     * @param queryBuilder 查询
     * @return BoolQueryBuilder
     */
    public BoolQueryBuilder mustNot(QueryBuilder queryBuilder) {
        if (queryBuilder == null) {
            throw new IllegalArgumentException("inner bool query clause cannot be null");
        }
        mustNotClauses.add(queryBuilder);
        return this;
    }

    /**
     * 在should中添加一个查询
     *
     * @param queryBuilder 查询
     * @return BoolQueryBuilder
     */
    public BoolQueryBuilder should(QueryBuilder queryBuilder) {
        if (queryBuilder == null) {
            throw new IllegalArgumentException("inner bool query clause cannot be null");
        }
        shouldClauses.add(queryBuilder);
        return this;
    }

    /**
     * 获取minimumShouldMatch表示should最低匹配度，支持数字、百分比如2、50%
     *
     * @param minimumShouldMatch minimumShouldMatch表示should最低匹配度
     * @return BoolQueryBuilder
     */
    public BoolQueryBuilder minimumShouldMatch(String minimumShouldMatch) {
        this.minimumShouldMatch = minimumShouldMatch;
        return this;
    }

    @Override
    public Query toQuery(QueryShardContext context) throws IOException {
        return null;
    }

    @Override
    public Query toFilter(QueryShardContext context) throws IOException {
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }


    /**
     * XContentParser 解析成 BoolQueryBuilder核心处理类
     * 多逻辑(BoolQueryBuilder)
     * <p>
     * long[startStamp] startStamps;
     * long[endStamp] endStamps;
     * <p>
     * startStamps取最小值,作为stamp
     * endStamps取最大值,作为stamp
     *
     * @param parser XContentParser
     * @return BoolQueryBuilder
     * @throws IOException XContentParser -> Pojo 过程出现的异常情况
     */
    public static BoolQueryBuilder fromXContent(XContentParser parser, Object context) throws IOException, ParsingException {
        float boost = DEFAULT_BOOST;
        String minimumShouldMatch = null;

        final List<QueryBuilder> mustClauses = new ArrayList<>();
        final List<QueryBuilder> mustNotClauses = new ArrayList<>();
        final List<QueryBuilder> shouldClauses = new ArrayList<>();
        final List<QueryBuilder> filterClauses = new ArrayList<>();
        String queryName = null;

        String currentFieldName = null;
        XContentParser.Token token;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.START_OBJECT) {
                switch (Objects.requireNonNull(currentFieldName)) {
                    case MUST:
                        mustClauses.add(parseInnerQueryBuilder(parser, context));
                        break;
                    case SHOULD:
                        shouldClauses.add(parseInnerQueryBuilder(parser, context));
                        break;
                    case FILTER:
                        filterClauses.add(parseInnerQueryBuilder(parser, context));
                        break;
                    case MUST_NOT:
                        mustNotClauses.add(parseInnerQueryBuilder(parser, context));
                        break;
                    default:
                        throw new ParsingException(parser.getTokenLocation(), "[bool] query does not support [" + currentFieldName + "]");
                }
            } else if (token.isValue()) {
                if (MINIMUM_SHOULD_MATCH.match(currentFieldName)) {
                    minimumShouldMatch = parser.textOrNull();
                } else if (BOOST_FIELD.match(currentFieldName)) {
                    boost = parser.floatValue();
                } else if (NAME_FIELD.match(currentFieldName)) {
                    queryName = parser.text();
                } else {
                    throw new ParsingException(parser.getTokenLocation(), "[bool] query does not support [" + currentFieldName + "]");
                }
            }
        }
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        //设置stamps
        List<Long> andStartStamps = new ArrayList<>();
        List<Long> andEndStamps = new ArrayList<>();
        List<Long> orStartStamps = new ArrayList<>();
        List<Long> orEndStamps = new ArrayList<>();
        List<Long> startStamps = new ArrayList<>();
        List<Long> endStamps = new ArrayList<>();
        for (QueryBuilder queryBuilder : mustClauses) {
            andStartStamps.add(queryBuilder.startStamp());
            andEndStamps.add(queryBuilder.endStamp());
            boolQuery.must(queryBuilder);
        }
        for (QueryBuilder queryBuilder : filterClauses) {
            andStartStamps.add(queryBuilder.startStamp());
            andEndStamps.add(queryBuilder.endStamp());
            boolQuery.filter(queryBuilder);
        }
        for (QueryBuilder queryBuilder : mustNotClauses) {
            orStartStamps.add(queryBuilder.startStamp());
            orEndStamps.add(queryBuilder.endStamp());
            boolQuery.mustNot(queryBuilder);
        }
        //Should并非所有情况情况都是逻辑与，当minimumShouldMatch= shouldClauses.size()的时候为must
        if (minimumShouldMatch !=  null && (Integer.parseInt(minimumShouldMatch) == shouldClauses.size())) {
            for (QueryBuilder queryBuilder : shouldClauses) {
                andStartStamps.add(queryBuilder.startStamp());
                andEndStamps.add(queryBuilder.endStamp());
                boolQuery.should(queryBuilder);
            }
        } else {
            for (QueryBuilder queryBuilder : shouldClauses) {
                orStartStamps.add(queryBuilder.startStamp());
                orEndStamps.add(queryBuilder.endStamp());
                boolQuery.should(queryBuilder);
            }
        }
        //and逻辑取最小值
        if (andStartStamps.size() > 0) {
            andStartStamps.sort((p1, p2) -> (int) (p2 - p1));
            andEndStamps.sort((p1, p2) -> (int) (p1 - p2));
            startStamps.addAll(andStartStamps);
            endStamps.addAll(andEndStamps);
            //or逻辑取最大值
        } else if (orStartStamps.size() > 0) {
            orStartStamps.sort((p1, p2) -> (int) (p1 - p2));
            orEndStamps.sort((p1, p2) -> (int) (p2 - p1));
            startStamps.addAll(orStartStamps);
            endStamps.addAll(orEndStamps);
        }
        boolQuery.boost(boost);
        boolQuery.minimumShouldMatch(minimumShouldMatch);
        boolQuery.queryName(queryName);
        boolQuery.startStamp(startStamps.get(0));
        boolQuery.endStamp(endStamps.get(0));
        return boolQuery;
    }

    /**
     * Builder中写入BoolQuery(pojo)的数据，Builder.append(参数) 将pojo->Xcontent
     *
     * @param builder XContentBuilder
     * @throws IOException Builder中append(参数)异常
     */
    @Override
    protected void doXContent(XContentBuilder builder) throws IOException {
        builder.startObject(NAME);
        doXArrayContent(MUST, mustClauses, builder);
        doXArrayContent(FILTER, filterClauses, builder);
        doXArrayContent(MUST_NOT, mustNotClauses, builder);
        doXArrayContent(SHOULD, shouldClauses, builder);
        if (minimumShouldMatch != null) {
            builder.field(MINIMUM_SHOULD_MATCH.getPreferredName(), minimumShouldMatch);
        }
        printBoostAndQueryName(builder);
        builder.endObject();
    }

    /**
     * Builder.append(List<QueryBuilder> clauses)
     *
     * @param field   Append的字段名称
     * @param clauses Append(list<query类>)
     * @param builder 需要拼接的builder类
     * @throws IOException builder拼接异常
     */
    private static void doXArrayContent(String field, List<QueryBuilder> clauses, XContentBuilder builder)
            throws IOException {
        if (clauses.isEmpty()) {
            return;
        }
        builder.startArray(field);
        for (QueryBuilder clause : clauses) {
            clause.toXContent(builder);
        }
        builder.endArray();
    }
}
