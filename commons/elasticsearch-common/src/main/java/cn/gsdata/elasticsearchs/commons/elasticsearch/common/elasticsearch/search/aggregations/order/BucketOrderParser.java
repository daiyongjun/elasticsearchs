package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.order;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;

import java.io.IOException;

public class BucketOrderParser {
    public static BucketOrder parse(XContentParser parser) throws IOException {
        XContentParser.Token token;
        String orderKey = null;
        boolean orderAsc = false;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                orderKey = parser.currentName();
            } else if (token == XContentParser.Token.VALUE_STRING) {
                String dir = parser.text();
                if ("asc".equalsIgnoreCase(dir)) {
                    orderAsc = true;
                } else if ("desc".equalsIgnoreCase(dir)) {
                    orderAsc = false;
                } else {
                    throw new ParsingException(parser.getTokenLocation(),
                            "Unknown order direction [" + dir + "]");
                }
            } else {
                throw new ParsingException(parser.getTokenLocation(),
                        "Unexpected token [" + token + "] for [order]");
            }
        }
        if (orderKey == null) {
            throw new ParsingException(parser.getTokenLocation(),
                    "Must specify at least one field for [order]");
        }
        // _term and _time order deprecated in 6.0; replaced by _key
        if ("_term".equals(orderKey) || "_time".equals(orderKey)) {
            throw new ParsingException(parser.getTokenLocation(), "Deprecated aggregation order key [{}] used, replaced by [_key]", orderKey);
        }
        switch (orderKey) {
            case "_term":
            case "_time":
            case "_key":
                return orderAsc ? InternalOrder.KEY_ASC : InternalOrder.KEY_DESC;
            case "_count":
                return orderAsc ? InternalOrder.COUNT_ASC : InternalOrder.COUNT_DESC;
            default: // assume all other orders are sorting on a sub-aggregation. Validation occurs later.
                return new AggregationOrder(orderKey, orderAsc);
        }
    }
}
