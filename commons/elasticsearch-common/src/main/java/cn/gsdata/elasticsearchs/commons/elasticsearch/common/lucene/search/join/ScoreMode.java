package cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.join;

/**
 * 实现lucene的join中嵌套查询中的记分模式
 *
 * @author daiyongjun
 */
public enum ScoreMode {
    /**
     * 不得分。
     */
    None,
    /**
     * 父命中的分数是所有子分数的平均值。
     */
    Avg,
    /**
     * 父命中的分数是所有子分数的最大值。
     */
    Max,
    /**
     * 父命中的分数是所有子分数的总和。
     */
    Total,
    /**
     * 父命中的分数是所有子分数的最小值。
     */
    Min
}
