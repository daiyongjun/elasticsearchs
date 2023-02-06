package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import java.util.function.LongSupplier;

/**
 * 在分片级别创建 lucene 查询的上下文对象。
 *
 * @author daiyongjun
 */
public class QueryShardContext {
    protected final LongSupplier nowInMillis;

    public QueryShardContext(LongSupplier nowInMillis) {
        this.nowInMillis = nowInMillis;
    }

    public long nowInMillis() {
        return nowInMillis.getAsLong();
    }
}
