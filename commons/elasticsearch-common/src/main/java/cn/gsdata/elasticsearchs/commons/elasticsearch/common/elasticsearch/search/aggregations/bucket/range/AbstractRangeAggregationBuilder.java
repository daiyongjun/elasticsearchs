package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.range;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 定义抽象的range桶类
 *
 * @author daiyongjun
 */
public abstract class AbstractRangeAggregationBuilder<AB extends AbstractRangeAggregationBuilder<AB, R>, R extends RangeBuilder> extends ValuesSourceAggregationBuilder<AB> {

    public static final ParseField RANGES_FIELD = new ParseField("ranges");
    public static final ParseField KEYED_FIELD = new ParseField("keyed");

    /**
     * 内置多个ranges
     * ranges : [{ "key": "average", "from": 100, "to": 200 },...]
     */
    protected List<R> ranges = new ArrayList<>();
    /**
     * 将keyed标志设置为true，设置buckets返回的值为哈希buckets{key1:{},key2:{}};keyed标志为false,设置buckets返回是数组buckets:[{key:xxx},{key:xx}]
     */
    protected boolean keyed = false;

    /**
     * AbstractRangeBuilder构造方法
     *
     * @param name 当前聚合的名称
     */
    protected AbstractRangeAggregationBuilder(String name) {
        super(name);
    }

    /**
     * 设置  keyed 属性的值
     *
     * @param keyed 将keyed的值
     * @return AB
     */
    public AB keyed(boolean keyed) {
        this.keyed = keyed;
        return (AB) this;
    }

    /**
     * 为属性ranges添加range
     *
     * @param range 待添加的range
     * @return AB
     */
    public AB addRange(R range) {
        if (range == null) {
            throw new IllegalArgumentException("[range] must not be null: [" + name + "]");
        }
        ranges.add(range);
        return (AB) this;
    }


    @Override
    protected XContentBuilder doXContentBody(XContentBuilder builder) throws IOException {
        builder.field(RANGES_FIELD.getPreferredName(), ranges);
        builder.field(KEYED_FIELD.getPreferredName(), keyed);
        return builder;
    }
}
