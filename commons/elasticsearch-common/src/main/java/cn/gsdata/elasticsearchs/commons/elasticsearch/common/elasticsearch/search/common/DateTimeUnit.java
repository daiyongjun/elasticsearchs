package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.ElasticsearchException;
import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import java.util.function.Function;

/**
 * 直方图桶中时间枚举类型,当前枚举类型为📊桶的范围
 *
 * @author daiyongjun
 */
public enum DateTimeUnit {
    /**
     * 定义不同的DateTimeField field(DateTimeZone tz);转换逻辑的枚举类型如：每年的每周，每年的每月......
     */
    WEEK_OF_WEEKYEAR((byte) 1, tz -> ISOChronology.getInstance(tz).weekOfWeekyear()),
    YEAR_OF_CENTURY((byte) 2, tz -> ISOChronology.getInstance(tz).yearOfCentury()),
    MONTH_OF_YEAR((byte) 4, tz -> ISOChronology.getInstance(tz).monthOfYear()),
    DAY_OF_MONTH((byte) 5, tz -> ISOChronology.getInstance(tz).dayOfMonth()),
    HOUR_OF_DAY((byte) 6, tz -> ISOChronology.getInstance(tz).hourOfDay()),
    MINUTES_OF_HOUR((byte) 7, tz -> ISOChronology.getInstance(tz).minuteOfHour()),
    SECOND_OF_MINUTE((byte) 8, tz -> ISOChronology.getInstance(tz).secondOfMinute());

    private final byte id;
    private final Function<DateTimeZone, DateTimeField> fieldFunction;

    DateTimeUnit(byte id, Function<DateTimeZone, DateTimeField> fieldFunction) {
        this.id = id;
        this.fieldFunction = fieldFunction;
    }

    public byte id() {
        return id;
    }

    public DateTimeField field(DateTimeZone tz) {
        return fieldFunction.apply(tz);
    }

    public static DateTimeUnit resolve(byte id) {
        switch (id) {
            case 1:
                return WEEK_OF_WEEKYEAR;
            case 2:
                return YEAR_OF_CENTURY;
            case 4:
                return MONTH_OF_YEAR;
            case 5:
                return DAY_OF_MONTH;
            case 6:
                return HOUR_OF_DAY;
            case 7:
                return MINUTES_OF_HOUR;
            case 8:
                return SECOND_OF_MINUTE;
            default:
                throw new ElasticsearchException("Unknown date time unit id [" + id + "]");
        }
    }
}
