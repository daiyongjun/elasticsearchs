package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.script.Script;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 参考文档: https://www.elastic.co/guide/en/elasticsearch/reference/6.1/query-dsl-terms-set-query.html
 * TermsSetQuery 术语集查询，该查询是一个新查询后续版本可能会有所变更
 * TermsSetQuery 与 TermsQuery 不同的地方增加了minimum_should_match 等参数，支持配置匹配terms参数内容的比例
 * 注意：minimum_should_match_{field|script} 6.1版本中并不是int 类型而是String类型，该值是写在document中的如：
 * PUT /my-index
 * {
 * "mappings": {
 * "doc": {
 * "properties": {
 * "required_matches": {
 * "type": "long"
 * }
 * }
 * }
 * }
 * }
 * PUT /my-index/doc/1?refresh
 * {
 * "codes": ["ghi", "jkl"],
 * "required_matches": 2
 * }
 * PUT /my-index/doc/2?refresh
 * {
 * "codes": ["def", "ghi"],
 * "required_matches": 2
 * }
 *
 * @author daiyongjun
 */
public class TermsSetQueryBuilder extends AbstractQueryBuilder<TermsSetQueryBuilder> {
    /**
     * TermsSetQuery的名称
     */
    public static final String NAME = "terms_set";

    static final ParseField TERMS_FIELD = new ParseField("terms");
    static final ParseField MINIMUM_SHOULD_MATCH_FIELD = new ParseField("minimum_should_match_field");
    static final ParseField MINIMUM_SHOULD_MATCH_SCRIPT = new ParseField("minimum_should_match_script");
    /**
     * 术语集查询的字段的名称
     */
    private final String fieldName;
    /**
     * 术语集待查询的values值
     */
    private final List<?> values;
    /**
     * Mapping中minimumShouldMatch的字段
     */
    private String minimumShouldMatchField;
    /**
     * Mapping中minimumShouldMatch的脚本数据
     */
    private Script minimumShouldMatchScript;

    public TermsSetQueryBuilder(String fieldName, List<?> values) {
        this.fieldName = Objects.requireNonNull(fieldName);
        this.values = Objects.requireNonNull(values);
    }

    /**
     * 设置Mapping中minimumShouldMatch的字段
     *
     * @param minimumShouldMatchField String
     * @return TermsSetQueryBuilder
     */
    public TermsSetQueryBuilder setMinimumShouldMatchField(String minimumShouldMatchField) {
        if (minimumShouldMatchScript != null) {
            throw new IllegalArgumentException("A script has already been specified. Cannot specify both a field and script");
        }
        this.minimumShouldMatchField = minimumShouldMatchField;
        return this;
    }

    /**
     * Mapping中minimumShouldMatch的脚本数据
     *
     * @param minimumShouldMatchScript Script
     * @return TermsSetQueryBuilder
     */
    public TermsSetQueryBuilder setMinimumShouldMatchScript(Script minimumShouldMatchScript) {
        if (minimumShouldMatchField != null) {
            throw new IllegalArgumentException("A field has already been specified. Cannot specify both a field and script");
        }
        this.minimumShouldMatchScript = minimumShouldMatchScript;
        return this;
    }


    public static TermsSetQueryBuilder fromXContent(XContentParser parser, Object Context) throws IOException {
        XContentParser.Token token = parser.nextToken();
        if (token != XContentParser.Token.FIELD_NAME) {
            throw new ParsingException(parser.getTokenLocation(), "[" + NAME + "] unknown token [" + token + "]");
        }
        String currentFieldName = parser.currentName();
        String fieldName = currentFieldName;

        token = parser.nextToken();
        if (token != XContentParser.Token.START_OBJECT) {
            throw new ParsingException(parser.getTokenLocation(), "[" + NAME + "] unknown token [" + token + "]");
        }

        List<Object> values = new ArrayList<>();
        String minimumShouldMatchField = null;
        Script minimumShouldMatchScript = null;
        String queryName = null;
        float boost = DEFAULT_BOOST;

        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.START_ARRAY) {
                if (TERMS_FIELD.match(currentFieldName)) {
                    values = TermsQueryBuilder.parseValues(parser);
                } else {
                    throw new ParsingException(parser.getTokenLocation(), "[" + NAME + "] query does not support ["
                            + currentFieldName + "]");
                }
            } else if (token == XContentParser.Token.START_OBJECT) {
                if (MINIMUM_SHOULD_MATCH_SCRIPT.match(currentFieldName)) {
                    minimumShouldMatchScript = Script.parse(parser);
                } else {
                    throw new ParsingException(parser.getTokenLocation(), "[" + NAME + "] query does not support ["
                            + currentFieldName + "]");
                }
            } else if (token.isValue()) {
                if (MINIMUM_SHOULD_MATCH_FIELD.match(currentFieldName)) {
                    minimumShouldMatchField = parser.text();
                } else if (BOOST_FIELD.match(currentFieldName)) {
                    boost = parser.floatValue();
                } else if (NAME_FIELD.match(currentFieldName)) {
                    queryName = parser.text();
                } else {
                    throw new ParsingException(parser.getTokenLocation(), "[" + NAME + "] query does not support ["
                            + currentFieldName + "]");
                }
            } else {
                throw new ParsingException(parser.getTokenLocation(), "[" + NAME + "] unknown token [" + token +
                        "] after [" + currentFieldName + "]");
            }
        }
        token = parser.nextToken();
        if (token != XContentParser.Token.END_OBJECT) {
            throw new ParsingException(parser.getTokenLocation(), "[" + NAME + "] unknown token [" + token + "]");
        }
        TermsSetQueryBuilder queryBuilder = new TermsSetQueryBuilder(fieldName, values)
                .queryName(queryName).boost(boost);
        if (minimumShouldMatchField != null) {
            queryBuilder.setMinimumShouldMatchField(minimumShouldMatchField);
        }
        if (minimumShouldMatchScript != null) {
            queryBuilder.setMinimumShouldMatchScript(minimumShouldMatchScript);
        }
        return queryBuilder;
    }

    @Override
    protected void doXContent(XContentBuilder builder) throws IOException {
        builder.startObject(NAME);
        builder.startObject(fieldName);
        builder.field(TERMS_FIELD.getPreferredName(), values);
        if (minimumShouldMatchField != null) {
            builder.field(MINIMUM_SHOULD_MATCH_FIELD.getPreferredName(), minimumShouldMatchField);
        }
        if (minimumShouldMatchScript != null) {
            builder.field(MINIMUM_SHOULD_MATCH_SCRIPT.getPreferredName(), minimumShouldMatchScript);
        }
        printBoostAndQueryName(builder);
        builder.endObject();
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
