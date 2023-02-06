package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.Strings;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.Query;

import java.io.IOException;

/**
 * 通用符查询
 * 支持的通配符是*匹配任何字符序列（包括空字符）和?匹配任何单个字符的通配符
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/query-dsl-range-query.html
 *
 * @author daiyongjun
 */
public class WildcardQueryBuilder extends AbstractQueryBuilder<WildcardQueryBuilder> implements MultiTermQueryBuilder {
    public static final String NAME = "wildcard";

    private static final ParseField WILDCARD_FIELD = new ParseField("wildcard");
    private static final ParseField VALUE_FIELD = new ParseField("value");
    private static final ParseField REWRITE_FIELD = new ParseField("rewrite");

    private final String fieldName;

    private final String value;

    private String rewrite;

    public WildcardQueryBuilder(String fieldName, String value) {
        if (Strings.isEmpty(fieldName)) {
            throw new IllegalArgumentException("field name is null or empty");
        }
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        this.fieldName = fieldName;
        this.value = value;
    }

    public WildcardQueryBuilder rewrite(String rewrite) {
        this.rewrite = rewrite;
        return this;
    }

    public static WildcardQueryBuilder fromXContent(XContentParser parser,Object Context) throws IOException {
        String fieldName = null;
        String rewrite = null;
        String value = null;
        float boost = DEFAULT_BOOST;
        String queryName = null;
        String currentFieldName = null;
        XContentParser.Token token;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.START_OBJECT) {
                throwParsingExceptionOnMultipleFields(NAME, parser.getTokenLocation(), fieldName, currentFieldName);
                fieldName = currentFieldName;
                while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                    if (token == XContentParser.Token.FIELD_NAME) {
                        currentFieldName = parser.currentName();
                    } else {
                        if (WILDCARD_FIELD.match(currentFieldName)) {
                            value = parser.text();
                        } else if (VALUE_FIELD.match(currentFieldName)) {
                            value = parser.text();
                        } else if (BOOST_FIELD.match(currentFieldName)) {
                            boost = parser.floatValue();
                        } else if (REWRITE_FIELD.match(currentFieldName)) {
                            rewrite = parser.textOrNull();
                        } else if (NAME_FIELD.match(currentFieldName)) {
                            queryName = parser.text();
                        } else {
                            throw new ParsingException(parser.getTokenLocation(),
                                    "[wildcard] query does not support [" + currentFieldName + "]");
                        }
                    }
                }
            } else {
                throwParsingExceptionOnMultipleFields(NAME, parser.getTokenLocation(), fieldName, parser.currentName());
                fieldName = parser.currentName();
                value = parser.text();
            }
        }

        return new WildcardQueryBuilder(fieldName, value)
                .rewrite(rewrite)
                .boost(boost)
                .queryName(queryName);
    }

    @Override
    protected void doXContent(XContentBuilder builder) throws IOException {
        builder.startObject(NAME);
        builder.startObject(fieldName);
        builder.field(WILDCARD_FIELD.getPreferredName(), value);
        if (rewrite != null) {
            builder.field(REWRITE_FIELD.getPreferredName(), rewrite);
        }
        printBoostAndQueryName(builder);
        builder.endObject();
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
