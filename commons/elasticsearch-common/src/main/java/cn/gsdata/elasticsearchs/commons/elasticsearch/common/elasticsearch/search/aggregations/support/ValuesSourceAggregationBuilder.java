package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.support;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.script.Script;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.joda.time.DateTimeZone;

import java.io.IOException;

/**
 * @author daiyongjun
 */
public abstract class ValuesSourceAggregationBuilder<AB extends ValuesSourceAggregationBuilder<AB>> extends AbstractAggregationBuilder<AB> {
    /**
     * 聚合的自定义名称
     */
    private String field = null;
    /**
     * 脚本语法内容
     */
    private Script script = null;
    /**
     * 设置当聚合在文档中发现缺失值时使用的值，如：document为4个student信息，需要avg
     * student的score分数，但是某个学生没有分数这个字段，则missing则就是为缺失分数的学生设置默认值
     */
    private Object missing = null;
    /**
     * 设置用于聚合输出结果的格式
     */
    private String format = null;
    /**
     * 设置时区格式
     */
    private DateTimeZone timeZone = null;


    public static final ParseField FIELD_NAME = new ParseField("field");
    public static final ParseField MISSING_FIELD_NAME = new ParseField("missing");
    public static final ParseField FORMAT_FIELD_NAME = new ParseField("format");
    public static final ParseField TIME_ZONE_FIELD_NAME = new ParseField("time_zone");


    public static <T> void declareValuesSourceFields(
            ObjectParser<T, ? extends ValuesSourceAggregationBuilder<?>> objectParser,
            boolean scriptable, boolean formatter) {
        declareFields(objectParser, scriptable, formatter, true);
    }

    /**
     * 声明BucketAggregationBuilder下定义的属性值
     *
     * @param objectParser  field属性内置的解析器
     * @param scriptable    是否支持script标识
     * @param formattable   是否支持format标识
     * @param timezoneAware 是否支持timezone标识
     * @param <T>           声明范形类
     */
    public static <T> void declareFields(
            ObjectParser<T, ? extends ValuesSourceAggregationBuilder<?>> objectParser, boolean scriptable, boolean formattable, boolean timezoneAware) {
        objectParser.declareField(ValuesSourceAggregationBuilder::field, XContentParser::text,
                new ParseField("field"), ObjectParser.ValueType.STRING);

        objectParser.declareField(ValuesSourceAggregationBuilder::missing, XContentParser::objectText,
                new ParseField("missing"), ObjectParser.ValueType.VALUE);

        if (formattable) {
            objectParser.declareField(ValuesSourceAggregationBuilder::format, XContentParser::text,
                    new ParseField("format"), ObjectParser.ValueType.STRING);
        }
        if (scriptable) {
            objectParser.declareField(ValuesSourceAggregationBuilder::script,
                    (parser, context) -> Script.parse(parser),
                    Script.SCRIPT_PARSE_FIELD, ObjectParser.ValueType.OBJECT_OR_STRING);
        }
        if (timezoneAware) {
            objectParser.declareField(ValuesSourceAggregationBuilder::timeZone, p -> {
                if (p.currentToken() == XContentParser.Token.VALUE_STRING) {
                    return DateTimeZone.forID(p.text());
                } else {
                    return DateTimeZone.forOffsetHours(p.intValue());
                }
            }, TIME_ZONE_FIELD_NAME, ObjectParser.ValueType.LONG);
        }
    }

    /**
     * 构造一个新的聚合构建器。
     *
     * @param name 当前聚合的名称
     */
    protected ValuesSourceAggregationBuilder(String name) {
        super(name);
    }

    /**
     * 设置聚合的自定义名称。
     *
     * @param field 字段名称
     * @return AB
     */
    @SuppressWarnings("unchecked")
    public AB field(String field) {
        if (field == null) {
            throw new IllegalArgumentException("[field] must not be null: [" + name + "]");
        }
        this.field = field;
        return (AB) this;
    }

    /**
     * 获取用于此聚合的字段。
     *
     * @return String
     */
    public String field() {
        return field;
    }

    /**
     * 设置脚本内容
     *
     * @param script Script
     * @return AB
     */
    public AB script(Script script) {
        if (script == null) {
            throw new IllegalArgumentException("[script] must not be null: [" + name + "]");
        }
        this.script = script;
        return (AB) this;
    }

    /**
     * 获取脚本内容
     *
     * @return Script
     */
    public Script script() {
        return script;
    }

    /**
     * 设置用于聚合输出的格式。
     *
     * @param format 输出格式
     * @return AB
     */
    @SuppressWarnings("unchecked")
    public AB format(String format) {
        if (format == null) {
            throw new IllegalArgumentException("[format] must not be null: [" + name + "]");
        }
        this.format = format;
        return (AB) this;
    }

    /**
     * 获取用于聚合输出的格式。
     *
     * @return String
     */
    public String format() {
        return format;
    }

    /**
     * 设置当聚合在文档中发现缺失值时使用的值，如：document为4个student信息，需要avg
     * student的score分数，但是某个学生没有分数这个字段，则missing则就是为缺失分数的学生设置默认值
     *
     * @param missing 设置缺省值的内容
     * @return AB
     */
    @SuppressWarnings("unchecked")
    public AB missing(Object missing) {
        if (missing == null) {
            throw new IllegalArgumentException("[missing] must not be null: [" + name + "]");
        }
        this.missing = missing;
        return (AB) this;
    }

    /**
     * 获取当聚合在文档中发现缺失值时要使用的值
     *
     * @return Object
     */
    public Object missing() {
        return missing;
    }

    /**
     * 设置用于此聚合输出的时区
     *
     * @param timeZone 时区信息
     * @return AB
     */
    @SuppressWarnings("unchecked")
    public AB timeZone(Object timeZone) {
        if (timeZone == null) {
            throw new IllegalArgumentException("[timeZone] must not be null: [" + name + "]");
        }
        this.timeZone = (DateTimeZone) timeZone;
        return (AB) this;
    }

    /**
     * 获取此聚合输出的时区
     *
     * @return TimeZone
     */
    public DateTimeZone timeZone() {
        return timeZone;
    }

    @Override
    public final XContentBuilder internalXContent(XContentBuilder builder) throws IOException {
        builder.startObject();
        if (field != null) {
            builder.field("field", field);
        }
        if (script != null) {
            builder.field("script", script);
        }
        if (missing != null) {
            builder.field("missing", missing);
        }
        if (format != null) {
            builder.field("format", format);
        }
        if (timeZone != null) {
            builder.field("time_zone", timeZone);
        }
        doXContentBody(builder);
        builder.endObject();
        return builder;
    }

    protected abstract XContentBuilder doXContentBody(XContentBuilder builder) throws IOException;

}
