package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.Strings;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.Query;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.util.automaton.Operations;

import java.io.IOException;

/**
 * 正则表达式查询，支持正则表达式的查询
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/query-dsl-regexp-query.html
 *
 * @author daiyongjun
 */
public class RegexpQueryBuilder extends AbstractQueryBuilder<RegexpQueryBuilder> implements MultiTermQueryBuilder {
    public static final String NAME = "regexp";

    public static final int DEFAULT_FLAGS_VALUE = RegexpFlag.ALL.value();
    public static final int DEFAULT_MAX_DETERMINIZED_STATES = Operations.DEFAULT_MAX_DETERMINIZED_STATES;

    private static final ParseField FLAGS_VALUE_FIELD = new ParseField("flags_value");
    private static final ParseField MAX_DETERMINIZED_STATES_FIELD = new ParseField("max_determinized_states");
    private static final ParseField FLAGS_FIELD = new ParseField("flags");
    private static final ParseField REWRITE_FIELD = new ParseField("rewrite");
    private static final ParseField VALUE_FIELD = new ParseField("value");

    private final String fieldName;

    private final String value;
    /**
     * 支持使用特殊标志
     */
    private int flagsValue = DEFAULT_FLAGS_VALUE;
    /**
     * 指数级数量（阀值防止其过于正则表达式的逻辑复杂度过于复杂）
     */
    private int maxDeterminizedStates = DEFAULT_MAX_DETERMINIZED_STATES;

    private String rewrite;

    public RegexpQueryBuilder(String fieldName, String value) {
        if (Strings.isEmpty(fieldName)) {
            throw new IllegalArgumentException("field name is null or empty");
        }
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        this.fieldName = fieldName;
        this.value = value;
    }

    public RegexpQueryBuilder flags(int flags) {
        this.flagsValue = flags;
        return this;
    }

    public RegexpQueryBuilder maxDeterminizedStates(int value) {
        this.maxDeterminizedStates = value;
        return this;
    }

    public RegexpQueryBuilder rewrite(String rewrite) {
        this.rewrite = rewrite;
        return this;
    }


    public static RegexpQueryBuilder fromXContent(XContentParser parser, Object Context) throws IOException {
        String fieldName = null;
        String rewrite = null;
        String value = null;
        float boost = DEFAULT_BOOST;
        int flagsValue = RegexpQueryBuilder.DEFAULT_FLAGS_VALUE;
        int maxDeterminizedStates = RegexpQueryBuilder.DEFAULT_MAX_DETERMINIZED_STATES;
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
                        if (VALUE_FIELD.match(currentFieldName)) {
                            value = parser.textOrNull();
                        } else if (BOOST_FIELD.match(currentFieldName)) {
                            boost = parser.floatValue();
                        } else if (REWRITE_FIELD.match(currentFieldName)) {
                            rewrite = parser.textOrNull();
                        } else if (FLAGS_FIELD.match(currentFieldName)) {
                            String flags = parser.textOrNull();
                            flagsValue = RegexpFlag.resolveValue(flags);
                        } else if (MAX_DETERMINIZED_STATES_FIELD.match(currentFieldName)) {
                            maxDeterminizedStates = parser.intValue();
                        } else if (FLAGS_VALUE_FIELD.match(currentFieldName)) {
                            flagsValue = parser.intValue();
                        } else if (NAME_FIELD.match(currentFieldName)) {
                            queryName = parser.text();
                        } else {
                            throw new ParsingException(parser.getTokenLocation(),
                                    "[regexp] query does not support [" + currentFieldName + "]");
                        }
                    }
                }
            } else {
                throwParsingExceptionOnMultipleFields(NAME, parser.getTokenLocation(), fieldName, parser.currentName());
                fieldName = currentFieldName;
                value = parser.textOrNull();
            }
        }

        return new RegexpQueryBuilder(fieldName, value)
                .flags(flagsValue)
                .maxDeterminizedStates(maxDeterminizedStates)
                .rewrite(rewrite)
                .boost(boost)
                .queryName(queryName);
    }

    @Override
    protected void doXContent(XContentBuilder builder) throws IOException {
        builder.startObject(NAME);
        builder.startObject(fieldName);
        builder.field(VALUE_FIELD.getPreferredName(), this.value);
        builder.field(FLAGS_VALUE_FIELD.getPreferredName(), flagsValue);
        builder.field(MAX_DETERMINIZED_STATES_FIELD.getPreferredName(), maxDeterminizedStates);
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
