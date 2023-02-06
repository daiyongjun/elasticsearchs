package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AggregationBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AggregationBuilderParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query.QueryBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query.QueryParser;

public interface SearchPlugin {

    /**
     * 定义搜索扩展Spec（搜索内置query和analysis）
     *
     * @param <P>
     * @author daiyongjun
     */
    class SearchExtensionSpec<P> {
        private final ParseField name;
        private final P parser;

        public SearchExtensionSpec(ParseField name, P parser) {
            this.name = name;
            this.parser = parser;
        }

        public SearchExtensionSpec(String name, P parser) {
            this(new ParseField(name), parser);
        }

        public ParseField getName() {
            return name;
        }

        public P getParser() {
            return parser;
        }
    }

    /**
     * 查询的Spec类
     *
     * @param <QB> 定义基础的泛型，约束属性类型
     * @author daiyongjun
     */
    class QuerySpec<QB extends QueryBuilder> extends SearchExtensionSpec<QueryParser<QB>> {

        public QuerySpec(String name, QueryParser<QB> parser) {
            super(name, parser);
        }
    }

    /**
     * 聚合的Spec类
     *
     * @param <AB> 定义基础的泛型，约束属性类型
     * @author daiyongjun
     */
    class AggregationSpec<AB extends AggregationBuilder> extends SearchExtensionSpec<AggregationBuilderParser<AB>> {
        public AggregationSpec(String name, AggregationBuilderParser<AB> parser) {
            super(name, parser);
        }
    }
}
