package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.range;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ToXContent;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;

import java.io.IOException;
import java.util.Locale;

/**
 * Range, AbstractRangeBuilder内置属性 List<Range> ranges
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-bucket-range-aggregation.html#search-aggregations-bucket-range-aggregation
 *
 * @author daiyongjun
 */
public class RangeBuilder implements ToXContent {
    public static final ParseField KEY_FIELD = new ParseField("key");
    public static final ParseField FROM_FIELD = new ParseField("from");
    public static final ParseField TO_FIELD = new ParseField("to");
    /**
     * ranges : [{ "key": "average", "from": 100, "to": 200 },...]
     * key : 自定义键的值from : 范围的启始值、to ： 范围的结束值
     * fromAsStr ： 范围的起始值（字符串类型） 、toAsStr ：范围的结束值（字符串类型）
     */
    protected final String key;
    protected final double from;
    protected final String fromAsStr;
    protected final double to;
    protected final String toAsStr;

    public RangeBuilder(String key, Double from, String fromAsStr, Double to, String toAsStr) {
        this.key = key;
        this.from = from == null ? Double.NEGATIVE_INFINITY : from;
        this.fromAsStr = fromAsStr;
        this.to = to == null ? Double.POSITIVE_INFINITY : to;
        this.toAsStr = toAsStr;
    }

    public static RangeBuilder fromXContent(XContentParser parser) throws IOException {
        XContentParser.Token token;
        String currentFieldName = null;
        double from = Double.NEGATIVE_INFINITY;
        String fromAsStr = null;
        double to = Double.POSITIVE_INFINITY;
        String toAsStr = null;
        String key = null;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.VALUE_NUMBER) {
                if (FROM_FIELD.match(currentFieldName)) {
                    from = parser.doubleValue();
                } else if (TO_FIELD.match(currentFieldName)) {
                    to = parser.doubleValue();
                } else {
                    String message = "Failed to parse object: unknown field [%s] found";
                    throw new ParsingException(parser.getTokenLocation(), String.format(Locale.ROOT, message, currentFieldName));
                }
            } else if (token == XContentParser.Token.VALUE_STRING) {
                if (FROM_FIELD.match(currentFieldName)) {
                    fromAsStr = parser.text();
                } else if (TO_FIELD.match(currentFieldName)) {
                    toAsStr = parser.text();
                } else if (KEY_FIELD.match(currentFieldName)) {
                    key = parser.text();
                } else {
                    String message = "Failed to parse object: unknown field [%s] found";
                    throw new ParsingException(parser.getTokenLocation(), String.format(Locale.ROOT, message, currentFieldName));
                }
            } else if (token == XContentParser.Token.VALUE_NULL) {
                if (FROM_FIELD.match(currentFieldName) || TO_FIELD.match(currentFieldName)
                        || KEY_FIELD.match(currentFieldName)) {
                    // ignore null value
                } else {
                    String message = "Failed to parse object: unknown field [%s] found";
                    throw new ParsingException(parser.getTokenLocation(), String.format(Locale.ROOT, message, currentFieldName));
                }
            } else {
                String message = "Failed to parse object: unknown field [%s] found";
                throw new ParsingException(parser.getTokenLocation(), String.format(Locale.ROOT, message, currentFieldName));
            }
        }
        return new RangeBuilder(key, from, fromAsStr, to, toAsStr);
    }


    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        builder.startObject();
        if (key != null) {
            builder.field(KEY_FIELD.getPreferredName(), key);
        }
        if (Double.isFinite(from)) {
            builder.field(FROM_FIELD.getPreferredName(), from);
        }
        if (Double.isFinite(to)) {
            builder.field(TO_FIELD.getPreferredName(), to);
        }
        if (fromAsStr != null) {
            builder.field(FROM_FIELD.getPreferredName(), fromAsStr);
        }
        if (toAsStr != null) {
            builder.field(TO_FIELD.getPreferredName(), toAsStr);
        }
        builder.endObject();
        return builder;
    }
}
