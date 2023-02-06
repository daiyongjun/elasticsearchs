package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.order;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ToXContent;

/**
 * BucketOrder  基于桶排序
 *
 * @author daiyongjun
 */
public abstract class BucketOrder implements ToXContent {

    public static BucketOrder count(boolean asc) {
        return asc ? InternalOrder.COUNT_ASC : InternalOrder.COUNT_DESC;
    }

    /**
     * 获取排序ID
     *
     * @return ID
     */
    abstract byte id();
}
