package cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.util.automaton;

/**
 * @author daiyongjun
 */
public class RegExp {
    /**
     * INTERVAL
     * 语法标志，启用数字间隔 ( < n - m > )。
     */
    public static final int INTERSECTION = 0x0001;
    /**
     * COMPLEMENT
     * 语法标志，启用补码 ( ~ )。
     */
    public static final int COMPLEMENT = 0x0002;
    /**
     * EMPTY
     * 语法标志，启用空语言 ( # )。
     */
    public static final int EMPTY = 0x0004;
    /**
     * ANYSTRING
     * 语法标志，启用任意字符串 ( @ )
     */
    public static final int ANYSTRING = 0x0008;
    /**
     * INTERSECTION
     * 语法标志，启用交集 ( & )。
     */
    public static final int INTERVAL = 0x0020;
    /**
     * NONE
     * 语法标志，不启用可选的正则表达式语法。
     */
    public static final int NONE = 0x0000;
    /**
     * ALL
     * 语法标志，启用所有可选的正则表达式语法。
     */
    public static final int ALL = 0xffff;
}
