package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;

import java.io.IOException;

/**
 * 定义抽象聚合抽象类（内置泛型AB【aggregationBuilder】）
 *
 * @author daiyongjun
 */
public abstract class AbstractAggregationBuilder<AB extends AbstractAggregationBuilder<AB>> extends AggregationBuilder {

    /**
     * 构造一个新的聚合构建器。
     *
     * @param name 当前聚合的名称
     */
    protected AbstractAggregationBuilder(String name) {
        super(name);
    }

    @Override
    public AB subAggregation(AggregationBuilder subAggregation) {
        if (subAggregation == null) {
            throw new IllegalArgumentException("[aggregation] must not be null: [" + name + "]");
        }
        subAggregations.addAggregationBuilder(subAggregation);
        return (AB) this;
    }

    @Override
    public AB subAggregations(AggregationsBuilder subAggregations) {
        if (subAggregations == null) {
            throw new IllegalArgumentException("[subFactories] must not be null: [" + name + "]");
        }
        this.subAggregations = subAggregations;
        return (AB) this;
    }


    //@SuppressWarnings("unchecked")
    //@Override
    //public AB subAggregation(AggregationBuilder subAggregation) {
    //    if (subAggregation == null) {
    //        throw new IllegalArgumentException("[aggregation] must not be null: [" + name + "]");
    //    }
    //    aggregationsBuilder.addAggregator(subAggregation);
    //    return (AB) this;
    //}

    /**
     * 按实现AggregationBuilder类的toXContent(builder)方法，Append出特定的XContentBuilder
     * {"test_agg":{"terms":{"field":"foo"}}}
     *
     * @param builder XContentBuilder
     * @return XContentBuilder
     * @throws IOException Builder进行Append过程中出现异常
     */
    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        builder.startObject(name);
        builder.field(getType());
        internalXContent(builder);
        if (subAggregations != null && (subAggregations.count()) > 0) {
            builder.field("aggregations");
            subAggregations.toXContent(builder);
        }
        return builder.endObject();
    }

    /**
     * 0
     * 定义抽象internalXContent(builder)方法，依具体的实现类，实现Append出特定的XContentBuilder
     *
     * @param builder XContentBuilder
     * @return XContentBuilder
     * @throws IOException Builder进行Append过程中出现异常
     */
    protected abstract XContentBuilder internalXContent(XContentBuilder builder) throws IOException;
}
