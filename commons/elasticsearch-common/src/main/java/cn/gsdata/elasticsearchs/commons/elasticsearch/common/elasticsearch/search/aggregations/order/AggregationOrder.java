package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.order;

public class AggregationOrder extends InternalOrder {
    static final byte ID = 0;

    /**
     * BucketOrder 构造方法，按指定排序属性
     *
     * @param key Order
     * @param asc 是否倒排
     */
    public AggregationOrder(String key, boolean asc) {
        super(ID, key, asc);
    }
}
