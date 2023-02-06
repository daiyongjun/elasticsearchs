package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.common.unit.Fuzziness;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.Strings;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.FuzzyQuery;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.Query;

import java.io.IOException;

/**
 * 模糊查询 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/query-dsl-fuzzy-query.html
 * @author daiyongjun
 */
public class FuzzyQueryBuilder extends AbstractQueryBuilder<FuzzyQueryBuilder> implements MultiTermQueryBuilder{
    public static final String NAME = "fuzzy";

    public static final Fuzziness DEFAULT_FUZZINESS = Fuzziness.AUTO;

    public static final int DEFAULT_PREFIX_LENGTH = FuzzyQuery.defaultPrefixLength;

    public static final int DEFAULT_MAX_EXPANSIONS = FuzzyQuery.defaultMaxExpansions;

    public static final boolean DEFAULT_TRANSPOSITIONS = false;

    private static final ParseField TERM_FIELD = new ParseField("term");
    private static final ParseField VALUE_FIELD = new ParseField("value");
    private static final ParseField PREFIX_LENGTH_FIELD = new ParseField("prefix_length");
    private static final ParseField MAX_EXPANSIONS_FIELD = new ParseField("max_expansions");
    private static final ParseField TRANSPOSITIONS_FIELD = new ParseField("transpositions");
    private static final ParseField REWRITE_FIELD = new ParseField("rewrite");

    private final String fieldName;

    private final Object value;

    private Fuzziness fuzziness = DEFAULT_FUZZINESS;

    private int prefixLength = DEFAULT_PREFIX_LENGTH;

    private int maxExpansions = DEFAULT_MAX_EXPANSIONS;

    private boolean transpositions = DEFAULT_TRANSPOSITIONS;

    private String rewrite;

    public FuzzyQueryBuilder(String fieldName, Object value) {
        if (Strings.isEmpty(fieldName)) {
            throw new IllegalArgumentException("field name cannot be null or empty");
        }
        if (value == null) {
            throw new IllegalArgumentException("query value cannot be null");
        }
        this.fieldName = fieldName;
        this.value = value;
    }

    public FuzzyQueryBuilder fuzziness(Fuzziness fuzziness) {
        this.fuzziness = (fuzziness == null) ? DEFAULT_FUZZINESS : fuzziness;
        return this;
    }

    public FuzzyQueryBuilder prefixLength(int prefixLength) {
        this.prefixLength = prefixLength;
        return this;
    }

    public FuzzyQueryBuilder maxExpansions(int maxExpansions) {
        this.maxExpansions = maxExpansions;
        return this;
    }

    public FuzzyQueryBuilder transpositions(boolean transpositions) {
        this.transpositions = transpositions;
        return this;
    }

    public FuzzyQueryBuilder rewrite(String rewrite) {
        this.rewrite = rewrite;
        return this;
    }


    public static FuzzyQueryBuilder fromXContent(XContentParser parser, Object Context) throws IOException {
        String fieldName = null;
        Object value = null;
        Fuzziness fuzziness = FuzzyQueryBuilder.DEFAULT_FUZZINESS;
        int prefixLength = FuzzyQueryBuilder.DEFAULT_PREFIX_LENGTH;
        int maxExpansions = FuzzyQueryBuilder.DEFAULT_MAX_EXPANSIONS;
        boolean transpositions = FuzzyQueryBuilder.DEFAULT_TRANSPOSITIONS;
        String rewrite = null;
        String queryName = null;
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
                    } else if (token.isValue()) {
                        if (TERM_FIELD.match(currentFieldName)) {
                            value = parser.objectText();
                        } else if (VALUE_FIELD.match(currentFieldName)) {
                            value = parser.objectText();
                        } else if (BOOST_FIELD.match(currentFieldName)) {
                            boost = parser.floatValue();
                        } else if (Fuzziness.FIELD.match(currentFieldName)) {
                            fuzziness = Fuzziness.parse(parser);
                        } else if (PREFIX_LENGTH_FIELD.match(currentFieldName)) {
                            prefixLength = parser.intValue();
                        } else if (MAX_EXPANSIONS_FIELD.match(currentFieldName)) {
                            maxExpansions = parser.intValue();
                        } else if (TRANSPOSITIONS_FIELD.match(currentFieldName)) {
                            transpositions = parser.booleanValue();
                        } else if (REWRITE_FIELD.match(currentFieldName)) {
                            rewrite = parser.textOrNull();
                        } else if (NAME_FIELD.match(currentFieldName)) {
                            queryName = parser.text();
                        } else {
                            throw new ParsingException(parser.getTokenLocation(),
                                    "[fuzzy] query does not support [" + currentFieldName + "]");
                        }
                    } else {
                        throw new ParsingException(parser.getTokenLocation(),
                                "[" + NAME + "] unexpected token [" + token + "] after [" + currentFieldName + "]");
                    }
                }
            } else {
                throwParsingExceptionOnMultipleFields(NAME, parser.getTokenLocation(), fieldName, parser.currentName());
                fieldName = parser.currentName();
                value = parser.objectText();
            }
        }
        return new FuzzyQueryBuilder(fieldName, value)
                .fuzziness(fuzziness)
                .prefixLength(prefixLength)
                .maxExpansions(maxExpansions)
                .transpositions(transpositions)
                .rewrite(rewrite)
                .boost(boost)
                .queryName(queryName);
    }

    @Override
    protected void doXContent(XContentBuilder builder) throws IOException {
        builder.startObject(NAME);
        builder.startObject(fieldName);
        builder.field(VALUE_FIELD.getPreferredName(), this.value);
        fuzziness.toXContent(builder);
        builder.field(PREFIX_LENGTH_FIELD.getPreferredName(), prefixLength);
        builder.field(MAX_EXPANSIONS_FIELD.getPreferredName(), maxExpansions);
        builder.field(TRANSPOSITIONS_FIELD.getPreferredName(), transpositions);
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
