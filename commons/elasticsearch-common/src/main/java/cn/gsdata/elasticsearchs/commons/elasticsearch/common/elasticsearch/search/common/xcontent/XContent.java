package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent;

import java.io.IOException;
import java.io.OutputStream;

/**
 * XContent,封装的正文内容：内含有解析器，生成器，分隔符，正文内容的格式
 *
 * @author daiyongjun
 */
public interface XContent {

    /**
     * 内容类型
     *
     * @return XContentType
     */
    XContentType type();

    /**
     * 设置多个数据流分隔符
     *
     * @return byte
     */
    byte streamSeparator();

    /**
     * 使用指定正文注册器和内容，XContent的内容解析器
     *
     * @param xContentRegistry 正文注册器
     * @param content          正文内容
     * @return XContentParser
     * @throws IOException 正文解析异常
     */
    XContentParser createParser(NamedXContentRegistry xContentRegistry, String content) throws IOException;

    /**
     * 使用输出流，生成XContent的生成器
     *
     * @param os 输出流
     * @return XContentGenerator
     * @throws IOException 异常信息
     */
    XContentGenerator createGenerator(OutputStream os) throws IOException;
}

