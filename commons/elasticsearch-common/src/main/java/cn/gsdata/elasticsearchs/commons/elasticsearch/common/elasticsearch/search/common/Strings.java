package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ToXContent;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.json.JsonXContent;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.ElasticsearchException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.ExceptionsHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 工具类Strings,用于字符串处理类
 *
 * @author daiyongjun
 */
public class Strings {
    public static final String[] EMPTY_ARRAY = new String[0];

    /**
     * 判断字符是否为空
     *
     * @param str 待判断字符
     * @return boolean
     */
    public static boolean isEmpty(CharSequence str) {
        return !hasLength(str);
    }

    /**
     * 检查给定的字符既不是 null也不是长度 0。
     *
     * @param str 待判断的字符
     * @return boolean
     */
    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }


    /**
     * 判断字符串中间是否存在空白字符
     *
     * @param str 待判断的字符串
     * @return boolean
     */
    public static boolean hasText(String str) {
        return hasText((CharSequence) str);
    }

    /**
     * 判断字符串是否包含空白字符
     *
     * @param str 待判断的字符串
     * @return boolean
     */
    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }


    public static String[] delimitedListToStringArray(String str, String delimiter) {
        return delimitedListToStringArray(str, delimiter, null);
    }

    public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
        if (str == null) {
            return new String[0];
        }
        if (delimiter == null) {
            return new String[]{str};
        }
        List<String> result = new ArrayList<>();
        if ("".equals(delimiter)) {
            for (int i = 0; i < str.length(); i++) {
                result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
            }
        } else {
            int pos = 0;
            int delPos;
            while ((delPos = str.indexOf(delimiter, pos)) != -1) {
                result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                pos = delPos + delimiter.length();
            }
            if (str.length() > 0 && pos <= str.length()) {
                // Add rest of String, but not in case of empty input.
                result.add(deleteAny(str.substring(pos), charsToDelete));
            }
        }
        return toStringArray(result);
    }

    public static String[] toStringArray(Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }

    public static String deleteAny(String inString, String charsToDelete) {
        if (!hasLength(inString) || !hasLength(charsToDelete)) {
            return inString;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inString.length(); i++) {
            char c = inString.charAt(i);
            if (charsToDelete.indexOf(c) == -1) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 将ToXContent的Object转换为String (将转换后的Object转换为String)
     *
     * @param toXContent 待转换的Object
     * @return String
     */
    public static String toString(ToXContent toXContent) {
        try {
            XContentBuilder builder = createBuilder();
            toXContent.toXContent(builder);
             return builder.string();
        } catch (IOException e) {
            try {
                XContentBuilder builder = createBuilder();
                builder.startObject();
                builder.field("error", "error building toString out of XContent: " + e.getMessage());
                builder.field("stack_trace", ExceptionsHelper.stackTrace(e));
                builder.endObject();
                return builder.string();
            } catch (IOException e2) {
                throw new ElasticsearchException("cannot generate error message for deserialization", e);
            }
        }
    }

    private static XContentBuilder createBuilder() throws IOException {
        return JsonXContent.contentBuilder();
    }
}
