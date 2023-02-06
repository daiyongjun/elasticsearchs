package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.sort;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ToXContent;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query.QueryBuilder;

import java.io.IOException;

/**
 * Elasticsearch 还支持按一个或多个嵌套对象内的字段进行排序
 *
 * @author daiyongjun
 */
public class NestedSortBuilder implements ToXContent {
    public static final ParseField NESTED_FIELD = new ParseField("nested");
    public static final ParseField PATH_FIELD = new ParseField("path");
    public static final ParseField FILTER_FIELD = new ParseField("filter");
    /**
     * 定义要排序的嵌套对象，实际的排序字段必须是此嵌套对象内的直接字段（类似于定义文件复制的文件路径；该值是定义嵌套对象）
     */
    private final String path;
    /**
     * 嵌套对象的过滤器，默认情况下一般是不用嵌套过滤器，使用时注意查询和排序都需要写相同的嵌套条件
     */
    private QueryBuilder filter;
    /**
     * 嵌套嵌套排序
     */
    private NestedSortBuilder nestedSort;

    public NestedSortBuilder(String path) {
        this.path = path;
    }

    public NestedSortBuilder setFilter(final QueryBuilder filter) {
        this.filter = filter;
        return this;
    }

    public NestedSortBuilder setNestedSort(final NestedSortBuilder nestedSortBuilder) {
        this.nestedSort = nestedSortBuilder;
        return this;
    }

    public static NestedSortBuilder fromXContent(XContentParser parser) throws IOException {
        String path = null;
        QueryBuilder filter = null;
        NestedSortBuilder nestedSort = null;

        XContentParser.Token token = parser.currentToken();
        if (token == XContentParser.Token.START_OBJECT) {
            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    String currentName = parser.currentName();
                    parser.nextToken();
                    if (currentName.equals(PATH_FIELD.getPreferredName())) {
                        path = parser.text();
                    } else if (currentName.equals(FILTER_FIELD.getPreferredName())) {
                        filter = SortBuilder.parseNestedFilter(parser,"");
                    } else if (currentName.equals(NESTED_FIELD.getPreferredName())) {
                        nestedSort = NestedSortBuilder.fromXContent(parser);
                    } else {
                        throw new IllegalArgumentException("malformed nested sort format, unknown field name [" + currentName + "]");
                    }
                } else {
                    throw new IllegalArgumentException("malformed nested sort format, only field names are allowed");
                }
            }
        } else {
            throw new IllegalArgumentException("malformed nested sort format, must start with an object");
        }
        return new NestedSortBuilder(path).setFilter(filter).setNestedSort(nestedSort);
    }


    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        builder.startObject();
        if (path != null) {
            builder.field(PATH_FIELD.getPreferredName(), path);
        }
        if (filter != null) {
            builder.field(FILTER_FIELD.getPreferredName(), filter);
        }
        if (nestedSort != null) {
            builder.field(NESTED_FIELD.getPreferredName(), nestedSort);
        }
        builder.endObject();
        return builder;
    }
}
