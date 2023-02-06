package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.Strings;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ToXContent;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Aggregations构建类（Builder）List<AggregationBuilder>组成
 *
 * @author daiyongjun
 */
public class AggregationsBuilder implements ToXContent {

    private final Set<String> names = new HashSet<>();

    private final List<AggregationBuilder> aggregationBuilders = new ArrayList<>();

    /**
     * 获取AggregationsBuilder内含 aggregationBuilders 的长度
     *
     * @return int
     */
    public int count() {
        return aggregationBuilders.size();
    }

    /**
     * AggregationsBuilder 添加 AggregationBuilder
     *
     * @param aggregationBuilder 待添加的 AggregationBuilder
     * @return AggregationsBuilder
     */
    public AggregationsBuilder addAggregationBuilder(AggregationBuilder aggregationBuilder) {
        if (!names.add(aggregationBuilder.name)) {
            throw new IllegalArgumentException("Two sibling aggregations cannot have the same name: [" + aggregationBuilder.name + "]");
        }
        aggregationBuilders.add(aggregationBuilder);
        return this;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        builder.startObject();
        for (AggregationBuilder subAgg : aggregationBuilders) {
            subAgg.toXContent(builder);
        }
        builder.endObject();
        return builder;
    }


    @Override
    public String toString() {
        return Strings.toString(this);
    }
}
