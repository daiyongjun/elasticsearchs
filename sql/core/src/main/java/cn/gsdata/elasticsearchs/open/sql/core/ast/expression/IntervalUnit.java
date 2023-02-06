package cn.gsdata.elasticsearchs.open.sql.core.ast.expression;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 定义间隔之间的单位的枚举类型 如：interval 1 day、interval 2 month
 *
 * @author daiyongjun
 */
@RequiredArgsConstructor
public enum IntervalUnit {
    /**
     * 定义不同的定义间隔之间的单位
     */
    UNKNOWN,
    MICROSECOND,
    SECOND,
    MINUTE,
    HOUR,
    DAY,
    WEEK,
    MONTH,
    QUARTER,
    YEAR,
    SECOND_MICROSECOND,
    MINUTE_MICROSECOND,
    MINUTE_SECOND,
    HOUR_MICROSECOND,
    HOUR_SECOND,
    HOUR_MINUTE,
    DAY_MICROSECOND,
    DAY_SECOND,
    DAY_MINUTE,
    DAY_HOUR,
    YEAR_MONTH;

    private static final List<IntervalUnit> INTERVAL_UNITS;

    /*
      初始化INTERVAL_UNITS
     */
    static {
        ImmutableList.Builder<IntervalUnit> builder = new ImmutableList.Builder<>();
        INTERVAL_UNITS = builder.add(IntervalUnit.values()).build();
    }

    /**
     * 基于间隔单元的名称 返回特定的枚举类型
     *
     * @param unit String  间隔单元的名称
     * @return IntervalUnit
     */
    public static IntervalUnit of(String unit) {
        return INTERVAL_UNITS.stream()
                .filter(v -> unit.equalsIgnoreCase(v.name()))
                .findFirst()
                .orElse(IntervalUnit.UNKNOWN);
    }
}
