package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.json;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentGenerator;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentHelper;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;

import java.io.IOException;
import java.io.OutputStream;

/**
 * JsonXContent生成器：使用JsonGenerator将Object转换成XContent（String）
 *
 * @author daiyongjun
 */
public class JsonXContentGenerator implements XContentGenerator {

    /**
     * generator创建 JsonGenerator jg = jsonFactory.createGenerator(byteArrayOutputStream, JsonEncoding.UTF8);
     */
    private final OutputStream os;

    /**
     * JsonXContent生成器核心解析类
     */
    protected final JsonGenerator generator;


    public JsonXContentGenerator(JsonGenerator jsonGenerator, OutputStream os) {
        this.os = os;
        this.generator = jsonGenerator;
    }

    @Override
    public XContentType contentType() {
        return XContentType.JSON;
    }

    @Override
    public void writeStartArray() throws IOException {
        generator.writeStartArray();
    }

    @Override
    public void writeEndArray() throws IOException {
        generator.writeEndArray();
    }


    @Override
    public void writeStartObject() throws IOException {
        generator.writeStartObject();
    }

    @Override
    public void writeEndObject() throws IOException {
        generator.writeEndObject();
    }

    @Override
    public void writeFieldName(String name) throws IOException {
        generator.writeFieldName(name);
    }

    @Override
    public void writeNull() throws IOException {
        generator.writeNull();
    }

    @Override
    public void writeNullField(String name) throws IOException {
        generator.writeNullField(name);
    }

    @Override
    public void writeNumber(int value) throws IOException {
        generator.writeNumber(value);
    }

    @Override
    public void writeNumber(float value) throws IOException {
        generator.writeNumber(value);
    }

    @Override
    public void writeNumber(Double value) throws IOException {
        generator.writeNumber(value);
    }

    @Override
    public void writeBoolean(boolean value) throws IOException {
        generator.writeBoolean(value);
    }

    @Override
    public void writeBinary(byte[] value) throws IOException {
        generator.writeBinary(value);
    }

    @Override
    public void copyCurrentStructure(XContentParser parser) throws IOException {
        if (parser.currentToken() == null) {
            parser.nextToken();
        }
        if (parser instanceof JsonXContentParser) {
            generator.copyCurrentStructure(((JsonXContentParser) parser).parser);
        } else {
            XContentHelper.copyCurrentStructure(this, parser);
        }
    }

    @Override
    public void writeStringField(String name, String value) throws IOException {
        generator.writeStringField(name, value);
    }


    @Override
    public void writeString(String value) throws IOException {
        generator.writeString(value);
    }

    @Override
    public void writeString(char[] value, int offset, int len) throws IOException {
        generator.writeString(value, offset, len);
    }


    @Override
    public void close() throws IOException {
        if (generator.isClosed()) {
            return;
        }
        JsonStreamContext context = generator.getOutputContext();
        if ((context != null) && (!context.inRoot())) {
            throw new IOException("Unclosed object or array found");
        }
        generator.close();
    }

}
