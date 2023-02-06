package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.composite;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AbstractAggregationBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-bucket-composite-aggregation.html
 *
 * @author daiyongjun
 */
public class CompositeAggregationBuilder extends AbstractAggregationBuilder<CompositeAggregationBuilder> {
    public static final String NAME = "composite";

    public static final ParseField AFTER_FIELD_NAME = new ParseField("after");
    public static final ParseField SIZE_FIELD_NAME = new ParseField("size");
    public static final ParseField SOURCES_FIELD_NAME = new ParseField("sources");

    private Map<String, Object> after;
    private int size = 10;
    private List<CompositeValuesSourceBuilder<?>> sources;

    private static final ObjectParser<Void, CompositeAggregationBuilder> PARSER;

    static {
        PARSER = new ObjectParser<>(NAME);
        PARSER.declareInt(CompositeAggregationBuilder::size, SIZE_FIELD_NAME);
        PARSER.declareObject(CompositeAggregationBuilder::aggregateAfter, (parser, context) -> parser.map(), AFTER_FIELD_NAME);
        PARSER.declareObjectArray(CompositeAggregationBuilder::setSources,
                (p, c) -> CompositeValuesSourceBuilder.fromXContent(p), SOURCES_FIELD_NAME);
    }

    public static CompositeAggregationBuilder parse(String aggregationName, XContentParser parser) throws IOException {
        return PARSER.parse(parser, null, new CompositeAggregationBuilder(aggregationName));
    }

    /**
     * 构造一个新的聚合构建器。
     *
     * @param name 当前聚合的名称
     */
    protected CompositeAggregationBuilder(String name) {
        super(name);
    }

    public CompositeAggregationBuilder size(int size) {
        this.size = size;
        return this;
    }

    public CompositeAggregationBuilder aggregateAfter(Map<String, Object> afterKey) {
        this.after = afterKey;
        return this;
    }


    private CompositeAggregationBuilder setSources(List<CompositeValuesSourceBuilder<?>> sources) {
        this.sources = sources;
        return this;
    }

    @Override
    protected XContentBuilder internalXContent(XContentBuilder builder) throws IOException {
        builder.startObject();
        builder.field(SIZE_FIELD_NAME.getPreferredName(), size);
        builder.startArray(SOURCES_FIELD_NAME.getPreferredName());
        for (CompositeValuesSourceBuilder<?> source : sources) {
            builder.startObject();
            builder.startObject(source.name());
            source.toXContent(builder);
            builder.endObject();
            builder.endObject();
        }
        builder.endArray();
        if (after != null) {
            CompositeAggregationBuilder.buildCompositeMap(AFTER_FIELD_NAME.getPreferredName(), after, builder);
        }
        builder.endObject();
        return builder;
    }

    static void buildCompositeMap(String fieldName, Map<String, Object> composite, XContentBuilder builder) throws IOException {
        builder.startObject(fieldName);
        for (Map.Entry<String, Object> entry : composite.entrySet()) {
            builder.field(entry.getKey(), entry.getValue());
        }
        builder.endObject();
    }

    @Override
    public String getType() {
        return NAME;
    }
}
