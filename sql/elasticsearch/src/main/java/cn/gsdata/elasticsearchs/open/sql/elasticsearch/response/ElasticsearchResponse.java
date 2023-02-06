package cn.gsdata.elasticsearchs.open.sql.elasticsearch.response;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;

import java.util.Iterator;

/**
 * Elasticsearch搜索响应。
 *
 * @author daiyongjun
 */
public class ElasticsearchResponse implements Iterable<ExprValue> {
    /**
     * 查询结果 (non-aggregation)
     */
    private final SearchHits hits;
    /**
     * 聚合结果
     */
    private final Aggregations aggregations;

    /**
     * ElasticsearchResponse 构造函数
     *
     * @param esResponse SearchResponse Es封装的搜索响应
     */
    public ElasticsearchResponse(SearchResponse esResponse) {
        this.hits = esResponse.getHits();
        this.aggregations = esResponse.getAggregations();
    }

    /**
     * ElasticsearchResponse 构造函数
     *
     * @param hits SearchHits Es封装的查询结果 (non-aggregation)
     */
    public ElasticsearchResponse(SearchHits hits) {
        this.hits = hits;
        this.aggregations = null;
    }

    /**
     * Elasticsearch搜索响应结果为空
     *
     * @return boolean ture 结果为空 false 结果不为空
     */
    public boolean isEmpty() {
        return (hits.getHits() == null) || (hits.getHits().length == 0) && aggregations == null;
    }

    /**
     * 
     * @return  boolean true 搜索结果聚合结果不为空，false搜索结果没有聚合结果
     */
    public boolean isAggregationResponse() {
        return aggregations != null;
    }

    /**
     * 重写iterator，使响应可迭代而不需要显式返回内部数据结构。
     *
     * @return Iterator<ExprValue>
     */
    @Override
    public Iterator<ExprValue> iterator() {
        return null;
    }
}
