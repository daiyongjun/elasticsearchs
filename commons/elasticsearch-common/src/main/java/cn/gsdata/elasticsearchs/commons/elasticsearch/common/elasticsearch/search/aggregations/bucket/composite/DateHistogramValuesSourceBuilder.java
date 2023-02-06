package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.composite;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.support.ValueType;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.joda.time.DateTimeZone;

import java.io.IOException;

public class DateHistogramValuesSourceBuilder extends CompositeValuesSourceBuilder<DateHistogramValuesSourceBuilder> {
    static final String TYPE = "date_histogram";
    public static final ParseField INTERVAL_FIELD = new ParseField("interval");

    private long interval = 0;
    private DateHistogramInterval dateHistogramInterval;
    private DateTimeZone timeZone = null;

    private static final ObjectParser<Void, DateHistogramValuesSourceBuilder> PARSER;

    static {
        PARSER = new ObjectParser<>(DateHistogramValuesSourceBuilder.TYPE);
        PARSER.declareField((histogram, interval) -> {
            if (interval instanceof Long) {
                histogram.interval((long) interval);
            } else {
                histogram.dateHistogramInterval((DateHistogramInterval) interval);
            }
        }, p -> {
            if (p.currentToken() == XContentParser.Token.VALUE_NUMBER) {
                return p.longValue();
            } else {
                return new DateHistogramInterval(p.text());
            }
        }, INTERVAL_FIELD, ObjectParser.ValueType.LONG);
        PARSER.declareField(DateHistogramValuesSourceBuilder::timeZone, p -> {
            if (p.currentToken() == XContentParser.Token.VALUE_STRING) {
                return DateTimeZone.forID(p.text());
            } else {
                return DateTimeZone.forOffsetHours(p.intValue());
            }
        }, new ParseField("time_zone"), ObjectParser.ValueType.LONG);
        declareValuesSourceFields(PARSER, ValueType.NUMERIC);
    }

    DateHistogramValuesSourceBuilder(String name) {
        super(name);
    }

    static DateHistogramValuesSourceBuilder parse(String name, XContentParser parser) throws IOException {
        return PARSER.parse(parser, null, new DateHistogramValuesSourceBuilder(name));
    }

    public DateHistogramValuesSourceBuilder interval(long interval) {
        if (interval < 1) {
            throw new IllegalArgumentException("[interval] must be 1 or greater for [date_histogram] source");
        }
        this.interval = interval;
        return this;
    }

    public DateHistogramValuesSourceBuilder dateHistogramInterval(DateHistogramInterval dateHistogramInterval) {
        if (dateHistogramInterval == null) {
            throw new IllegalArgumentException("[dateHistogramInterval] must not be null");
        }
        this.dateHistogramInterval = dateHistogramInterval;
        return this;
    }

    public DateHistogramValuesSourceBuilder timeZone(DateTimeZone timeZone) {
        if (timeZone == null) {
            throw new IllegalArgumentException("[timeZone] must not be null: [" + name + "]");
        }
        this.timeZone = timeZone;
        return this;
    }

    @Override
    protected void doXContentBody(XContentBuilder builder) throws IOException {
        if (dateHistogramInterval == null) {
            builder.field(INTERVAL_FIELD.getPreferredName(), interval);
        } else {
            builder.field(INTERVAL_FIELD.getPreferredName(), dateHistogramInterval.toString());
        }
        if (timeZone != null) {
            builder.field("time_zone", timeZone);
        }
    }

    @Override
    public String type() {
        return TYPE;
    }

}
