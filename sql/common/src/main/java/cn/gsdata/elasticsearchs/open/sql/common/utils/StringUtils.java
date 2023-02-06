package cn.gsdata.elasticsearchs.open.sql.common.utils;

import com.google.common.base.Strings;

import java.util.Locale;

/**
 * 字符串处理工具类
 *
 * @author daiyongjun
 */
public class StringUtils {
    /**
     * 为指定字符串取消指定标记
     *
     * @param string 待处理的字符串
     * @param marks  指定的标记列表
     * @return String
     */
    public static String unquoteMarks(String string, String... marks) {
        //String[] marks = {"\"", "'", "`"};
        boolean quoted = false;
        for (String mark : marks) {
            if (isQuoted(string, mark)) {
                quoted = true;
                break;
            }
        }
        if (quoted) {
            return string.substring(1, string.length() - 1);
        } else {
            return string;
        }
    }

    /**
     * 取消以 ` 作为标记的标识符。如：`标识符`
     *
     * @param identifier `标识符`｜标识符
     * @return String   标识符
     */
    public static String unquoteIdentifier(String identifier) {
        return unquoteMarks(identifier, "`");
    }

    /**
     * 取消以" 或'或`作为标记的标识符。如：" 标识符"  或者 '标识符' 或者 `标识符`
     *
     * @param text " 标识符"  ｜ '标识符' ｜ `标识符` ｜ 标识符
     * @return String   标识符
     */
    public static String unquoteText(String text) {
        return unquoteMarks(text, "\"", "'", "`");
    }


    public static String format(final String format, Object... args) {
        return String.format(Locale.ROOT, format, args);
    }

    /**
     * Text是否被指定标记被引用（开头或者结尾）
     *
     * @param text String
     * @param mark String 指定的标记
     * @return boolean 是否被引用
     */
    private static boolean isQuoted(String text, String mark) {
        return !Strings.isNullOrEmpty(text) && text.startsWith(mark) && text.endsWith(mark);
    }
}
