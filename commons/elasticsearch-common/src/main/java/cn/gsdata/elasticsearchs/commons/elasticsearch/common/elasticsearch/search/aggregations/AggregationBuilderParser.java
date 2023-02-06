package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;

import java.io.IOException;

/**
 * @author daiyongjun
 */
@FunctionalInterface
public interface AggregationBuilderParser<AB extends AggregationBuilder> {

    /**
     * 根据 XContentParser 内容解析器 持有的聚合信息，和聚合名称创建一个AggregationBuilder
     *
     * @param aggregationName 聚合名称
     * @param parser          内容解析器
     * @return AB
     * @throws IOException 解析过程异常信息
     */
    AB parse(String aggregationName, XContentParser parser) throws IOException;
}
