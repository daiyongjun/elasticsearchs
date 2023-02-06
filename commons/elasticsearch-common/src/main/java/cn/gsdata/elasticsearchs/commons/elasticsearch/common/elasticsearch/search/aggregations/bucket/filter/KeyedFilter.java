package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.filter;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ToXContent;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query.QueryBuilder;

import java.io.IOException;

public class KeyedFilter implements ToXContent {
    private final String key;
    private final QueryBuilder filter;

    public KeyedFilter(String key, QueryBuilder filter) {
        if (key == null) {
            throw new IllegalArgumentException("[key] must not be null");
        }
        if (filter == null) {
            throw new IllegalArgumentException("[filter] must not be null");
        }
        this.key = key;
        this.filter = filter;
    }


    public String key() {
        return key;
    }

    public QueryBuilder filter() {
        return filter;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        builder.field(key, filter);
        return builder;
    }
}
