package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.Query;

import java.io.IOException;

/**
 * 嵌套查询,支持查询嵌套类型的对象或者文档，其中嵌套对象和文档的定义
 * {
 * "mappings": {
 * "type1" : {
 * "properties" : {
 * "obj1" : {
 * "type" : "nested"
 * }
 * }
 * }
 * }
 * }
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/query-dsl-nested-query.html
 *
 * @author daiyongjun
 */
public class NestedQueryBuilder extends AbstractQueryBuilder<NestedQueryBuilder> {
    public static final String NAME = "nested";

    public static final boolean DEFAULT_IGNORE_UNMAPPED = false;

    private static final ParseField SCORE_MODE_FIELD = new ParseField("score_mode");
    private static final ParseField PATH_FIELD = new ParseField("path");
    private static final ParseField QUERY_FIELD = new ParseField("query");
    private static final ParseField INNER_HITS_FIELD = new ParseField("inner_hits");
    private static final ParseField IGNORE_UNMAPPED_FIELD = new ParseField("ignore_unmapped");

    ///**
    // * 查询path指向嵌套对象路径
    // */
    //private final String path;
    ///**
    // * score_mode 允许设置内部子级匹配如何影响父级的评分，默认为avg、可以是sum、min、max none
    // */
    //private final ScoreMode scoreMode;
    ///**
    // *
    // */
    //private final QueryBuilder query;


    private InnerHitBuilder innerHitBuilder;
    private boolean ignoreUnmapped = DEFAULT_IGNORE_UNMAPPED;

    @Override
    protected void doXContent(XContentBuilder builder) throws IOException {

    }

    @Override
    public Query toQuery(QueryShardContext context) throws IOException {
        return null;
    }

    @Override
    public Query toFilter(QueryShardContext context) throws IOException {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
