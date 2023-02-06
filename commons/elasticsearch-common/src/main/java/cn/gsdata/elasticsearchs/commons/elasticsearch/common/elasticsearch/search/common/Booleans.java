package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common;

/**
 * 注意：当前类是禁止使用的，非开源代码
 *
 *  工具类Booleans,用于快速判定出boolean
 */
public final class Booleans {
    private Booleans() {
        throw new AssertionError("No instances intended");
    }

    /**
     * 将字符串解析为boolean类型，否则抛出异常
     */
    public static boolean parseBoolean(String value) {
        if (isFalse(value)) {
            return false;
        }
        if (isTrue(value)) {
            return true;
        }
        throw new IllegalArgumentException("Failed to parse value [" + value + "] as only [true] or [false] are allowed.");
    }

    public static Boolean parseBoolean(String value, Boolean defaultValue) {
        if (Strings.hasText(value)) {
            return parseBoolean(value);
        }
        return defaultValue;
    }

    /**
     * @return  判断字符串是否为"false"。
     */
    public static boolean isFalse(String value) {
        return "false".equals(value);
    }

    /**
     * @return  判断字符串是否为"true"。
     */
    public static boolean isTrue(String value) {
        return "true".equals(value);
    }
}
