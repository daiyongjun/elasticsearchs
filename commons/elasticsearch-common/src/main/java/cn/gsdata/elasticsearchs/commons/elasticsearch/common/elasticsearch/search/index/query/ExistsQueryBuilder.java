package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.Strings;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.Query;

import java.io.IOException;

/**
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/query-dsl-exists-query.html
 *
 * @author daiyongjun
 */
public class ExistsQueryBuilder extends AbstractQueryBuilder<ExistsQueryBuilder> {

    public static final String NAME = "exists";

    public static final ParseField FIELD_FIELD = new ParseField("field");

    private final String fieldName;

    public ExistsQueryBuilder(String fieldName) {
        if (Strings.isEmpty(fieldName)) {
            throw new IllegalArgumentException("field name is null or empty");
        }
        this.fieldName = fieldName;
    }

    public String fieldName() {
        return this.fieldName;
    }

    public static ExistsQueryBuilder fromXContent(XContentParser parser, Object Context) throws IOException {
        String fieldPattern = null;
        String queryName = null;
        float boost = DEFAULT_BOOST;

        XContentParser.Token token;
        String currentFieldName = null;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token.isValue()) {
                if (FIELD_FIELD.match(currentFieldName)) {
                    fieldPattern = parser.text();
                } else if (NAME_FIELD.match(currentFieldName)) {
                    queryName = parser.text();
                } else if (BOOST_FIELD.match(currentFieldName)) {
                    boost = parser.floatValue();
                } else {
                    throw new ParsingException(parser.getTokenLocation(), "[" + ExistsQueryBuilder.NAME +
                            "] query does not support [" + currentFieldName + "]");
                }
            } else {
                throw new ParsingException(parser.getTokenLocation(), "[" + ExistsQueryBuilder.NAME +
                        "] unknown token [" + token + "] after [" + currentFieldName + "]");
            }
        }

        if (fieldPattern == null) {
            throw new ParsingException(parser.getTokenLocation(), "[" + ExistsQueryBuilder.NAME + "] must be provided with a [field]");
        }

        ExistsQueryBuilder builder = new ExistsQueryBuilder(fieldPattern);
        builder.queryName(queryName);
        builder.boost(boost);
        return builder;
    }

    @Override
    protected void doXContent(XContentBuilder builder) throws IOException {
        builder.startObject(NAME);
        builder.field(FIELD_FIELD.getPreferredName(), fieldName);
        printBoostAndQueryName(builder);
        builder.endObject();
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
        return NAME;
    }
}
