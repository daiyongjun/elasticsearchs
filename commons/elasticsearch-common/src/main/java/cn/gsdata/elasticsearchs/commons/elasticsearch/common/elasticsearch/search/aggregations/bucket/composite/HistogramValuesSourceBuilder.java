package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.composite;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.support.ValueType;

import java.io.IOException;

public class HistogramValuesSourceBuilder extends CompositeValuesSourceBuilder<HistogramValuesSourceBuilder> {
    static final String TYPE = "histogram";
    private static final ObjectParser<Void, HistogramValuesSourceBuilder> PARSER;
    public static final ParseField INTERVAL_FIELD = new ParseField("interval");

    private double interval = 0;

    static {
        PARSER = new ObjectParser<>(HistogramValuesSourceBuilder.TYPE);
        PARSER.declareDouble(HistogramValuesSourceBuilder::interval, INTERVAL_FIELD);
        declareValuesSourceFields(PARSER, ValueType.NUMERIC);
    }

    HistogramValuesSourceBuilder(String name) {
        super(name);
    }

    static HistogramValuesSourceBuilder parse(String name, XContentParser parser) throws IOException {
        return PARSER.parse(parser, null, new HistogramValuesSourceBuilder(name));
    }


    public HistogramValuesSourceBuilder interval(double interval) {
        if (interval <= 0) {
            throw new IllegalArgumentException("[interval] must be greater than 0 for [histogram] source");
        }
        this.interval = interval;
        return this;
    }

    @Override
    protected void doXContentBody(XContentBuilder builder) throws IOException {
        builder.field(INTERVAL_FIELD.getPreferredName(), interval);
    }

    @Override
    String type() {
        return TYPE;
    }
}
