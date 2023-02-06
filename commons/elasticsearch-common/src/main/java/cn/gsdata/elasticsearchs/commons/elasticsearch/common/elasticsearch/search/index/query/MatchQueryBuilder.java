package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.common.unit.Fuzziness;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.index.search.MatchQuery;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.FuzzyQuery;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.Query;

import java.io.IOException;

/**
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/query-dsl-match-query.html
 * 描述的语法格式如下：{"query":{"match":{"content":{"query":"Brown Fox","operator":"and"}}}}
 *
 * @author daiyongjun
 */
public class MatchQueryBuilder extends AbstractQueryBuilder<MatchQueryBuilder> {
    public static final ParseField QUERY_FIELD = new ParseField("query");
    public static final ParseField ZERO_TERMS_QUERY_FIELD = new ParseField("zero_terms_query");
    public static final ParseField CUTOFF_FREQUENCY_FIELD = new ParseField("cutoff_frequency");
    public static final ParseField LENIENT_FIELD = new ParseField("lenient");
    public static final ParseField FUZZY_TRANSPOSITIONS_FIELD = new ParseField("fuzzy_transpositions");
    public static final ParseField FUZZY_REWRITE_FIELD = new ParseField("fuzzy_rewrite");
    public static final ParseField MINIMUM_SHOULD_MATCH_FIELD = new ParseField("minimum_should_match");
    public static final ParseField OPERATOR_FIELD = new ParseField("operator");
    public static final ParseField MAX_EXPANSIONS_FIELD = new ParseField("max_expansions");
    public static final ParseField PREFIX_LENGTH_FIELD = new ParseField("prefix_length");
    public static final ParseField ANALYZER_FIELD = new ParseField("analyzer");
    public static final ParseField GENERATE_SYNONYMS_PHRASE_QUERY = new ParseField("auto_generate_synonyms_phrase_query");
    /**
     * 匹配查询的名称
     */
    public static final String NAME = "match";
    /**
     * 默认模式术语组合在匹配查询中
     */
    public static final Operator DEFAULT_OPERATOR = Operator.OR;
    /**
     * 所需查询字段的名称：
     */
    private final String fieldName;
    /**
     * 待查询的值内容：如"query":"Brown Fox"中的"Brown Fox"
     */
    private final Object value;
    /**
     * 定义的枚举类型：用于解释查询值中文本的布尔逻辑。
     * （1）OR（默认）：查询的文本值经过分析器分析后需匹配一个即可。
     * （2）AND：查询的文本值经过分析器分析后需匹配全部。
     */
    private Operator operator = DEFAULT_OPERATOR;
    /**
     * (可选的，string类型)分析器用于将查询值中的文本转换为分词。默认情况下，使用索引时的分析器（analyzer）。
     */
    private String analyzer;
    /**
     * (可选的，string类型)文档中子句必须匹配的最小数目。
     */
    private String minimumShouldMatch;
    /**
     * (可选的，boolean类型)如果为true，将自动为多词条同义词创建匹配短语查询。默认值为true。
     */
    private boolean autoGenerateSynonymsPhraseQuery = true;
    /**
     * (可选的，string类型)模糊性表示允许匹配的最大编辑距离。
     */
    private Fuzziness fuzziness = null;
    /**
     * FuzzyQuery的模糊查询类属性， (可选的，integer类型)为模糊匹配而保持不变的起始字符数。默认值为0。
     * prefix_length 表示不能没模糊化的初始字符数。由于大部分的拼写错误发生在词的结尾，而不是词的开始，使用 prefix_length 就可以完成优化。注意 prefix_length 必须结合 fuzziness 参数使用。
     */
    private int prefixLength = FuzzyQuery.defaultPrefixLength;
    /**
     * FuzzyQuery的模糊查询类属性，(可选的，integer类型)查询将扩展到的最大词条数。默认值为50。
     */
    private int maxExpansions = FuzzyQuery.defaultMaxExpansions;
    /**
     * FuzzyQuery的模糊查询类属性，(可选的，boolean类型)如果为true，用于模糊匹配的编辑包括两个相邻字符的换位(ab→ba)。默认值为true。
     */
    private boolean fuzzyTranspositions = FuzzyQuery.defaultTranspositions;
    /**
     * （可选，字符串）用于重写查询的方法。有关有效值和更多信息，请参见rewrite参数。
     */
    private String fuzzyRewrite = null;
    /**
     * (可选的，boolean类型)如果为true，则忽略输入值格式的错误，比如为数值字段提供文本查询值。默认值为false。
     */
    private boolean lenient = false;
    /**
     * 在 [0..1] 中设置一个截止值（或绝对数 >=1），表示将被视为低频术语的术语文档频率的最大阈值。
     */
    private Float cutoffFrequency = null;

    /**
     * (可选的，string类型)表示如果分析器删除所有分词，是否不返回文档。
     * （1）none（默认）：如果分析器删除所有分词，则不会返回文档。
     * （2）all：如果分析器删除所有分词，返回所有文档。
     */
    private MatchQuery.ZeroTermsQuery zeroTermsQuery = MatchQuery.DEFAULT_ZERO_TERMS_QUERY;

    public MatchQueryBuilder(String fieldName, Object value) {
        if (fieldName == null) {
            throw new IllegalArgumentException("[" + NAME + "] requires fieldName");
        }
        if (value == null) {
            throw new IllegalArgumentException("[" + NAME + "] requires query value");
        }
        this.fieldName = fieldName;
        this.value = value;
    }


    /**
     * 设置术语之间的关系，默认是OR 的关系
     *
     * @param operator Operator    术语之间的关系
     * @return MatchQueryBuilder
     */
    public MatchQueryBuilder operator(Operator operator) {
        if (operator == null) {
            throw new IllegalArgumentException("[" + NAME + "] requires operator to be non-null");
        }
        this.operator = operator;
        return this;
    }

    /**
     * 设置待使用的分析器用于将查询值中的文本转换为分词。默认情况下，使用索引时的分析器（analyzer）。
     *
     * @param analyzer String
     * @return MatchQueryBuilder
     */
    public MatchQueryBuilder analyzer(String analyzer) {
        this.analyzer = analyzer;
        return this;
    }

    /**
     * 由于设置成词元之间的关系是OR的关系，OR的关系就存在长尾问题，由此(可选的，string类型)文档中子句必须匹配的最小数目。
     *
     * @param minimumShouldMatch String
     * @return MatchQueryBuilder
     */
    public MatchQueryBuilder minimumShouldMatch(String minimumShouldMatch) {
        this.minimumShouldMatch = minimumShouldMatch;
        return this;
    }

    /**
     * 设置 (可选的，string类型)模糊性表示允许匹配的最大编辑距离。
     *
     * @param fuzziness Object
     * @return MatchQueryBuilder
     */
    public MatchQueryBuilder fuzziness(Object fuzziness) {
        this.fuzziness = Fuzziness.build(fuzziness);
        return this;
    }

    /**
     * 设置（可选，字符串）用于重写查询的方法。有关有效值和更多信息，请参见rewrite参数。
     *
     * @param fuzzyRewrite String
     * @return MatchQueryBuilder
     */
    public MatchQueryBuilder fuzzyRewrite(String fuzzyRewrite) {
        this.fuzzyRewrite = fuzzyRewrite;
        return this;
    }

    /**
     * 设置FuzzyQuery的模糊查询类属性， (可选的，integer类型)为模糊匹配而保持不变的起始字符数。默认值为0。
     *
     * @param prefixLength int
     * @return MatchQueryBuilder
     */
    public MatchQueryBuilder prefixLength(int prefixLength) {
        if (prefixLength < 0) {
            throw new IllegalArgumentException("[" + NAME + "] requires prefix length to be non-negative.");
        }
        this.prefixLength = prefixLength;
        return this;
    }

    /**
     * 设置FuzzyQuery的模糊查询类属性，(可选的，boolean类型)如果为true，用于模糊匹配的编辑包括两个相邻字符的换位(ab→ba)。默认值为true。
     *
     * @param fuzzyTranspositions boolean
     * @return MatchQueryBuilder
     */
    public MatchQueryBuilder fuzzyTranspositions(boolean fuzzyTranspositions) {
        this.fuzzyTranspositions = fuzzyTranspositions;
        return this;
    }

    /**
     * 设置FuzzyQuery的模糊查询类属性，(可选的，integer类型)查询将扩展到的最大词条数。默认值为50。
     *
     * @param maxExpansions int
     * @return MatchQueryBuilder
     */
    public MatchQueryBuilder maxExpansions(int maxExpansions) {
        if (maxExpansions <= 0) {
            throw new IllegalArgumentException("[" + NAME + "] requires maxExpansions to be positive.");
        }
        this.maxExpansions = maxExpansions;
        return this;
    }

    /**
     * 设置(可选的，boolean类型)如果为true，则忽略输入值格式的错误，比如为数值字段提供文本查询值。默认值为false。
     *
     * @param lenient boolean
     * @return MatchQueryBuilder
     */
    public MatchQueryBuilder lenient(boolean lenient) {
        this.lenient = lenient;
        return this;
    }

    /**
     * 设置 在 [0..1] 中设置一个截止值
     *
     * @param cutoff float
     * @return MatchQueryBuilder
     */
    public MatchQueryBuilder cutoffFrequency(float cutoff) {
        this.cutoffFrequency = cutoff;
        return this;
    }

    /**
     * 设置MatchQuery的zeroTermsQuery的值
     *
     * @param zeroTermsQuery MatchQuery.ZeroTermsQuery
     * @return MatchQueryBuilder
     */
    public MatchQueryBuilder zeroTermsQuery(MatchQuery.ZeroTermsQuery zeroTermsQuery) {
        if (zeroTermsQuery == null) {
            throw new IllegalArgumentException("[" + NAME + "] requires zeroTermsQuery to be non-null");
        }
        this.zeroTermsQuery = zeroTermsQuery;
        return this;
    }

    /**
     * 设置将自动为多词条同义词创建匹配短语查询
     *
     * @param enable boolean
     * @return MatchQueryBuilder
     */
    public MatchQueryBuilder autoGenerateSynonymsPhraseQuery(boolean enable) {
        this.autoGenerateSynonymsPhraseQuery = enable;
        return this;
    }

    /**
     * Builder建造核心处理方法
     *
     * @param parser XContentParser解析器
     * @return MatchQueryBuilder
     * @throws IOException 解析异常
     */
    public static MatchQueryBuilder fromXContent(XContentParser parser, Object Context) throws IOException {
        String fieldName = null;
        Object value = null;
        float boost = DEFAULT_BOOST;
        String minimumShouldMatch = null;
        String analyzer = null;
        Operator operator = MatchQueryBuilder.DEFAULT_OPERATOR;
        Fuzziness fuzziness = null;
        int prefixLength = FuzzyQuery.defaultPrefixLength;
        int maxExpansion = FuzzyQuery.defaultMaxExpansions;
        boolean fuzzyTranspositions = FuzzyQuery.defaultTranspositions;
        String fuzzyRewrite = null;
        boolean lenient = MatchQuery.DEFAULT_LENIENCY;
        Float cutOffFrequency = null;
        MatchQuery.ZeroTermsQuery zeroTermsQuery = MatchQuery.DEFAULT_ZERO_TERMS_QUERY;
        boolean autoGenerateSynonymsPhraseQuery = true;
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
                    } else if (token.isValue()) {
                        if (QUERY_FIELD.match(currentFieldName)) {
                            value = parser.objectText();
                        } else if (ANALYZER_FIELD.match(currentFieldName)) {
                            analyzer = parser.text();
                        } else if (BOOST_FIELD.match(currentFieldName)) {
                            boost = parser.floatValue();
                        } else if (Fuzziness.FIELD.match(currentFieldName)) {
                            fuzziness = Fuzziness.parse(parser);
                        } else if (PREFIX_LENGTH_FIELD.match(currentFieldName)) {
                            prefixLength = parser.intValue();
                        } else if (MAX_EXPANSIONS_FIELD.match(currentFieldName)) {
                            maxExpansion = parser.intValue();
                        } else if (OPERATOR_FIELD.match(currentFieldName)) {
                            operator = Operator.fromString(parser.text());
                        } else if (MINIMUM_SHOULD_MATCH_FIELD.match(currentFieldName)) {
                            minimumShouldMatch = parser.textOrNull();
                        } else if (FUZZY_REWRITE_FIELD.match(currentFieldName)) {
                            fuzzyRewrite = parser.textOrNull();
                        } else if (FUZZY_TRANSPOSITIONS_FIELD.match(currentFieldName)) {
                            fuzzyTranspositions = parser.booleanValue();
                        } else if (LENIENT_FIELD.match(currentFieldName)) {
                            lenient = parser.booleanValue();
                        } else if (CUTOFF_FREQUENCY_FIELD.match(currentFieldName)) {
                            cutOffFrequency = parser.floatValue();
                        } else if (ZERO_TERMS_QUERY_FIELD.match(currentFieldName)) {
                            String zeroTermsDocs = parser.text();
                            if ("none".equalsIgnoreCase(zeroTermsDocs)) {
                                zeroTermsQuery = MatchQuery.ZeroTermsQuery.NONE;
                            } else if ("all".equalsIgnoreCase(zeroTermsDocs)) {
                                zeroTermsQuery = MatchQuery.ZeroTermsQuery.ALL;
                            } else {
                                throw new ParsingException(parser.getTokenLocation(),
                                        "Unsupported zero_terms_docs value [" + zeroTermsDocs + "]");
                            }
                        } else if (NAME_FIELD.match(currentFieldName)) {
                            queryName = parser.text();
                        } else if (GENERATE_SYNONYMS_PHRASE_QUERY.match(currentFieldName)) {
                            autoGenerateSynonymsPhraseQuery = parser.booleanValue();
                        } else {
                            throw new ParsingException(parser.getTokenLocation(),
                                    "[" + NAME + "] query does not support [" + currentFieldName + "]");
                        }
                    } else {
                        throw new ParsingException(parser.getTokenLocation(),
                                "[" + NAME + "] unknown token [" + token + "] after [" + currentFieldName + "]");
                    }
                }
            } else {
                throwParsingExceptionOnMultipleFields(NAME, parser.getTokenLocation(), fieldName, parser.currentName());
                fieldName = parser.currentName();
                value = parser.objectText();
            }
        }

        if (value == null) {
            throw new ParsingException(parser.getTokenLocation(), "No text specified for text query");
        }
        MatchQueryBuilder matchQuery = new MatchQueryBuilder(fieldName, value);
        matchQuery.operator(operator);
        matchQuery.analyzer(analyzer);
        matchQuery.minimumShouldMatch(minimumShouldMatch);
        if (fuzziness != null) {
            matchQuery.fuzziness(fuzziness);
        }
        matchQuery.fuzzyRewrite(fuzzyRewrite);
        matchQuery.prefixLength(prefixLength);
        matchQuery.fuzzyTranspositions(fuzzyTranspositions);
        matchQuery.maxExpansions(maxExpansion);
        matchQuery.lenient(lenient);
        if (cutOffFrequency != null) {
            matchQuery.cutoffFrequency(cutOffFrequency);
        }
        matchQuery.zeroTermsQuery(zeroTermsQuery);
        matchQuery.autoGenerateSynonymsPhraseQuery(autoGenerateSynonymsPhraseQuery);
        matchQuery.queryName(queryName);
        matchQuery.boost(boost);
        return matchQuery;
    }


    @Override
    protected void doXContent(XContentBuilder builder) throws IOException {
        builder.startObject(NAME);
        builder.startObject(fieldName);

        builder.field(QUERY_FIELD.getPreferredName(), value);
        builder.field(OPERATOR_FIELD.getPreferredName(), operator.toString());
        if (analyzer != null) {
            builder.field(ANALYZER_FIELD.getPreferredName(), analyzer);
        }
        if (fuzziness != null) {
            fuzziness.toXContent(builder);
        }
        builder.field(PREFIX_LENGTH_FIELD.getPreferredName(), prefixLength);
        builder.field(MAX_EXPANSIONS_FIELD.getPreferredName(), maxExpansions);
        if (minimumShouldMatch != null) {
            builder.field(MINIMUM_SHOULD_MATCH_FIELD.getPreferredName(), minimumShouldMatch);
        }
        if (fuzzyRewrite != null) {
            builder.field(FUZZY_REWRITE_FIELD.getPreferredName(), fuzzyRewrite);
        }
        // LUCENE 4 UPGRADE we need to document this & test this
        builder.field(FUZZY_TRANSPOSITIONS_FIELD.getPreferredName(), fuzzyTranspositions);
        builder.field(LENIENT_FIELD.getPreferredName(), lenient);
        builder.field(ZERO_TERMS_QUERY_FIELD.getPreferredName(), zeroTermsQuery.toString());
        if (cutoffFrequency != null) {
            builder.field(CUTOFF_FREQUENCY_FIELD.getPreferredName(), cutoffFrequency);
        }
        builder.field(GENERATE_SYNONYMS_PHRASE_QUERY.getPreferredName(), autoGenerateSynonymsPhraseQuery);
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
