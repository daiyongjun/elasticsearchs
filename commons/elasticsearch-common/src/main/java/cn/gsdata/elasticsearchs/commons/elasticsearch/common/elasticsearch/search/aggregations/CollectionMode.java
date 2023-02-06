package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.ElasticsearchException;

/**
 * CollectionMode相关属性
 *
 * @author daiyongjun
 */
public enum CollectionMode {

    //参考官方文档，es内存优化，CollectionMode设置https://www.elastic.co/guide/en/elasticsearch/reference/7.3/search-aggregations-bucket-terms-aggregation.html
    DEPTH_FIRST(new ParseField("depth_first")),
    BREADTH_FIRST(new ParseField("breadth_first"));

    public static final ParseField KEY = new ParseField("collect_mode");

    private final ParseField parseField;

    CollectionMode(ParseField parseField) {
        this.parseField = parseField;
    }

    public ParseField parseField() {
        return parseField;
    }

    public static CollectionMode parse(String value) {
        CollectionMode[] modes = CollectionMode.values();
        for (CollectionMode mode : modes) {
            if (mode.parseField.match(value)) {
                return mode;
            }
        }
        throw new ElasticsearchException("no [{}] found for value [{}]", KEY.getPreferredName(), value);
    }
}
