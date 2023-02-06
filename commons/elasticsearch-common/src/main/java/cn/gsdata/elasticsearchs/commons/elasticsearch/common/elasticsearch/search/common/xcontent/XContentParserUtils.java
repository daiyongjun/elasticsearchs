package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent;


import java.util.Locale;
import java.util.function.Supplier;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;

/**
 * @author daiyongjun
 */
public final class XContentParserUtils {

    public static void ensureExpectedToken(XContentParser.Token expected, XContentParser.Token actual, Supplier<XContentLocation> location) {
        if (actual != expected) {
            String message = "Failed to parse object: expecting token of type [%s] but found [%s]";
            throw new ParsingException(location.get(), String.format(Locale.ROOT, message, expected, actual));
        }
    }
}
