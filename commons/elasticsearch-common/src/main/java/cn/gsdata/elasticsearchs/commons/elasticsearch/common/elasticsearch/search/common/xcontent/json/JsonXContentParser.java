package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.json;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.NamedXContentRegistry;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentLocation;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentType;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.AbstractXContentParser;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;

/**
 * XContent内容Parser解析器,使用JsonParser将XContent（String）解析成Object
 *
 * @author daiyongjun
 */
public class JsonXContentParser extends AbstractXContentParser {
    /**
     * Jackson-core核心包JsonParser
     */
    final JsonParser parser;

    /**
     * 正文字段解析注册器
     */
    private final NamedXContentRegistry xContentRegistry;


    public JsonXContentParser(NamedXContentRegistry xContentRegistry, JsonParser parser) {
        this.xContentRegistry = xContentRegistry;
        this.parser = parser;
    }

    @Override
    public XContentType contentType() {
        return XContentType.JSON;
    }

    @Override
    public Token currentToken() {
        return convertToken(parser.getCurrentToken());
    }

    @Override
    public Token nextToken() throws IOException {
        return convertToken(parser.nextToken());
    }

    /**
     * 将Jackson-core的parser解析XContent内容的token转换为XContentParser定义的Token
     *
     * @param token Jackson-core的parser解析XContent内容的token
     * @return Token XContentParser定义的Token
     */
    private Token convertToken(JsonToken token) {
        if (token == null) {
            return null;
        }
        switch (token) {
            case FIELD_NAME:
                return Token.FIELD_NAME;
            case VALUE_FALSE:
            case VALUE_TRUE:
                return Token.VALUE_BOOLEAN;
            case VALUE_STRING:
                return Token.VALUE_STRING;
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT:
                return Token.VALUE_NUMBER;
            case VALUE_NULL:
                return Token.VALUE_NULL;
            case START_OBJECT:
                return Token.START_OBJECT;
            case END_OBJECT:
                return Token.END_OBJECT;
            case START_ARRAY:
                return Token.START_ARRAY;
            case END_ARRAY:
                return Token.END_ARRAY;
            case VALUE_EMBEDDED_OBJECT:
                return Token.VALUE_EMBEDDED_OBJECT;
            default:
                throw new IllegalStateException("No matching token for json_token [" + token + "]");
        }
    }


    @Override
    public XContentLocation getTokenLocation() {
        JsonLocation loc = parser.getTokenLocation();
        if (loc == null) {
            return null;
        }
        return new XContentLocation(loc.getLineNr(), loc.getColumnNr());
    }

    @Override
    public String currentName() throws IOException {
        return parser.getCurrentName();
    }



    @Override
    public NumberType numberType() throws IOException {
        return null;
    }

    @Override
    public Number numberValue() throws IOException {
        return parser.getNumberValue();
    }

    @Override
    protected long doLongValue() throws IOException {
        return parser.getLongValue();
    }

    @Override
    protected double doDoubleValue() throws IOException {
        return parser.getDoubleValue();
    }

    @Override
    public int intValue() throws IOException {
        Token token = currentToken();
        if (token == Token.VALUE_STRING) {
            double doubleValue = Double.parseDouble(text());
            if (doubleValue < Integer.MIN_VALUE || doubleValue > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Value [" + text() + "] is out of range for an integer");
            }
            return (int) doubleValue;
        }
        return parser.getIntValue();
    }

    @Override
    public long longValue() throws IOException {
        return parser.getLongValue();
    }

    @Override
    public float floatValue() throws IOException {
        Token token = currentToken();
        if (token == Token.VALUE_STRING) {
            return Float.parseFloat(text());
        }
        return parser.getFloatValue();
    }

    @Override
    public double doubleValue() throws IOException {
        return parser.getDoubleValue();
    }

    @Override
    public boolean booleanValue() throws IOException {
        return parser.getBooleanValue();
    }

    @Override
    public final String textOrNull() throws IOException {
        if (currentToken() == Token.VALUE_NULL) {
            return null;
        }
        return text();
    }

    @Override
    public String text() throws IOException {
        if (currentToken().isValue()) {
            return parser.getText();
        }
        throw new IllegalStateException("Can't get text on a " + currentToken() + " at " + getTokenLocation());
    }


    @Override
    public Object objectText() throws IOException {
        JsonToken currentToken = parser.getCurrentToken();
        if (currentToken == JsonToken.VALUE_STRING) {
            return text();
        } else if (currentToken == JsonToken.VALUE_NUMBER_INT || currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
            return parser.getNumberValue();
        } else if (currentToken == JsonToken.VALUE_TRUE) {
            return Boolean.TRUE;
        } else if (currentToken == JsonToken.VALUE_FALSE) {
            return Boolean.FALSE;
        } else if (currentToken == JsonToken.VALUE_NULL) {
            return null;
        } else {
            return text();
        }
    }

    @Override
    public boolean hasTextCharacters() {
        return false;
    }

    @Override
    public char[] textCharacters() throws IOException {
        return new char[0];
    }

    @Override
    public int textLength() throws IOException {
        return 0;
    }

    @Override
    public int textOffset() throws IOException {
        return 0;
    }

    @Override
    public <T> T namedObject(Class<T> categoryClass, String name, Object context) throws IOException {
        return xContentRegistry.parseNamedObject(categoryClass, name, this, context);
    }

    @Override
    public void skipChildren() throws IOException {
        parser.skipChildren();
    }

    @Override
    public void close() throws IOException {
        if (parser.isClosed()) {
            return;
        }
        parser.close();
    }

    @Override
    public byte[] binaryValue() throws IOException {
        return parser.getBinaryValue();
    }

}
