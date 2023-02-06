package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.cbor;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.*;
import java.io.IOException;
import java.io.OutputStream;


/**
 * 基于 CBOR 的内容
 *
 * @author daiyongjun
 */
public class CborXContent implements XContent {
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
