package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.histogram;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.CheckedFunction;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.*;

import java.io.IOException;

/**
 * 日期直方图聚合桶builder类的属性内容extendedBounds类
 *
 * @author daiyongjun
 */
public class ExtendedBounds implements ToXContent {
    static final ParseField EXTENDED_BOUNDS_FIELD = new ParseField("extended_bounds");
    static final ParseField MIN_FIELD = new ParseField("min");
    static final ParseField MAX_FIELD = new ParseField("max");

    /**
     * ExtendedBounds设置的最小值（Long）
     */
    private final Long min;
    /**
     * ExtendedBounds设置的最大值（Long）
     */
    private final Long max;
    /**
     * ExtendedBounds设置的最小值（String）
     */
    private final String minAsStr;
    /**
     * ExtendedBounds设置的最大值（String）
     */
    private final String maxAsStr;

    /**
     * extendedBounds类构造方法，设置为min(Long), max(Long)
     */
    public ExtendedBounds(Long min, Long max) {
        this(min, max, null, null);
    }

    /**
     * extendedBounds类构造方法，设置为minAsStr(String),maxAsStr(String)
     */
    public ExtendedBounds(String minAsStr, String maxAsStr) {
        this(null, null, minAsStr, maxAsStr);
    }

    /**
     * @param min      ExtendedBounds设置的最小值（Long）
     * @param max      ExtendedBounds设置的最大值（Long）
     * @param minAsStr ExtendedBounds设置的最小值（String）
     * @param maxAsStr ExtendedBounds设置的最大值（String）
     */
    private ExtendedBounds(Long min, Long max, String minAsStr, String maxAsStr) {
        this.min = min;
        this.max = max;
        this.minAsStr = minAsStr;
        this.maxAsStr = maxAsStr;
    }

    public static final ConstructingObjectParser<Void, ExtendedBounds> PARSER = new ConstructingObjectParser<>(EXTENDED_BOUNDS_FIELD.getPreferredName(), a -> {
        assert a.length == 2;
        Long min = null;
        Long max = null;
        String minAsStr = null;
        String maxAsStr = null;
        if (a[0] == null) {
            // nothing to do with it
        } else if (a[0] instanceof Long) {
            min = (Long) a[0];
        } else if (a[0] instanceof String) {
            minAsStr = (String) a[0];
        } else {
            throw new IllegalArgumentException("Unknown field type [" + a[0].getClass() + "]");
        }
        if (a[1] == null) {
            // nothing to do with it
        } else if (a[1] instanceof Long) {
            max = (Long) a[1];
        } else if (a[1] instanceof String) {
            maxAsStr = (String) a[1];
        } else {
            throw new IllegalArgumentException("Unknown field type [" + a[1].getClass() + "]");
        }
        return new ExtendedBounds(min, max, minAsStr, maxAsStr);
    });

    static {
        CheckedFunction<XContentParser, Object, IOException> longOrString = p -> {
            if (p.currentToken() == XContentParser.Token.VALUE_NUMBER) {
                return p.longValue(false);
            }
            if (p.currentToken() == XContentParser.Token.VALUE_STRING) {
                return p.text();
            }
            if (p.currentToken() == XContentParser.Token.VALUE_NULL) {
                return null;
            }
            throw new IllegalArgumentException("Unsupported token [" + p.currentToken() + "]");
        };
        PARSER.declareField(ConstructingObjectParser.optionalConstructorArg(), longOrString, MIN_FIELD, ObjectParser.ValueType.LONG_OR_NULL);
        PARSER.declareField(ConstructingObjectParser.optionalConstructorArg(), longOrString, MAX_FIELD, ObjectParser.ValueType.LONG_OR_NULL);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        builder.startObject(EXTENDED_BOUNDS_FIELD.getPreferredName());
        if (min != null) {
            builder.field(MIN_FIELD.getPreferredName(), min);
        } else {
            builder.field(MIN_FIELD.getPreferredName(), minAsStr);
        }
        if (max != null) {
            builder.field(MAX_FIELD.getPreferredName(), max);
        } else {
            builder.field(MAX_FIELD.getPreferredName(), maxAsStr);
        }
        builder.endObject();
        return builder;
    }
}
