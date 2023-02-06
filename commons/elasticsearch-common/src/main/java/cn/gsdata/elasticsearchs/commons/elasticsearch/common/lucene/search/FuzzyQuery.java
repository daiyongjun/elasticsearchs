package cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search;

/**
 * 这个是重写org.apache.lucene的包内容，原因lucene无引入任何内容
 * FuzzyQuery的模糊查询类
 *
 * @author daiyongjun
 */
public class FuzzyQuery {
    /**
     * (可选的，integer类型)为模糊匹配而保持不变的起始字符数。默认值为0。
     * prefix_length 表示不能没模糊化的初始字符数。由于大部分的拼写错误发生在词的结尾，而不是词的开始，使用 prefix_length 就可以完成优化。注意 prefix_length 必须结合 fuzziness 参数使用。
     */
    public final static int defaultPrefixLength = 0;
    /**
     * (可选的，integer类型)查询将扩展到的最大词条数。默认值为50。
     */
    public final static int defaultMaxExpansions = 50;
    /**
     * (可选的，boolean类型)如果为true，用于模糊匹配的编辑包括两个相邻字符的换位(ab→ba)。默认值为true。
     */
    public final static boolean defaultTranspositions = true;
}
