package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.json;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.*;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;


/**
 * ES定义的抽象正文内容XContent的实现类；Json格式的抽象正文内容
 * 使用jackson.core的核心类，JsonParser和JsonGenerator来解析JSON和生成JSON
 *
 * @author daiyongjun
 */
public class JsonXContent implements XContent {
    /**
     * Jackson-core核心包，进行JSON -> POJO的核心处理JsonFactory工厂类
     */
    private static final JsonFactory JSON_FACTORY;

    public static final JsonXContent JSON_X_CONTENT;

    static {
        //配置JsonFactory的基础对象
        JSON_FACTORY = new JsonFactory();
        JSON_FACTORY.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);
        JSON_FACTORY.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        JSON_FACTORY.configure(JsonFactory.Feature.FAIL_ON_SYMBOL_HASH_OVERFLOW, false);
        JSON_FACTORY.configure(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, false);
        JSON_FACTORY.configure(JsonParser.Feature.STRICT_DUPLICATE_DETECTION, true);
        // 初始化对象
        JSON_X_CONTENT = new JsonXContent();
    }

    /**
     * 设置XContent的类型
     *
     * @return XContentType
     */
    @Override
    public XContentType type() {
        return XContentType.JSON;
    }

    /**
     * 设置多个Json格式的分隔符
     *
     * @return byte
     */
    @Override
    public byte streamSeparator() {
        return '\n';
    }


    @Override
    public XContentParser createParser(NamedXContentRegistry xContentRegistry, String content) throws IOException {
        return new JsonXContentParser(xContentRegistry, JSON_FACTORY.createParser(new StringReader(content)));
    }

    @Override
    public XContentGenerator createGenerator(OutputStream os) throws IOException {
        return new JsonXContentGenerator(JSON_FACTORY.createGenerator(os, JsonEncoding.UTF8), os);
    }

    /**
     * XContentBuilder创建拼接类，XContentBuilder用户拼接XContent内容类
     *
     * @return XContentBuilder
     * @throws IOException XContent构建类创建异常
     */
    public static XContentBuilder contentBuilder() throws IOException {
        return XContentBuilder.builder(JSON_X_CONTENT);
    }

}
