package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.Query;

import java.io.IOException;

/**
 * 基于term的query类
 *
 * @author daiyongjun
 */
public class TermQueryBuilder extends BaseTermQueryBuilder<TermQueryBuilder> {
    /**
     * Term的query类的名称
     */
    public static final String NAME = "term";
    /**
     * 基于ParseField 定义字段名称
     */
    private static final ParseField TERM_FIELD = new ParseField("term");


    public TermQueryBuilder(String fieldName, Object value) {
        super(fieldName, value);
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

    /**
     * XContentParser 解析成 TermQueryBuilder核心处理类
     *
     * @param parser XContentParser
     * @return TermQueryBuilder
     * @throws IOException XContentParser -> Pojo 过程出现的异常情况
     */
    public static TermQueryBuilder fromXContent(XContentParser parser, Object Context) throws IOException {
        String queryName = null;
        String fieldName = null;
        Object value = null;
        float boost = DEFAULT_BOOST;
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
                        if (TERM_FIELD.match(currentFieldName)) {
                            value = parser.objectText();
                        } else if (VALUE_FIELD.match(currentFieldName)) {
                            value = parser.objectText();
                        } else if (NAME_FIELD.match(currentFieldName)) {
                            queryName = parser.text();
                        } else if (BOOST_FIELD.match(currentFieldName)) {
                            boost = parser.floatValue();
                        } else {
                            throw new ParsingException(parser.getTokenLocation(),
                                    "[term] query does not support [" + currentFieldName + "]");
                        }
                    }
                }
            } else if (token.isValue()) {
                throwParsingExceptionOnMultipleFields(NAME, parser.getTokenLocation(), fieldName, parser.currentName());
                fieldName = currentFieldName;
                value = parser.objectText();
            } else if (token == XContentParser.Token.START_ARRAY) {
                throw new ParsingException(parser.getTokenLocation(), "[term] query does not support array of values");
            }
        }
        TermQueryBuilder termQuery = new TermQueryBuilder(fieldName, value);
        termQuery.boost(boost);
        if (queryName != null) {
            termQuery.queryName(queryName);
        }
        return termQuery;
    }
}
