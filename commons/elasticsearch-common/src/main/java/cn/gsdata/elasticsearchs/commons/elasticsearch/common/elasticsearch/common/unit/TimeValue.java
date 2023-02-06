package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.common.unit;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.ElasticsearchParseException;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 时间
 *
 * @author daiyongjun
 */
public class TimeValue {

    private final long duration;

    private final TimeUnit timeUnit;

    public static final TimeValue MINUS_ONE = timeValueMillis(-1);
    public static final TimeValue ZERO = timeValueMillis(0);


    public static TimeValue timeValueMillis(long millis) {
        return new TimeValue(millis, TimeUnit.MILLISECONDS);
    }

    public TimeValue(long duration, TimeUnit timeUnit) {
        this.duration = duration;
        this.timeUnit = timeUnit;
    }

    public static TimeValue parseTimeValue(String sValue, TimeValue defaultValue, String settingName) {
        settingName = Objects.requireNonNull(settingName);
        if (sValue == null) {
            return defaultValue;
        }
        final String normalized = sValue.toLowerCase(Locale.ROOT).trim();
        if (normalized.endsWith("nanos")) {
            return new TimeValue(parse(sValue, normalized, "nanos"), TimeUnit.NANOSECONDS);
        } else if (normalized.endsWith("micros")) {
            return new TimeValue(parse(sValue, normalized, "micros"), TimeUnit.MICROSECONDS);
        } else if (normalized.endsWith("ms")) {
            return new TimeValue(parse(sValue, normalized, "ms"), TimeUnit.MILLISECONDS);
        } else if (normalized.endsWith("s")) {
            return new TimeValue(parse(sValue, normalized, "s"), TimeUnit.SECONDS);
        } else if (sValue.endsWith("m")) {
            // parsing minutes should be case-sensitive as 'M' means "months", not "minutes"; this is the only special case.
            return new TimeValue(parse(sValue, normalized, "m"), TimeUnit.MINUTES);
        } else if (normalized.endsWith("h")) {
            return new TimeValue(parse(sValue, normalized, "h"), TimeUnit.HOURS);
        } else if (normalized.endsWith("d")) {
            return new TimeValue(parse(sValue, normalized, "d"), TimeUnit.DAYS);
        } else if (normalized.matches("-0*1")) {
            return TimeValue.MINUS_ONE;
        } else if (normalized.matches("0+")) {
            return TimeValue.ZERO;
        } else {
            // Missing units:
            throw new ElasticsearchParseException(
                    "failed to parse setting [{}] with value [{}] as a time value: unit is missing or unrecognized",
                    settingName,
                    sValue);
        }
    }

    private static long parse(final String initialInput, final String normalized, final String suffix) {
        final String s = normalized.substring(0, normalized.length() - suffix.length()).trim();
        try {
            return Long.parseLong(s);
        } catch (final NumberFormatException e) {
            try {
                @SuppressWarnings("unused") final double ignored = Double.parseDouble(s);
                throw new ElasticsearchParseException("failed to parse [{}], fractional time values are not supported", e, initialInput);
            } catch (final NumberFormatException ignored) {
                throw new ElasticsearchParseException("failed to parse [{}]", e, initialInput);
            }
        }
    }

}
