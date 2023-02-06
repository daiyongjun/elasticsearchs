package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.Strings;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.util.automaton.RegExp;

import java.util.Locale;

/**
 * 定义lucene支持的正则标识
 * 参考文档：https://lucene.apache.org/core/4_9_0/core/org/apache/lucene/util/automaton/RegExp.html
 *
 * @author daiyongjun
 */
public enum RegexpFlag {
    /**
     * lucene定义的正则标识
     */
    INTERSECTION(RegExp.INTERSECTION),
    COMPLEMENT(RegExp.COMPLEMENT),
    EMPTY(RegExp.EMPTY),
    ANYSTRING(RegExp.ANYSTRING),
    INTERVAL(RegExp.INTERVAL),
    NONE(RegExp.NONE),
    ALL(RegExp.ALL);
    final int value;

    RegexpFlag(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static int resolveValue(String flags) {
        if (flags == null || flags.isEmpty()) {
            return RegExp.ALL;
        }
        int magic = RegExp.NONE;
        for (String s : Strings.delimitedListToStringArray(flags, "|")) {
            if (s.isEmpty()) {
                continue;
            }
            try {
                RegexpFlag flag = RegexpFlag.valueOf(s.toUpperCase(Locale.ROOT));
                if (flag == RegexpFlag.NONE) {
                    continue;
                }
                if (flag == RegexpFlag.ALL) {
                    return flag.value();
                }
                magic |= flag.value();
            } catch (IllegalArgumentException iae) {
                throw new IllegalArgumentException("Unknown regexp flag [" + s + "]");
            }
        }
        return magic;
    }
}
