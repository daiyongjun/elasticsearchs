package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import java.util.Locale;

/**
 * 定义match-query翻译成多词源，多词源之间的关系，默认是OR
 *
 * @author daiyongjun
 */
public enum Operator {
    /**
     * OR 和AND的关系
     */
    OR, AND;

    public static Operator fromString(String op) {
        return valueOf(op.toUpperCase(Locale.ROOT));
    }
}
