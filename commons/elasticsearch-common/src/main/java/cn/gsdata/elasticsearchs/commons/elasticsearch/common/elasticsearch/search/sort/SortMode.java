package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.sort;

import java.util.Locale;
import java.util.Objects;

/**
 * Elasticsearch 支持按数组或多值字段排序，如SQL中Order by A,B,C
 * SortMode内置了min、max、sum、avg、median
 *
 * @author daiyongjun
 */
public enum SortMode {
    /**
     * Order by A,B,C多值字段，可以把[A,B,C]理解为一个数组，MIN为取数组中的最小值、MAX为取数组中的最大值
     * SUM为取数组三个值只和、AVG为取数组中的平均值、MEDIAN表示为三者的中位数
     */
    MIN, MAX, SUM, AVG, MEDIAN;

    public static SortMode fromString(final String str) {
        Objects.requireNonNull(str, "input string is null");
        switch (str.toLowerCase(Locale.ROOT)) {
            case ("min"):
                return MIN;
            case ("max"):
                return MAX;
            case ("sum"):
                return SUM;
            case ("avg"):
                return AVG;
            case ("median"):
                return MEDIAN;
            default:
                throw new IllegalArgumentException("Unknown SortMode [" + str + "]");
        }
    }
}
