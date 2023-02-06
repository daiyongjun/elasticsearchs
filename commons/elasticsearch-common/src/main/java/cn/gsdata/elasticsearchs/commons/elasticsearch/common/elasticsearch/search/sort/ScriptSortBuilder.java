package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.sort;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.AbstractObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ConstructingObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query.QueryBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.script.Script;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

/**
 * 基于脚本(Script)排序
 *
 * @author daiyongjun
 */
public class ScriptSortBuilder extends SortBuilder<ScriptSortBuilder> {
    public static final String NAME = "_script";

    public static final ParseField TYPE_FIELD = new ParseField("type");
    public static final ParseField SCRIPT_FIELD = new ParseField("script");
    public static final ParseField SORTMODE_FIELD = new ParseField("mode");

    /**
     * 排序的脚本内容
     */
    private final Script script;
    /**
     * 脚本return返回的类型，常见有number、string类型等
     */
    private final ScriptSortType type;
    /**
     * 支持按数组或多值字段排序Order by A,B,C的排序模式
     */
    private SortMode sortMode;
    /**
     * 6.1版本以前的嵌套排序规则，6.x版本以后支持内置 nested 关键词如：sort:{"path":xxxxx,"nestedFilter":{query语法}}
     * path: 嵌套排序字段的路径
     */
    private String nestedPath;
    /**
     * 6.1版本以前的嵌套排序规则，6.x版本以后支持内置 nested 关键词如：sort:{"path":xxxxx,"nestedFilter":{query语法}}
     * nestedSort: 嵌套排序字段的排序规则
     */
    private NestedSortBuilder nestedSort;
    /**
     * 6.1版本以前的嵌套排序规则，6.x版本以后支持内置 nested 关键词如：sort:{"path":xxxxx,"nestedFilter":{query语法}}
     */
    private QueryBuilder nestedFilter;


    public ScriptSortBuilder(Script script, ScriptSortType type) {
        Objects.requireNonNull(script, "script cannot be null");
        Objects.requireNonNull(type, "type cannot be null");
        this.script = script;
        this.type = type;
    }

    /**
     * 设置  SortMode 字段的值
     *
     * @param sortMode SortMode 多值排序的排序模式
     * @return ScriptSortBuilder
     */
    public ScriptSortBuilder sortMode(SortMode sortMode) {
        Objects.requireNonNull(sortMode, "sort mode cannot be null.");
        if (ScriptSortType.STRING.equals(type) && (sortMode == SortMode.SUM || sortMode == SortMode.AVG ||
                sortMode == SortMode.MEDIAN)) {
            throw new IllegalArgumentException("script sort of type [string] doesn't support mode [" + sortMode + "]");
        }
        this.sortMode = sortMode;
        return this;
    }

    /**
     * 设置待弃用的nestedPath属性的值
     *
     * @param nestedPath String  嵌套路径
     * @return ScriptSortBuilder
     */
    @Deprecated
    public ScriptSortBuilder setNestedPath(String nestedPath) {
        if (this.nestedSort != null) {
            throw new IllegalArgumentException("Setting both nested_path/nested_filter and nested not allowed");
        }
        this.nestedPath = nestedPath;
        return this;
    }

    /**
     * 设置待弃用的nestedSort属性的值
     *
     * @param nestedSort NestedSortBuilder
     * @return ScriptSortBuilder
     */
    public ScriptSortBuilder setNestedSort(final NestedSortBuilder nestedSort) {
        if (this.nestedFilter != null || this.nestedPath != null) {
            throw new IllegalArgumentException("Setting both nested_path/nested_filter and nested not allowed");
        }
        this.nestedSort = nestedSort;
        return this;
    }

    /**
     * 设置待弃用的nestedFilter的值
     *
     * @param nestedFilter QueryBuilder
     * @return ScriptSortBuilder
     */
    @Deprecated
    public ScriptSortBuilder setNestedFilter(QueryBuilder nestedFilter) {
        if (this.nestedSort != null) {
            throw new IllegalArgumentException("Setting both nested_path/nested_filter and nested not allowed");
        }
        this.nestedFilter = nestedFilter;
        return this;
    }

    private static ConstructingObjectParser<Void, ScriptSortBuilder> PARSER = new ConstructingObjectParser<>(NAME,
            a -> new ScriptSortBuilder((Script) a[0], (ScriptSortType) a[1]));

    static {
        PARSER.declareField(ConstructingObjectParser.constructorArg(), (parser, context) -> Script.parse(parser), Script.SCRIPT_PARSE_FIELD, AbstractObjectParser.ValueType.OBJECT_OR_STRING);
        PARSER.declareField(ConstructingObjectParser.constructorArg(), p -> ScriptSortType.fromString(p.text()), TYPE_FIELD, AbstractObjectParser.ValueType.STRING);
        PARSER.declareString((b, v) -> b.order(SortOrder.fromString(v)), ORDER_FIELD);
        PARSER.declareString((b, v) -> b.sortMode(SortMode.fromString(v)), SORTMODE_FIELD);
        //
        PARSER.declareString(ScriptSortBuilder::setNestedPath, NESTED_PATH_FIELD);
        PARSER.declareObject(ScriptSortBuilder::setNestedFilter, (p, c) -> {
            return parseNestedFilter(p,"");
        }, NESTED_FILTER_FIELD);
        PARSER.declareObject(ScriptSortBuilder::setNestedSort, (p, c) -> NestedSortBuilder.fromXContent(p), NestedSortBuilder.NESTED_FIELD);
    }


    public static ScriptSortBuilder fromXContent(XContentParser parser, String elementName) {
        return PARSER.apply(parser, null);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        builder.startObject();
        builder.startObject(NAME);
        builder.field(SCRIPT_FIELD.getPreferredName(), script);
        builder.field(TYPE_FIELD.getPreferredName(), type);
        builder.field(ORDER_FIELD.getPreferredName(), order);
        if (sortMode != null) {
            builder.field(SORTMODE_FIELD.getPreferredName(), sortMode);
        }
        if (nestedPath != null) {
            builder.field(NESTED_PATH_FIELD.getPreferredName(), nestedPath);
        }
        if (nestedFilter != null) {
            builder.field(NESTED_FILTER_FIELD.getPreferredName(), nestedFilter);
        }
        if (nestedSort != null) {
            builder.field(NestedSortBuilder.NESTED_FIELD.getPreferredName(), nestedSort);
        }
        builder.endObject();
        builder.endObject();
        return builder;
    }

    /**
     * 基于脚本排序，定义的枚举类型，String类型和Number类型
     */
    public enum ScriptSortType {
        /**
         * 自定义的枚举类型
         */
        STRING,
        NUMBER;

        public static ScriptSortType fromString(final String str) {
            Objects.requireNonNull(str, "input string is null");
            switch (str.toLowerCase(Locale.ROOT)) {
                case ("string"):
                    return ScriptSortType.STRING;
                case ("number"):
                    return ScriptSortType.NUMBER;
                default:
                    throw new IllegalArgumentException("Unknown ScriptSortType [" + str + "]");
            }
        }
    }
}
