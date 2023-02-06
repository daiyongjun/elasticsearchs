package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.sort;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;

import java.io.IOException;

/**
 * 基于分数(Score)排序
 *
 * @author daiyongjun
 */
public class ScoreSortBuilder extends SortBuilder<ScoreSortBuilder> {
    public static final String NAME = "_score";

    public ScoreSortBuilder() {
        order(SortOrder.DESC);
    }


    private static ObjectParser<Void, ScoreSortBuilder> PARSER = new ObjectParser<>(NAME, ScoreSortBuilder::new);

    static {
        PARSER.declareString((builder, order) -> builder.order(SortOrder.fromString(order)), ORDER_FIELD);
    }

    public static ScoreSortBuilder fromXContent(XContentParser parser, String fieldName) {
        return PARSER.apply(parser, null);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        builder.startObject();
        builder.startObject(NAME);
        builder.field(ORDER_FIELD.getPreferredName(), order);
        builder.endObject();
        builder.endObject();
        return builder;
    }



}
