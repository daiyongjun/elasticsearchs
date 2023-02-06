package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.Strings;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ToXContent;

/**
 * 聚合构建类
 *
 * @author daiyongjun
 */
public abstract class AggregationBuilder implements ToXContent {
    /**
     * 聚合自定义名称如{"aggs":{"test_agg":{"terms":{"field":"foo"}}}} 中定义的test_agg的名称
     */
    protected final String name;

    protected AggregationsBuilder subAggregations = new AggregationsBuilder();

    /**
     * 构造一个新的聚合构建器。
     *
     * @param name 当前聚合的名称
     */
    protected AggregationBuilder(String name) {
        if (name == null) {
            throw new IllegalArgumentException("[name] must not be null: [" + name + "]");
        }
        this.name = name;
    }

    /**
     * 返回当前聚合的名称
     */
    public String getName() {
        return name;
    }


    /**
     * 此构建器构建的聚合类型的名称。
     *
     * @return String
     */
    public abstract String getType();

    /**
     * 添加一个子聚合到这个构建器。
     *
     * @param subAggregation 子聚合构造器
     * @return AggregationBuilder
     */
    public abstract AggregationBuilder subAggregation(AggregationBuilder subAggregation);

    /**
     * 如果此聚合支持子聚合，则设置子聚合。
     *
     * @param subAggregationsBuilder 子聚合AggregationsBuilder
     * @return BaseAggregationBuilder
     */
    public abstract AggregationBuilder subAggregations(AggregationsBuilder subAggregationsBuilder);


    @Override
    public String toString() {
        return Strings.toString(this);
    }

}
