package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.logging;


import java.util.HashSet;
import java.util.Set;

/**
 * Elasticsearch 日志消息的格式字符串。
 * @author daiyongjun
 */
public class LoggerMessageFormat {
    /**
     * 日志消息的格式化类
     */
    public static String format(final String messagePattern, final Object... argArray) {
        return format(null, messagePattern, argArray);
    }

    public static String format(final String prefix, final String messagePattern, final Object... argArray) {
        //prefix + messagePattern
        if (messagePattern == null) {
            return null;
        }
        if (argArray == null) {
            if (prefix == null) {
                return messagePattern;
            } else {
                return prefix + messagePattern;
            }
        }
        return  null;
    }
}
