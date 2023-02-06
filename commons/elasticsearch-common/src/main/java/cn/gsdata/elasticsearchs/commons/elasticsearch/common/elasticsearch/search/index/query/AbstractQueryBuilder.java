package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.Strings;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.NamedXContentRegistry;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentLocation;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;

import java.io.IOException;

/**
 * 抽象QueryBuilder类
 *
 * @author daiyongjun
 */
public abstract class AbstractQueryBuilder<QB extends AbstractQueryBuilder<QB>> implements QueryBuilder {
    /**
     * 默认时间戳
     */
    Long DEFAULT_STAMP = System.currentTimeMillis();
    /**
     * boost 默认值
     */
    public static final float DEFAULT_BOOST = 1.0f;
    public static final ParseField NAME_FIELD = new ParseField("_name");
    public static final ParseField BOOST_FIELD = new ParseField("boost");
    protected String queryName;
    protected float boost = DEFAULT_BOOST;
    /**
     * 基于待解析字段获取数据的时间范围，默认当前时间
     */
    Long startStamp = DEFAULT_STAMP;
    /**
     * 基于待解析字段获取数据的时间范围，默认当前时间
     */
    Long endStamp = DEFAULT_STAMP;

    /**
     * 返回查询的查询名称。
     *
     * @return String
     */
    @Override
    public final String queryName() {
        return queryName;
    }


    /**
     * 设置要分配给查询的任意名称（请参阅命名查询），并返回当QueryBuilder。
     *
     * @param queryName 设置查询名称
     * @return QueryBuilder
     */
    @Override
    @SuppressWarnings("unchecked")
    public final QB queryName(String queryName) {
        this.queryName = queryName;
        return (QB) this;
    }

    /**
     * 返回此查询的权重设置。
     *
     * @return float
     */
    @Override
    public final float boost() {
        return this.boost;
    }

    /**
     * 设置查询的权重，并返回该查询。
     *
     * @param boost 权重
     * @return QB
     */
    @Override
    @SuppressWarnings("unchecked")
    public final QB boost(float boost) {
        this.boost = boost;
        return (QB) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public QB startStamp(Long startStamp) {
        this.startStamp = startStamp;
        return (QB) this;
    }

    public long startStamp() {
        return this.startStamp;
    }

    @Override
    @SuppressWarnings("unchecked")
    public QB endStamp(Long endStamp) {
        this.endStamp = endStamp;
        return (QB) this;
    }

    public long endStamp() {
        return this.endStamp;
    }

    /**
     * QueryBuilder相关解析类，使用XContentParser解析为QueryBuilder类
     */
    public static QueryBuilder parseInnerQueryBuilder(XContentParser parser, Object context) throws IOException {
        if (parser.currentToken() != XContentParser.Token.START_OBJECT) {
            if (parser.nextToken() != XContentParser.Token.START_OBJECT) {
                throw new ParsingException(parser.getTokenLocation(), "[_na] query malformed, must start with start_object");
            }
        }
        if (parser.nextToken() == XContentParser.Token.END_OBJECT) {
            // we encountered '{}' for a query clause, it used to be supported, deprecated in 5.0 and removed in 6.0
            throw new IllegalArgumentException("query malformed, empty clause found at [" + parser.getTokenLocation() + "]");
        }
        if (parser.currentToken() != XContentParser.Token.FIELD_NAME) {
            throw new ParsingException(parser.getTokenLocation(), "[_na] query malformed, no field after start_object");
        }
        String queryName = parser.currentName();
        // move to the next START_OBJECT
        if (parser.nextToken() != XContentParser.Token.START_OBJECT) {
            throw new ParsingException(parser.getTokenLocation(), "[" + queryName + "] query malformed, no start_object after query name");
        }
        QueryBuilder result;
        try {
            result = parser.namedObject(QueryBuilder.class, queryName, context);
        } catch (NamedXContentRegistry.UnknownNamedObjectException e) {
            // Preserve the error message from 5.0 until we have a compellingly better message so we don't break BWC.
            // This intentionally doesn't include the causing exception because that'd change the "root_cause" of any unknown query errors
            throw new ParsingException(new XContentLocation(e.getLineNumber(), e.getColumnNumber()),
                    "no [query] registered for [" + e.getName() + "]");
        }
        //end_object of the specific query (e.g. match, multi_match etc.) element
        if (parser.currentToken() != XContentParser.Token.END_OBJECT) {
            throw new ParsingException(parser.getTokenLocation(),
                    "[" + queryName + "] malformed query, expected [END_OBJECT] but found [" + parser.currentToken() + "]");
        }
        //end_object of the query object
        if (parser.nextToken() != XContentParser.Token.END_OBJECT) {
            throw new ParsingException(parser.getTokenLocation(),
                    "[" + queryName + "] malformed query, expected [END_OBJECT] but found [" + parser.currentToken() + "]");
        }
        return result;
    }


    protected static void throwParsingExceptionOnMultipleFields(String queryName, XContentLocation contentLocation,
                                                                String processedFieldName, String currentFieldName) {
        if (processedFieldName != null) {
            throw new ParsingException(contentLocation, "[" + queryName + "] query doesn't support multiple fields, found ["
                    + processedFieldName + "] and [" + currentFieldName + "]");
        }
    }


    protected void printBoostAndQueryName(XContentBuilder builder) throws IOException {
        builder.field(BOOST_FIELD.getPreferredName(), boost);
        if (queryName != null) {
            builder.field(NAME_FIELD.getPreferredName(), queryName);
        }
    }


    /**
     * 按具体实现QueryBuilder类，Append出特定的XContentBuilder
     *
     * @param builder XContentBuilder
     * @return XContentBuilder
     * @throws IOException Builder进行Append过程中出现异常
     */
    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        builder.startObject();
        doXContent(builder);
        builder.endObject();
        return builder;
    }

    /**
     * 按具体实现QueryBuilder类，Append出特定的XContentBuilder
     *
     * @param builder XContentBuilder
     * @throws IOException 将Params拼接到XContentBuilder异常
     */
    protected abstract void doXContent(XContentBuilder builder) throws IOException;


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
