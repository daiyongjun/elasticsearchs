package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.histogram;

/**
 * 日期直方图聚合桶builder类的属性内容DateHistogramInterval类
 *
 * @author daiyongjun
 */
public class DateHistogramInterval {

    public static final DateHistogramInterval SECOND = new DateHistogramInterval("1s");
    public static final DateHistogramInterval MINUTE = new DateHistogramInterval("1m");
    public static final DateHistogramInterval HOUR = new DateHistogramInterval("1h");
    public static final DateHistogramInterval DAY = new DateHistogramInterval("1d");
    public static final DateHistogramInterval WEEK = new DateHistogramInterval("1w");
    public static final DateHistogramInterval MONTH = new DateHistogramInterval("1M");
    public static final DateHistogramInterval QUARTER = new DateHistogramInterval("1q");
    public static final DateHistogramInterval YEAR = new DateHistogramInterval("1y");

    /**
     * DateHistogramInterval的详细内容
     */
    private final String expression;

    public DateHistogramInterval(String expression) {
        this.expression = expression;
    }

    public static DateHistogramInterval seconds(int sec) {
        return new DateHistogramInterval(sec + "s");
    }

    public static DateHistogramInterval minutes(int min) {
        return new DateHistogramInterval(min + "m");
    }

    public static DateHistogramInterval hours(int hours) {
        return new DateHistogramInterval(hours + "h");
    }

    public static DateHistogramInterval days(int days) {
        return new DateHistogramInterval(days + "d");
    }

    public static DateHistogramInterval weeks(int weeks) {
        return new DateHistogramInterval(weeks + "w");
    }

    @Override
    public String toString() {
        return expression;
    }

}
