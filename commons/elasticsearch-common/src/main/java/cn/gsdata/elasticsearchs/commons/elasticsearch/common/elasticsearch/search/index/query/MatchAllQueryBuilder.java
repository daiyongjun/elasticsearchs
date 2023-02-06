package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.Query;

import java.io.IOException;

/**
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/query-dsl-match-all-query.html
 *
 * @author daiyongjun
 */
public class MatchAllQueryBuilder extends AbstractQueryBuilder<MatchAllQueryBuilder> {
    public static final String NAME = "match_all";

    private static final ObjectParser<Void, MatchAllQueryBuilder> PARSER = new ObjectParser<>(NAME, MatchAllQueryBuilder::new);

    static {
        PARSER.declareFloat(QueryBuilder::boost, BOOST_FIELD);
        PARSER.declareString(QueryBuilder::queryName, NAME_FIELD);
    }

    public MatchAllQueryBuilder() {
    }


    public static MatchAllQueryBuilder fromXContent(XContentParser parser, Object Context) {
        try {
            return PARSER.apply(parser, null);
        } catch (IllegalArgumentException e) {
            throw new ParsingException(parser.getTokenLocation(), e.getMessage(), e);
        }
    }

    @Override
    protected void doXContent(XContentBuilder builder) throws IOException {
        builder.startObject(NAME);
        printBoostAndQueryName(builder);
        builder.endObject();
    }

    @Override
    public Query toQuery(QueryShardContext context) throws IOException {
        //todo
        return null;
    }

    @Override
    public Query toFilter(QueryShardContext context) throws IOException {
        //todo
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
