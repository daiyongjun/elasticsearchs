package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.metrics;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.ElasticsearchException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AggregationsBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;

/**
 * 构建MetricsAggregationBuilder类，其实只是为了subAggregations中不支持子AggregationsBuilder
 *
 * @author daiyongjun
 */
public abstract class MetricsAggregationBuilder<AB extends ValuesSourceAggregationBuilder<AB>> extends ValuesSourceAggregationBuilder<AB> {
    /**
     * 构造一个新的聚合构建器。
     *
     * @param name 当前聚合的名称
     */
    protected MetricsAggregationBuilder(String name) {
        super(name);
    }

    @Override
    public AB subAggregations(AggregationsBuilder subAggregations) {
        throw new ElasticsearchException("Aggregator [" + name + "] of type ["
                + getType() + "] cannot accept sub-aggregations");
    }
}
