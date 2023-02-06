package cn.gsdata.elasticsearchs.open.sql.elasticsearch.client;

import cn.gsdata.elasticsearchs.open.sql.elasticsearch.mapping.IndexMapping;

import java.util.Map;

/**
 * Elasticsearch客户端抽象来包装不同的Elasticsearch客户端实现。
 * 例如，通过节点客户端实现ES插件（ElasticsearchNodeClient），或通过REST客户端实现独立模式（ElasticsearchRestClient）。
 *
 * @author daiyongjun
 */
public interface ElasticsearchClient {
    String META_CLUSTER_NAME = "CLUSTER_NAME";

    /**
     * 根据给定的索引表达式获取索引映射。
     *
     * @param indexExpression 给定的索引表达式
     * @return Map<String, IndexMapping>的> 从索引名到其映射的索引映射
     */
    Map<String, IndexMapping> getIndexMappings(String indexExpression);

    ElasticsearchResponse search(ElasticsearchRequest request);

}
