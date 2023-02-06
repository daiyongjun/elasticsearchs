package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ToXContent;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.Query;

import java.io.IOException;

/**
 * 查询基础类
 *
 * @author daiyongjun
 */
public interface QueryBuilder extends ToXContent {
    /**
     * 将此 QueryBuilder 转换为 lucene {@link Query}。
     *
     * @param context QueryShardContext
     * @return Query
     * @throws IOException 转换过程出现异常
     */
    Query toQuery(QueryShardContext context) throws IOException;

    /**
     * 将此 QueryBuilder 转换为充当过滤器的未评分 lucene {@link Query}。
     *
     * @param context QueryShardContext
     * @return Query
     * @throws IOException 转换过程出现异常
     */
    Query toFilter(QueryShardContext context) throws IOException;

    /**
     * 返回唯一标识查询的名称
     *
     * @return String
     */
    String getName();

    /**
     * 返回分配给查询的任意名称（请参阅命名查询）。
     *
     * @return String
     */
    String queryName();

    /**
     * 设置要分配给查询的任意名称（请参阅命名查询），并返回当QueryBuilder。
     *
     * @param queryName 设置查询名称
     * @return QueryBuilder
     */
    QueryBuilder queryName(String queryName);

    /**
     * 返回此查询的权重。
     *
     * @return float
     */
    float boost();

    /**
     * 设置此查询的权重。匹配此查询的文档（除了正常的 * 权重）将其分数乘以提供的提升。
     *
     * @param boost 权重
     * @return QueryBuilder
     */
    QueryBuilder boost(float boost);

    /**
     * 设置此查询的起始stamp
     *
     * @param startStamp 起始stamp
     * @return QueryBuilder
     */
    QueryBuilder startStamp(Long startStamp);

    /**
     * 获取startStamp的值
     *
     * @return Long
     */
    long startStamp();

    /**
     * 设置此查询的结束stamp
     *
     * @param endStamp 结束stamp
     * @return QueryBuilder
     */
    QueryBuilder endStamp(Long endStamp);

    /**
     * 获取endStamp
     *
     * @return long
     */
    long endStamp();
}
