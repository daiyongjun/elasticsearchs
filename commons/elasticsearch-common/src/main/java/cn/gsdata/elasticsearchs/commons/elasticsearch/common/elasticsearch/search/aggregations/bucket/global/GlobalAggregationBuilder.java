package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.global;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AbstractAggregationBuilder;

import java.io.IOException;

/**
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-bucket-global-aggregation.html
 * 设置全局聚合，聚合不受 query条件的限制
 *
 * @author daiyongjun
 */
public class GlobalAggregationBuilder extends AbstractAggregationBuilder<GlobalAggregationBuilder> {
    public static final String NAME = "global";

    public static GlobalAggregationBuilder parse(String aggregationName, XContentParser parser) throws IOException {
        parser.nextToken();
        return new GlobalAggregationBuilder(aggregationName);
    }

    /**
     * 构造一个新的聚合构建器。
     *
     * @param name 当前聚合的名称
     */
    protected GlobalAggregationBuilder(String name) {
        super(name);
    }

    @Override
    protected XContentBuilder internalXContent(XContentBuilder builder) throws IOException {
        builder.startObject();
        builder.endObject();
        return builder;
    }

    @Override
    public String getType() {
        return NAME;
    }
}
