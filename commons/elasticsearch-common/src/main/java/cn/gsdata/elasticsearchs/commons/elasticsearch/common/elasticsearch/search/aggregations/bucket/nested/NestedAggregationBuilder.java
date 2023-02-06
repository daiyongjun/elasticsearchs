package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.nested;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AbstractAggregationBuilder;

import java.io.IOException;

/**
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-bucket-nested-aggregation.html
 * 官网文档对NestedAggregation 定义是一个嵌套聚合，什么嵌套聚合，就是对于嵌套类型进行聚合如
 * 某个商品又一个经销商字段（resellers）该字段就是一个嵌套类型（nested）如下：
 * {
 * ...
 * "product" : {
 * "properties" : {
 * "resellers" : {
 * "type" : "nested",
 * "properties" : {
 * "name" : { "type" : "string" },
 * "price" : { "type" : "double" }
 * }
 * }
 * }
 * }
 * }
 * <p>
 * {
 * "query" : {
 * "match" : { "name" : "led tv" }
 * },
 * "aggs" : {
 * "resellers" : {
 * "nested" : {
 * "path" : "resellers"
 * },
 * "aggs" : {
 * "min_price" : { "min" : { "field" : "resellers.price" } }
 * }
 * }
 * }
 * }
 *
 * @author daiyongjun
 */
public class NestedAggregationBuilder extends AbstractAggregationBuilder<NestedAggregationBuilder> {
    static final ParseField PATH_FIELD = new ParseField("path");

    public static final String NAME = "nested";
    private final String path;

    /**
     * 构造一个新的聚合构建器。
     *
     * @param name 当前聚合的名称
     */
    public NestedAggregationBuilder(String name, String path) {
        super(name);
        if (path == null) {
            throw new IllegalArgumentException("[path] must not be null: [" + name + "]");
        }
        this.path = path;
    }

    @Override
    protected XContentBuilder internalXContent(XContentBuilder builder) throws IOException {
        builder.startObject();
        builder.field(PATH_FIELD.getPreferredName(), path);
        builder.endObject();
        return builder;
    }

    public static NestedAggregationBuilder parse(String aggregationName, XContentParser parser) throws IOException {
        String path = null;
        XContentParser.Token token;
        String currentFieldName = null;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.VALUE_STRING) {
                if (PATH_FIELD.match(currentFieldName)) {
                    path = parser.text();
                } else {
                    throw new ParsingException(parser.getTokenLocation(),
                            "Unknown key for a " + token + " in [" + aggregationName + "]: [" + currentFieldName + "].");
                }
            } else {
                throw new ParsingException(parser.getTokenLocation(), "Unexpected token " + token + " in [" + aggregationName + "].");
            }
        }
        if (path == null) {
            // "field" doesn't exist, so we fall back to the context of the ancestors
            throw new ParsingException(parser.getTokenLocation(), "Missing [path] field for nested aggregation [" + aggregationName + "]");
        }
        return new NestedAggregationBuilder(aggregationName, path);
    }


    @Override
    public String getType() {
        return NAME;
    }
}
