package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent;

import java.io.IOException;

/**
 * XContent的协助类
 *
 * @author daiyongjun
 */
public class XContentHelper {
    /**
     * 从当前XContentGenerator 复制到XContentGenerator destination中
     *
     * @param destination XContentGenerator
     * @param parser      XContentParser
     * @throws IOException 复制过程异常
     */
    public static void copyCurrentStructure(XContentGenerator destination, XContentParser parser) throws IOException {
        XContentParser.Token token = parser.currentToken();
        if (token == XContentParser.Token.FIELD_NAME) {
            destination.writeFieldName(parser.currentName());
            token = parser.nextToken();
        }
        switch (token) {
            case START_ARRAY:
                destination.writeStartArray();
                while (parser.nextToken() != XContentParser.Token.END_ARRAY) {
                    copyCurrentStructure(destination, parser);
                }
                destination.writeEndArray();
                break;
            case START_OBJECT:
                destination.writeStartObject();
                while (parser.nextToken() != XContentParser.Token.END_OBJECT) {
                    copyCurrentStructure(destination, parser);
                }
                destination.writeEndObject();
                break;
            default:
                copyCurrentEvent(destination, parser);
        }
    }

    /**
     * 基于定制的Token进行复制到XContentGenerator generator
     *
     * @param generator XContentGenerator
     * @param parser    XContentParser
     * @throws IOException 复制异常
     */
    public static void copyCurrentEvent(XContentGenerator generator, XContentParser parser) throws IOException {
        switch (parser.currentToken()) {
            case START_OBJECT:
                generator.writeStartObject();
                break;
            case END_OBJECT:
                generator.writeEndObject();
                break;
            case START_ARRAY:
                generator.writeStartArray();
                break;
            case END_ARRAY:
                generator.writeEndArray();
                break;
            case FIELD_NAME:
                generator.writeFieldName(parser.currentName());
                break;
            case VALUE_STRING:
                if (parser.hasTextCharacters()) {
                    generator.writeString(parser.textCharacters(), parser.textOffset(), parser.textLength());
                } else {
                    generator.writeString(parser.text());
                }
                break;
            case VALUE_NUMBER:
                switch (parser.numberType()) {
                    case INT:
                        generator.writeNumber(parser.intValue());
                        break;
                    case LONG:
                        generator.writeNumber(parser.longValue());
                        break;
                    case FLOAT:
                        generator.writeNumber(parser.floatValue());
                        break;
                    case DOUBLE:
                        generator.writeNumber(parser.doubleValue());
                        break;
                    default:
                        break;
                }
                break;
            case VALUE_BOOLEAN:
                generator.writeBoolean(parser.booleanValue());
                break;
            case VALUE_NULL:
                generator.writeNull();
                break;
            case VALUE_EMBEDDED_OBJECT:
                generator.writeBinary(parser.binaryValue());
            default:
                break;
        }
    }
}
