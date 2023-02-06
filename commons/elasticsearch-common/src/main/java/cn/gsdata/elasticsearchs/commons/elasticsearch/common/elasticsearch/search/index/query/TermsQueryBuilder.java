package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.Strings;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于terms的query类
 *
 * @author daiyongjun
 */
public class TermsQueryBuilder extends AbstractQueryBuilder<TermsQueryBuilder> {
    /**
     * Terms的query类的名称
     */
    public static final String NAME = "terms";
    /**
     * 字段名称
     */
    private final String fieldName;
    /**
     * terms对应字段的值的列表
     */
    private final List<?> values;

    public TermsQueryBuilder(String fieldName, List<?> values) {
        if (Strings.isEmpty(fieldName)) {
            throw new IllegalArgumentException("field name cannot be null.");
        }
        if (values == null) {
            throw new IllegalArgumentException("No value  specified for terms query");
        }
        this.fieldName = fieldName;
        this.values = values;
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
     * XContentParser 解析成 TermsQueryBuilder核心处理类
     *
     * @param parser XContentParser
     * @return TermsQueryBuilder
     * @throws IOException XContentParser -> Pojo 过程出现的异常情况
     */
    public static TermsQueryBuilder fromXContent(XContentParser parser, Object Context) throws IOException {
        String fieldName = null;
        List<Object> values = null;
        String queryName = null;
        float boost = DEFAULT_BOOST;
        XContentParser.Token token;
        String currentFieldName = null;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.START_ARRAY) {
                if (fieldName != null) {
                    throw new ParsingException(parser.getTokenLocation(),
                            "[" + TermsQueryBuilder.NAME + "] query does not support multiple fields");
                }
                fieldName = currentFieldName;
                values = parseValues(parser);
            } else if (token == XContentParser.Token.START_OBJECT) {
                if (fieldName != null) {
                    throw new ParsingException(parser.getTokenLocation(),
                            "[" + TermsQueryBuilder.NAME + "] query does not support more than one field. "
                                    + "Already got: [" + fieldName + "] but also found [" + currentFieldName + "]");
                }
                fieldName = currentFieldName;
            } else if (token.isValue()) {
                if (BOOST_FIELD.match(currentFieldName)) {
                    boost = parser.floatValue();
                } else if (NAME_FIELD.match(currentFieldName)) {
                    queryName = parser.text();
                } else {
                    throw new ParsingException(parser.getTokenLocation(),
                            "[" + TermsQueryBuilder.NAME + "] query does not support [" + currentFieldName + "]");
                }
            } else {
                throw new ParsingException(parser.getTokenLocation(),
                        "[" + TermsQueryBuilder.NAME + "] unknown token [" + token + "] after [" + currentFieldName + "]");
            }
        }

        if (fieldName == null) {
            throw new ParsingException(parser.getTokenLocation(), "[" + TermsQueryBuilder.NAME + "] query requires a field name, " +
                    "followed by array of terms or a document lookup specification");
        }
        return new TermsQueryBuilder(fieldName, values)
                .boost(boost)
                .queryName(queryName);
    }

    /**
     * 从Parser中解析出terms对应的values的值
     *
     * @param parser 数据解析器
     * @return List<Object>
     * @throws IOException  解析terms对应的values的值异常
     */
    static List<Object> parseValues(XContentParser parser) throws IOException {
        List<Object> values = new ArrayList<>();
        while (parser.nextToken() != XContentParser.Token.END_ARRAY) {
            Object value = parser.objectText();
            if (value == null) {
                throw new ParsingException(parser.getTokenLocation(), "No value specified for terms query");
            }
            values.add(value);
        }
        return values;
    }




    /**
     * Builder中写入BaseTermQuery(pojo)的数据，Builder.append(参数) 将pojo->Xcontent
     *
     * @param builder XContentBuilder
     * @throws IOException Builder中append(参数)异常
     */
    @Override
    protected void doXContent(XContentBuilder builder) throws IOException {
        builder.startObject(NAME);
        builder.field(fieldName, values);
        printBoostAndQueryName(builder);
        builder.endObject();
    }
}
