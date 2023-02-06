package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.adjacency;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AggregationBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 邻接矩阵：为了方便理解，我举一个指标的例子select stats(count) as 阅量 from es.app ;stats 实际是会基于count，生成阅量.count、阅量.sum、阅量.avg、阅量.min、阅量.max等聚合数据
 * 邻接矩阵则是会基于多个filter，组合bucket条件如：
 * {
 * "size": 0,
 * "aggs" : {
 * "interactions" : {
 * "adjacency_matrix" : {
 * "filters" : {
 * "grpA" : { "terms" : { "accounts" : ["hillary", "sidney"] }},
 * "grpB" : { "terms" : { "accounts" : ["donald", "mitt"] }},
 * "grpC" : { "terms" : { "accounts" : ["vladimir", "nigel"] }}
 * }
 * }
 * }
 * }
 * }
 * <p>
 * 生成的的bucket桶信息为bucketA、bucketB、bucketA&B、bucketA&C、bucketB&C、bucketC
 * |      | A    | B    | C    |
 * | ---- | ---- | ---- | ---- |
 * | A    | A    | A&B  | A&C  |
 * | B    | B    |      | B&C  |
 * | C    |      |      | C    |
 * 去掉相关重复的数据
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-bucket-adjacency-matrix-aggregation.html
 *
 * @author daiyongjun
 */
public class AdjacencyMatrixAggregationBuilder extends AbstractAggregationBuilder<AdjacencyMatrixAggregationBuilder> {
    public static final String NAME = "adjacency_matrix";

    private static final String DEFAULT_SEPARATOR = "&";
    private static final ParseField SEPARATOR_FIELD = new ParseField("separator");
    private static final ParseField FILTERS_FIELD = new ParseField("filters");
    private List<KeyedFilter> filters;
    private String separator = DEFAULT_SEPARATOR;

    private static final ObjectParser<Void, AdjacencyMatrixAggregationBuilder> PARSER = new ObjectParser<>(
            AdjacencyMatrixAggregationBuilder.NAME);

    static {
        PARSER.declareString(AdjacencyMatrixAggregationBuilder::separator, SEPARATOR_FIELD);
        PARSER.declareNamedObjects(AdjacencyMatrixAggregationBuilder::setFiltersAsList, KeyedFilter.PARSER, FILTERS_FIELD);
    }

    /**
     * 构造一个新的聚合构建器。
     *
     * @param name 当前聚合的名称
     */
    protected AdjacencyMatrixAggregationBuilder(String name) {
        super(name);
    }


    public static AggregationBuilder parse(String aggregationName, XContentParser parser) throws IOException {
        AdjacencyMatrixAggregationBuilder result = PARSER.parse(parser, null, new AdjacencyMatrixAggregationBuilder(aggregationName));
        result.checkConsistency();
        return result;
    }

    public AdjacencyMatrixAggregationBuilder separator(String separator) {
        if (separator == null) {
            throw new IllegalArgumentException("[separator] must not be null: [" + name + "]");
        }
        this.separator = separator;
        return this;
    }

    protected void setFiltersAsList(List<KeyedFilter> filters) {
        this.filters = new ArrayList<>(filters);
        // internally we want to have a fixed order of filters, regardless of
        // the order of the filters in the request
        Collections.sort(this.filters, Comparator.comparing(KeyedFilter::key));
    }

    protected void checkConsistency() {
        if ((filters == null) || (filters.size() == 0)) {
            throw new IllegalStateException("[" + name + "] is missing : " + FILTERS_FIELD.getPreferredName() + " parameter");
        }
    }

    @Override
    protected XContentBuilder internalXContent(XContentBuilder builder) throws IOException {
        builder.startObject();
        builder.field(SEPARATOR_FIELD.getPreferredName(), separator);
        builder.startObject(FILTERS_FIELD.getPreferredName());
        for (KeyedFilter keyedFilter : filters) {
            builder.field(keyedFilter.key(), keyedFilter.filter());
        }
        builder.endObject();
        builder.endObject();
        return builder;
    }

    @Override
    public String getType() {
        return NAME;
    }
}
