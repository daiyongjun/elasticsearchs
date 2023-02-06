package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent;


import java.io.IOException;

/**
 * ToXContent，将Pojo转换为XContent,按具体实现QueryBuilder类，Append出特定的XContentBuilder
 * 内置：toXContent(XContentBuilder builder, Params params)，可以将Object（params）转换为Builder类
 *
 * @author daiyongjun
 */
public interface ToXContent {
    /**
     * 按具体实现QueryBuilder类，Append出特定的XContentBuilder
     *
     * @param builder XContentBuilder
     * @return XContentBuilder
     * @throws IOException Builder进行Append过程中出现异常
     */
    XContentBuilder toXContent(XContentBuilder builder) throws IOException;

}
