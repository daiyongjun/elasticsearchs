package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.Query;

import java.io.IOException;

/**
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/query-dsl-match-query-phrase-prefix.html#query-dsl-match-query-phrase-prefix
 * 短语前缀查询，这个查询是短语查询+前缀匹配，除去最后一个词是短语匹配，最后一个词上进行前缀匹配。
 *
 * @author daiyongjun
 */
public class MatchPhrasePrefixQueryBuilder  extends AbstractQueryBuilder<MatchPhrasePrefixQueryBuilder>{
    public static final String NAME = "match_phrase_prefix";
    /**
     *
     */
    public static final ParseField MAX_EXPANSIONS_FIELD = new ParseField("max_expansions");



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
