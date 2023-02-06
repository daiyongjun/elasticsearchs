package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.index.search;

/**
 * matchQuery真实的类，Builder为其构建类
 *
 * @author daiyongjun
 */
public class MatchQuery {

    /**
     * (可选的，string类型)表示如果分析器删除所有分词，是否不返回文档。
     * （1）none（默认）：如果分析器删除所有分词，则不会返回文档。
     * （2）all：如果分析器删除所有分词，返回所有文档。
     *
     * @author daiyongjun
     */
    public enum ZeroTermsQuery {
        /**
         * none（默认）：如果分析器删除所有分词，则不会返回文档。
         * all：如果分析器删除所有分词，返回所有文档。
         */
        NONE(0),
        ALL(1);
        /**
         * 预设ZeroTermsQuery的状态值
         */
        private final int ordinal;

        ZeroTermsQuery(int ordinal) {
            this.ordinal = ordinal;
        }
    }


    /**
     * 默认zero terms query
     */
    public static final ZeroTermsQuery DEFAULT_ZERO_TERMS_QUERY = ZeroTermsQuery.NONE;

    /**
     * 默认phrase匹配 slop的值
     */
    public static final int DEFAULT_PHRASE_SLOP = 0;
    /**
     * 默认leniency setting
     */
    public static final boolean DEFAULT_LENIENCY = false;
}
