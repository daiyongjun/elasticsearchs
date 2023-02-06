package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.Numbers;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.ElasticsearchException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * XContentParser的抽象类
 *
 * @author daiyongjun
 */
public abstract class AbstractXContentParser implements XContentParser {
    static final MapFactory SIMPLE_MAP_FACTORY = HashMap::new;
    static final MapStringsFactory SIMPLE_MAP_STRINGS_FACTORY = HashMap::new;

    @Override
    public long longValue(boolean coerce) throws IOException {
        Token token = currentToken();
        if (token == Token.VALUE_STRING) {
            checkCoerceString(coerce, Long.class);
            return Numbers.toLong(text(), coerce);
        }
        long result = doLongValue();
        ensureNumberConversion(coerce, result, Long.class);
        return result;
    }

    protected abstract long doLongValue() throws IOException;

    private static void checkCoerceString(boolean coerce, Class<? extends Number> clazz) {
        if (!coerce) {
            //Need to throw type IllegalArgumentException as current catch logic in
            //NumberFieldMapper.parseCreateField relies on this for "malformed" value detection
            throw new IllegalArgumentException(clazz.getSimpleName() + " value passed as String");
        }
    }

    void ensureNumberConversion(boolean coerce, long result, Class<? extends Number> clazz) throws IOException {
        if (!coerce) {
            double fullVal = doDoubleValue();
            if (result != fullVal) {
                // Need to throw type IllegalArgumentException as current catch
                // logic in NumberFieldMapper.parseCreateField relies on this
                // for "malformed" value detection
                throw new IllegalArgumentException(fullVal + " cannot be converted to " + clazz.getSimpleName() + " without data loss");
            }
        }
    }

    @Override
    public Map<String, String> mapStrings() throws IOException {
        return readMapStrings(this);
    }

    static Map<String, String> readMapStrings(XContentParser parser) throws IOException {
        return readMapStrings(parser, SIMPLE_MAP_STRINGS_FACTORY);
    }

    static Map<String, String> readMapStrings(XContentParser parser, MapStringsFactory mapStringsFactory) throws IOException {
        Map<String, String> map = mapStringsFactory.newMap();
        XContentParser.Token token = parser.currentToken();
        if (token == null) {
            token = parser.nextToken();
        }
        if (token == XContentParser.Token.START_OBJECT) {
            token = parser.nextToken();
        }
        for (; token == XContentParser.Token.FIELD_NAME; token = parser.nextToken()) {
            // Must point to field name
            String fieldName = parser.currentName();
            // And then the value...
            parser.nextToken();
            String value = parser.text();
            map.put(fieldName, value);
        }
        return map;
    }


    @Override
    public Map<String, Object> map() throws IOException {
        return readMap(this);
    }


    static Map<String, Object> readMap(XContentParser parser) throws IOException {
        return readMap(parser, SIMPLE_MAP_FACTORY);
    }

    static Map<String, Object> readMap(XContentParser parser, MapFactory mapFactory) throws IOException {
        Map<String, Object> map = mapFactory.newMap();
        XContentParser.Token token = parser.currentToken();
        if (token == null) {
            token = parser.nextToken();
        }
        if (token == XContentParser.Token.START_OBJECT) {
            token = parser.nextToken();
        }
        for (; token == XContentParser.Token.FIELD_NAME; token = parser.nextToken()) {
            // Must point to field name
            String fieldName = parser.currentName();
            // And then the value...
            token = parser.nextToken();
            Object value = readValue(parser, mapFactory, token);
            map.put(fieldName, value);
        }
        return map;
    }


    static Object readValue(XContentParser parser, MapFactory mapFactory, XContentParser.Token token) throws IOException {
        if (token == XContentParser.Token.VALUE_NULL) {
            return null;
        } else if (token == XContentParser.Token.VALUE_STRING) {
            return parser.text();
        } else if (token == XContentParser.Token.VALUE_NUMBER) {
            return parser.numberValue();
        } else if (token == XContentParser.Token.VALUE_BOOLEAN) {
            return parser.booleanValue();
        } else if (token == XContentParser.Token.START_OBJECT) {
            return readMap(parser, mapFactory);
        } else if (token == XContentParser.Token.START_ARRAY) {
            return readList(parser, mapFactory);
        } else if (token == XContentParser.Token.VALUE_EMBEDDED_OBJECT) {
            return parser.binaryValue();
        }
        return null;
    }

    static List<Object> readList(XContentParser parser, MapFactory mapFactory) throws IOException {
        XContentParser.Token token = parser.currentToken();
        if (token == null) {
            token = parser.nextToken();
        }
        if (token == XContentParser.Token.FIELD_NAME) {
            token = parser.nextToken();
        }
        if (token == XContentParser.Token.START_ARRAY) {
            token = parser.nextToken();
        } else {
            throw new ElasticsearchException("Failed to parse list:  expecting "
                    + XContentParser.Token.START_ARRAY + " but got " + token);
        }
        ArrayList<Object> list = new ArrayList<>();
        for (; token != null && token != XContentParser.Token.END_ARRAY; token = parser.nextToken()) {
            list.add(readValue(parser, mapFactory, token));
        }
        return list;
    }

    protected abstract double doDoubleValue() throws IOException;

    /**
     *
     */
    interface MapFactory {
        /**
         * @return Map<String, Object>
         */
        Map<String, Object> newMap();
    }

    /**
     *
     */
    interface MapStringsFactory {
        /**
         * @return Map<String, String>
         */
        Map<String, String> newMap();
    }
}
