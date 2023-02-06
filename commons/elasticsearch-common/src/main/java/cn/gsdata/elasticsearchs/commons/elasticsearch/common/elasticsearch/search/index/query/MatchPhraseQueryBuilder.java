package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.index.search.MatchQuery;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.Strings;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.Query;

import java.io.IOException;

/**
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/query-dsl-match-query-phrase.html
 *
 * @author daiyongjun
 */
public class MatchPhraseQueryBuilder extends AbstractQueryBuilder<MatchQueryBuilder> {
    public static final String NAME = "match_phrase";
    public static final ParseField SLOP_FIELD = new ParseField("slop");
    /**
     * 短语匹配的字段的名称
     */
    private final String fieldName;
    /**
     * 待查询的值内容：如"match_phrase":"Brown Fox"中的"Brown Fox"
     */
    private final Object value;
    /**
     * (可选的，string类型)分析器用于将查询值中的文本转换为分词。默认情况下，使用索引时的分析器（analyzer）。
     */
    private String analyzer;
    /**
     * 短语匹配默认词元之间的距离
     */
    private int slop = MatchQuery.DEFAULT_PHRASE_SLOP;

    public MatchPhraseQueryBuilder(String fieldName, Object value) {
        if (Strings.isEmpty(fieldName)) {
            throw new IllegalArgumentException("[" + NAME + "] requires fieldName");
        }
        if (value == null) {
            throw new IllegalArgumentException("[" + NAME + "] requires query value");
        }
        this.fieldName = fieldName;
        this.value = value;
    }

    /**
     * 设置(可选的，string类型)分析器用于将查询值中的文本转换为分词。默认情况下，使用索引时的分析器（analyzer）。
     *
     * @param analyzer 指定分词器
     * @return MatchPhraseQueryBuilder
     */
    public MatchPhraseQueryBuilder analyzer(String analyzer) {
        this.analyzer = analyzer;
        return this;
    }

    /**
     * 设置短语匹配默认词元之间的距离
     *
     * @param slop 词元之间的位置距离 0为默认值
     * @return MatchPhraseQueryBuilder
     */
    public MatchPhraseQueryBuilder slop(int slop) {
        if (slop < 0) {
            throw new IllegalArgumentException("No negative slop allowed.");
        }
        this.slop = slop;
        return this;
    }

    public static MatchPhraseQueryBuilder fromXContent(XContentParser parser, Object Context) throws IOException {
        String fieldName = null;
        Object value = null;
        float boost = DEFAULT_BOOST;
        String analyzer = null;
        int slop = MatchQuery.DEFAULT_PHRASE_SLOP;
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
                        if (MatchQueryBuilder.QUERY_FIELD.match(currentFieldName)) {
                            value = parser.objectText();
                        } else if (MatchQueryBuilder.ANALYZER_FIELD.match(currentFieldName)) {
                            analyzer = parser.text();
                        } else if (BOOST_FIELD.match(currentFieldName)) {
                            boost = parser.floatValue();
                        } else if (SLOP_FIELD.match(currentFieldName)) {
                            slop = parser.intValue();
                        } else if (NAME_FIELD.match(currentFieldName)) {
                            queryName = parser.text();
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
        MatchPhraseQueryBuilder matchQuery = new MatchPhraseQueryBuilder(fieldName, value);
        matchQuery.analyzer(analyzer);
        matchQuery.slop(slop);
        matchQuery.queryName(queryName);
        matchQuery.boost(boost);
        return matchQuery;
    }

    @Override
    protected void doXContent(XContentBuilder builder) throws IOException {
        builder.startObject(NAME);
        builder.startObject(fieldName);

        builder.field(MatchQueryBuilder.QUERY_FIELD.getPreferredName(), value);
        if (analyzer != null) {
            builder.field(MatchQueryBuilder.ANALYZER_FIELD.getPreferredName(), analyzer);
        }
        builder.field(SLOP_FIELD.getPreferredName(), slop);
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
