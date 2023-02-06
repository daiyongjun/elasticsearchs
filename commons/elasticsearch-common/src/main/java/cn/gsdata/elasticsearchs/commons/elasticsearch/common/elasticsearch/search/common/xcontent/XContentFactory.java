package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.json.JsonXContent;

import java.io.IOException;

/**
 * XContent的工厂类，用于创建各种XContent类
 * XContent包括 JsonXContent、CborXContent、SmileXContent、YamlXContent ......
 *
 * @author daiyongjun
 */
public class XContentFactory {
    public static XContentBuilder jsonBuilder() throws IOException {
        return contentBuilder(XContentType.JSON);
    }

    public static XContentBuilder contentBuilder(XContentType type) throws IOException {
        if (type == XContentType.JSON) {
            return JsonXContent.contentBuilder();
        }
        //JsonXContent、CborXContent、SmileXContent、YamlXContent ......
        throw new IllegalArgumentException("No matching content type for " + type);
    }
}
