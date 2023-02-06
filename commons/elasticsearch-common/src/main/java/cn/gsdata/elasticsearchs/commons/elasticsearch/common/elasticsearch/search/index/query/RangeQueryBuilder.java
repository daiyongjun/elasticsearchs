package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.geo.ShapeRelation;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.joda.DateMathParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.joda.FormatDateTimeFormatter;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.joda.Joda;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.Query;
import org.joda.time.DateTimeZone;

import java.io.IOException;
import java.util.Objects;

/**
 * 基于Range的query类
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/query-dsl-range-query.html
 *
 * @author daiyongjun
 */
public class RangeQueryBuilder extends AbstractQueryBuilder<RangeQueryBuilder> implements MultiTermQueryBuilder {
    /**
     * Range的query类的名称
     */
    public static final String NAME = "range";

    public static final boolean DEFAULT_INCLUDE_UPPER = true;
    public static final boolean DEFAULT_INCLUDE_LOWER = true;

    /**
     * LT_FIELD,less than的简写、GT_FIELD,greater than的简写、LTE_FIELD,less than or equal的简写、GTE_FIELD,greater than or equal的简写、
     * FROM_FIELD,from 与less than表达的意思相同、TO_FIELD,to 与greater than表达的意思相同、INCLUDE_LOWER_FIELD,左闭区间 [、INCLUDE_UPPER_FIELD,表示右闭区间 ]、
     * TIME_ZONE_FIELD,时区的定义、TIME_ZONE_FIELD,时间格式、RELATION_FIELD,范围的关系relation字段上的范围查询支持一个关系参数该参数可以是WITHIN，CONTAINS，INTERSECTS（相交）
     */
    public static final ParseField LT_FIELD = new ParseField("lt");
    public static final ParseField GT_FIELD = new ParseField("gt");
    public static final ParseField LTE_FIELD = new ParseField("lte");
    public static final ParseField GTE_FIELD = new ParseField("gte");
    public static final ParseField FROM_FIELD = new ParseField("from");
    public static final ParseField TO_FIELD = new ParseField("to");
    private static final ParseField INCLUDE_LOWER_FIELD = new ParseField("include_lower");
    private static final ParseField INCLUDE_UPPER_FIELD = new ParseField("include_upper");
    private static final ParseField TIME_ZONE_FIELD = new ParseField("time_zone");
    private static final ParseField FORMAT_FIELD = new ParseField("format");
    private static final ParseField RELATION_FIELD = new ParseField("relation");
    /**
     * 字段名称
     */
    private final String fieldName;
    /**
     * 定义RangeQuery字段,from 与less than表达的意思相同
     */
    private Object from;
    /**
     * 定义RangeQuery字段,to 与greater than表达的意思相同
     */
    private Object to;
    /**
     * 定义RangeQuery字段,timeZone,时区的定义
     */
    private DateTimeZone timeZone;
    /**
     * 是否为左闭区间
     */
    private boolean includeLower = DEFAULT_INCLUDE_LOWER;
    /**
     * 是否为右闭区间
     */
    private boolean includeUpper = DEFAULT_INCLUDE_UPPER;
    /**
     * 时间格式基于joda-time作为时间格式化
     */
    private FormatDateTimeFormatter format;
    /**
     * 范围的关系relation字段上的范围查询支持一个关系参数
     */
    private ShapeRelation relation;

    /**
     * RangeQueryBuilder的构造方法
     *
     * @param fieldName String
     */
    public RangeQueryBuilder(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * 设置from参数的值
     *
     * @param from Object
     * @return RangeQueryBuilder
     */
    public RangeQueryBuilder from(Object from) {
        this.from = from;
        return this;
    }

    /**
     * 设置to参数的值
     *
     * @param to Object
     * @return RangeQueryBuilder
     */
    public RangeQueryBuilder to(Object to) {
        this.to = to;
        return this;
    }

    /**
     * 设置includeLower参数的值
     *
     * @param includeLower boolean
     * @return RangeQueryBuilder
     */
    public RangeQueryBuilder includeLower(boolean includeLower) {
        this.includeLower = includeLower;
        return this;
    }

    /**
     * 设置includeUpper参数的值
     *
     * @param includeUpper boolean
     * @return RangeQueryBuilder
     */
    public RangeQueryBuilder includeUpper(boolean includeUpper) {
        this.includeUpper = includeUpper;
        return this;
    }

    /**
     * 设置timeZone参数的值
     *
     * @param timeZone String
     * @return RangeQueryBuilder
     */
    public RangeQueryBuilder timeZone(String timeZone) {
        if (timeZone == null) {
            throw new IllegalArgumentException("timezone cannot be null");
        }
        this.timeZone = DateTimeZone.forID(timeZone);
        return this;
    }

    public DateTimeZone timeZone() {
        return this.timeZone;
    }

    public RangeQueryBuilder format(String format) {
        if (format == null) {
            throw new IllegalArgumentException("format cannot be null");
        }
        this.format = Joda.forPattern(format);
        return this;
    }

    public RangeQueryBuilder relation(String relation) {
        if (relation == null) {
            throw new IllegalArgumentException("relation cannot be null");
        }
        this.relation = ShapeRelation.getRelationByName(relation);
        if (this.relation == null) {
            throw new IllegalArgumentException(relation + " is not a valid relation");
        }
        if (!isRelationAllowed(this.relation)) {
            throw new IllegalArgumentException("[range] query does not support relation [" + relation + "]");
        }
        return this;
    }


    private boolean isRelationAllowed(ShapeRelation relation) {
        return relation == ShapeRelation.INTERSECTS
                || relation == ShapeRelation.CONTAINS
                || relation == ShapeRelation.WITHIN;
    }

    public static RangeQueryBuilder fromXContent(XContentParser parser, Object context) throws IOException {
        String fieldName = null;
        Object from = null;
        Object to = null;
        boolean includeLower = RangeQueryBuilder.DEFAULT_INCLUDE_LOWER;
        boolean includeUpper = RangeQueryBuilder.DEFAULT_INCLUDE_UPPER;
        String timeZone = null;
        float boost = DEFAULT_BOOST;
        String queryName = null;
        String format = null;
        String relation = null;
        String currentFieldName = null;
        XContentParser.Token token;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.START_OBJECT) {
                throwParsingExceptionOnMultipleFields(NAME, parser.getTokenLocation(), fieldName, currentFieldName);
                fieldName = currentFieldName;
                while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                    if (token == XContentParser.Token.FIELD_NAME) {
                        currentFieldName = parser.currentName();
                    } else {
                        if (FROM_FIELD.match(currentFieldName)) {
                            from = parser.objectText();
                        } else if (TO_FIELD.match(currentFieldName)) {
                            to = parser.objectText();
                        } else if (INCLUDE_LOWER_FIELD.match(currentFieldName)) {
                            includeLower = parser.booleanValue();
                        } else if (INCLUDE_UPPER_FIELD.match(currentFieldName)) {
                            includeUpper = parser.booleanValue();
                        } else if (BOOST_FIELD.match(currentFieldName)) {
                            boost = parser.floatValue();
                        } else if (GT_FIELD.match(currentFieldName)) {
                            from = parser.objectText();
                            includeLower = false;
                        } else if (GTE_FIELD.match(currentFieldName)) {
                            from = parser.objectText();
                            includeLower = true;
                        } else if (LT_FIELD.match(currentFieldName)) {
                            to = parser.objectText();
                            includeUpper = false;
                        } else if (LTE_FIELD.match(currentFieldName)) {
                            to = parser.objectText();
                            includeUpper = true;
                        } else if (TIME_ZONE_FIELD.match(currentFieldName)) {
                            timeZone = parser.text();
                        } else if (FORMAT_FIELD.match(currentFieldName)) {
                            format = parser.text();
                        } else if (RELATION_FIELD.match(currentFieldName)) {
                            relation = parser.text();
                        } else if (NAME_FIELD.match(currentFieldName)) {
                            queryName = parser.text();
                        } else {
                            throw new ParsingException(parser.getTokenLocation(),
                                    "[range] query does not support [" + currentFieldName + "]");
                        }
                    }
                }
            } else if (token.isValue()) {
                throw new ParsingException(parser.getTokenLocation(), "[range] query does not support [" + currentFieldName + "]");
            }
        }
        RangeQueryBuilder rangeQuery = new RangeQueryBuilder(fieldName);
        rangeQuery.from(from);
        rangeQuery.to(to);
        rangeQuery.includeLower(includeLower);
        rangeQuery.includeUpper(includeUpper);
        if (timeZone != null) {
            rangeQuery.timeZone(timeZone);
        }
        rangeQuery.boost(boost);
        rangeQuery.queryName(queryName);
        if (format != null) {
            rangeQuery.format(format);
        }
        if (relation != null) {
            rangeQuery.relation(relation);
        }
        //为定义的字段获取分片范围
        if (Objects.equals(fieldName, String.valueOf(context))) {
            FormatDateTimeFormatter formatter = Joda.forPattern(
                    "yyyy-MM-dd HH:mm:ss");
            DateTimeZone zone = rangeQuery.timeZone();
            zone = zone == null ? DateTimeZone.forID("+08:00") : zone;
            long startStamp = DateMathParser.parse(String.valueOf(from), System::currentTimeMillis, !includeLower, zone, formatter);
            long endStamp = DateMathParser.parse(String.valueOf(to), System::currentTimeMillis, !includeUpper, zone, formatter);
            rangeQuery.startStamp(startStamp);
            rangeQuery.endStamp(endStamp);
        }
        return rangeQuery;
    }

    @Override
    protected void doXContent(XContentBuilder builder) throws IOException {
        builder.startObject(NAME);
        builder.startObject(fieldName);
        builder.field(FROM_FIELD.getPreferredName(), this.from);
        builder.field(TO_FIELD.getPreferredName(), this.to);
        builder.field(INCLUDE_LOWER_FIELD.getPreferredName(), includeLower);
        builder.field(INCLUDE_UPPER_FIELD.getPreferredName(), includeUpper);
        if (timeZone != null) {
            builder.field(TIME_ZONE_FIELD.getPreferredName(), timeZone.getID());
        }
        if (format != null) {
            builder.field(FORMAT_FIELD.getPreferredName(), format.format());
        }
        if (relation != null) {
            builder.field(RELATION_FIELD.getPreferredName(), relation.getRelationName());
        }
        printBoostAndQueryName(builder);
        builder.endObject();
        builder.endObject();
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
}
