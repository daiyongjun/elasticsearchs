package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;

import java.io.IOException;

/**
 * 定义一个能够解析的查询解析器
 *
 * @author daiyongjun
 */
@FunctionalInterface
public interface QueryParser<QB extends QueryBuilder> {

    /**
     * 根据 XContentParser 内容解析器 持有的查询创建QueryBuilder
     *
     * @param parser 内容解析器
     * @return QB
     * @throws IOException 异常信息
     */
    QB fromXContent(XContentParser parser, Object context) throws IOException;
}
