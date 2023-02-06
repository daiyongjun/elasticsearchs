package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.yaml;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.*;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 基于 YAML 的内容
 *
 * @author daiyongjun
 */
public class YamlXContent implements XContent {
    @Override
    public XContentType type() {
        return null;
    }

    @Override
    public byte streamSeparator() {
        return 0;
    }

    @Override
    public XContentParser createParser(NamedXContentRegistry xContentRegistry, String content) throws IOException {
        return null;
    }

    @Override
    public XContentGenerator createGenerator(OutputStream os) throws IOException {
        return null;
    }
}
