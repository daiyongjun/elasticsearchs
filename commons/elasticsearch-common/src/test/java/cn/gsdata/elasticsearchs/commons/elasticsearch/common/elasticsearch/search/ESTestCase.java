package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search;


import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.NamedXContentRegistry;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContent;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;

import java.io.IOException;

/**
 * 模仿测试Test类
 */
public abstract class ESTestCase {
    protected abstract NamedXContentRegistry xContentRegistry();


    protected final XContentParser createParser(XContent xContent, String data) throws IOException {
        return xContent.createParser(xContentRegistry(), data);
    }
}


