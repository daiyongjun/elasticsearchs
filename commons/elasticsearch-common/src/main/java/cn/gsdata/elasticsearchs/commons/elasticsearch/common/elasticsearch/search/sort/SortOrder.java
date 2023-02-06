package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.sort;

import java.util.Locale;

/**
 * 定义Order排序的枚举类型
 *
 * @author daiyongjun
 */
public enum SortOrder {
    /**
     * 升序
     */
    ASC {
        @Override
        public String toString() {
            return "asc";
        }
    },
    /**
     * 降序
     */
    DESC {
        @Override
        public String toString() {
            return "desc";
        }
    };

    public static SortOrder fromString(String op) {
        return valueOf(op.toUpperCase(Locale.ROOT));
    }
}
