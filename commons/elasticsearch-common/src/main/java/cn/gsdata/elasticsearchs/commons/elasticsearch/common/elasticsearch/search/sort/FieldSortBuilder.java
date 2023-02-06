package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.sort;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.AbstractObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query.QueryBuilder;

import java.io.IOException;
import java.util.Objects;

/**
 * 基于字段(Field)排序
 *
 * @author daiyongjun
 */
public class FieldSortBuilder extends SortBuilder<FieldSortBuilder> {
    public static final String NAME = "field_sort";
    public static final ParseField MISSING = new ParseField("missing");
    public static final ParseField SORT_MODE = new ParseField("mode");
    public static final ParseField UNMAPPED_TYPE = new ParseField("unmapped_type");
    /**
     * 基于字段(Field) 排序的名称
     */
    private final String fieldName;
    /**
     * elastic-search存在一种字段叫做缺失字段，missing字段定义缺失字段的排序值，默认值为_last
     * 该missing值可以设置为_last、_first或自定义值
     */
    private Object missing;
    /**
     * 容错不存在的字段，elastic-search有一种场景，就是之前不存在这个字段，后面增加了一个新的字段，如
     * 年纪(age)  int 类型，此时我们要对age 进行排序则可以设置
     * "sort" : [
     * { "age" : {"unmapped_type" : "int"} }
     * ]
     */
    private String unmappedType;
    /**
     * 多值字段的排序模式 multiple prices per documen
     * {
     * "product": "chocolate",
     * "price": [20, 4]
     * }
     * {
     * "sort" : [
     * {"price" : {"order" : "asc", "mode" : "avg"}}
     * ]
     * }
     */
    private SortMode sortMode;

    /**
     * 6.1版本以前的嵌套排序规则，无需内置 nested 关键词如：sort:{"path":xxxxx,"nestedFilter":{query语法}}
     */
    private QueryBuilder nestedFilter;
    /**
     * 6.1版本以前的嵌套排序规则，无需内置 nested 关键词如：sort:{"path":xxxxx,"nestedPath":{query语法}}
     */
    private String nestedPath;
    /**
     * 6.1版本以前的嵌套排序规则，嵌套对象内的字段进行排序
     */
    private NestedSortBuilder nestedSort;

    /**
     * @param fieldName 字段名称
     */
    public FieldSortBuilder(String fieldName) {
        if (fieldName == null) {
            throw new IllegalArgumentException("fieldName must not be null");
        }
        this.fieldName = fieldName;
    }

    /**
     * 设置缺失字段的排序值
     *
     * @param missing Object
     * @return FieldSortBuilder
     */
    public FieldSortBuilder missing(Object missing) {
        this.missing = missing;
        return this;
    }

    /**
     * 设置容错不存在的字段
     *
     * @param type 不存在的字段
     * @return FieldSortBuilder
     */
    public FieldSortBuilder unmappedType(String type) {
        this.unmappedType = type;
        return this;
    }

    /**
     * 设置待排序的嵌套对象的路径
     *
     * @param nestedPath String
     * @return FieldSortBuilder
     */
    public FieldSortBuilder setNestedPath(String nestedPath) {
        if (this.nestedSort != null) {
            throw new IllegalArgumentException("Setting both nested_path/nested_filter and nested not allowed");
        }
        this.nestedPath = nestedPath;
        return this;
    }

    /**
     * 设置多值字段的排序模式
     *
     * @param sortMode SortMode
     * @return FieldSortBuilder
     */
    public FieldSortBuilder sortMode(SortMode sortMode) {
        Objects.requireNonNull(sortMode, "sort mode cannot be null");
        this.sortMode = sortMode;
        return this;
    }

    /**
     * 设置嵌套字段的过滤条件，这个排序规则是和query的nestedFilter绑定的
     *
     * @param nestedFilter 嵌套语法的过滤器
     * @return FieldSortBuilder
     */
    public FieldSortBuilder setNestedFilter(QueryBuilder nestedFilter) {
        if (this.nestedSort != null) {
            throw new IllegalArgumentException("Setting both nested_path/nested_filter and nested not allowed");
        }
        this.nestedFilter = nestedFilter;
        return this;
    }

    /**
     * 设置基于嵌套查询的过滤查询的字段排序，如果我们基于嵌套字段查询，并设置过滤条件，则排序使用当前嵌套字段
     * 必须要带上这个过滤条件
     *
     * @param nestedSort NestedSortBuilder
     * @return FieldSortBuilder
     */
    public FieldSortBuilder setNestedSort(final NestedSortBuilder nestedSort) {
        if (this.nestedFilter != null || this.nestedPath != null) {
            throw new IllegalArgumentException("Setting both nested_path/nested_filter and nested not allowed");
        }
        this.nestedSort = nestedSort;
        return this;
    }

    public static FieldSortBuilder fromXContent(XContentParser parser, String fieldName) throws IOException {
        return PARSER.parse(parser, null, new FieldSortBuilder(fieldName));
    }

    private static ObjectParser<Void, FieldSortBuilder> PARSER = new ObjectParser<>(NAME);

    static {
        PARSER.declareField(FieldSortBuilder::missing, XContentParser::objectText, MISSING, AbstractObjectParser.ValueType.VALUE);
        PARSER.declareString(FieldSortBuilder::setNestedPath, NESTED_PATH_FIELD);
        PARSER.declareString(FieldSortBuilder::unmappedType, UNMAPPED_TYPE);
        PARSER.declareString((b, v) -> b.order(SortOrder.fromString(v)), ORDER_FIELD);
        PARSER.declareString((b, v) -> b.sortMode(SortMode.fromString(v)), SORT_MODE);
        PARSER.declareObject(FieldSortBuilder::setNestedFilter, (p, c) -> {
            return parseNestedFilter(p,"");
        }, NESTED_FILTER_FIELD);
        PARSER.declareObject(FieldSortBuilder::setNestedSort, (p, c) -> NestedSortBuilder.fromXContent(p), NestedSortBuilder.NESTED_FIELD);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        builder.startObject();
        builder.startObject(fieldName);
        builder.field(ORDER_FIELD.getPreferredName(), order);
        if (missing != null) {
            builder.field(MISSING.getPreferredName(), missing);
        }
        if (unmappedType != null) {
            builder.field(UNMAPPED_TYPE.getPreferredName(), unmappedType);
        }
        if (sortMode != null) {
            builder.field(SORT_MODE.getPreferredName(), sortMode);
        }
        if (nestedFilter != null) {
            builder.field(NESTED_FILTER_FIELD.getPreferredName(), nestedFilter);
        }
        if (nestedPath != null) {
            builder.field(NESTED_PATH_FIELD.getPreferredName(), nestedPath);
        }
        if (nestedSort != null) {
            builder.field(NestedSortBuilder.NESTED_FIELD.getPreferredName(), nestedSort);
        }
        builder.endObject();
        builder.endObject();
        return builder;
    }
}
