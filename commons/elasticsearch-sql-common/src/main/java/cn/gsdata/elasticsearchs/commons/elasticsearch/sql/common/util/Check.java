package cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common.util;

import cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common.SqlIllegalArgumentException;

/**
 * 数据检测
 *
 * @author daiyongjun
 */
public abstract class Check {
    public static void notNull(Object object, String message, Object... values) {
        if (object == null) {
            throw new SqlIllegalArgumentException(message, values);
        }
    }
}
