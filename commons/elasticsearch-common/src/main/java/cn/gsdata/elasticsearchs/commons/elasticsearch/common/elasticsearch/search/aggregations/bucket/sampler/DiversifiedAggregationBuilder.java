package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.sampler;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;

import java.io.IOException;

/**
 * 参考文档： https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-bucket-sampler-aggregation.html
 * 抽样桶，于将任何子聚合的处理限制为得分最高的文档样本。一般和聚焦聚合配合使用，过滤一些得分比较低的桶，防止长尾
 * @author daiyongjun
 */
public class DiversifiedAggregationBuilder extends ValuesSourceAggregationBuilder<DiversifiedAggregationBuilder> {
    /**
     * 构造一个新的聚合构建器。
     *
     * @param name 当前聚合的名称
     */
    protected DiversifiedAggregationBuilder(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    protected XContentBuilder doXContentBody(XContentBuilder builder) throws IOException {
        return null;
    }
}
