package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.sort;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ToXContent;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query.QueryBuilder;

import java.io.IOException;
import java.util.*;

import static cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query.AbstractQueryBuilder.parseInnerQueryBuilder;
import static java.util.Collections.unmodifiableMap;

/**
 * SortBuilder类
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-request-sort.html
 *
 * @author daiyongjun
 */
public abstract class SortBuilder<T extends SortBuilder<T>> implements ToXContent {

    protected SortOrder order = SortOrder.ASC;


    public static final ParseField ORDER_FIELD = new ParseField("order");
    public static final ParseField NESTED_FILTER_FIELD = new ParseField("nested_filter");
    public static final ParseField NESTED_PATH_FIELD = new ParseField("nested_path");

    private static final Map<String, Parser<?>> PARSERS;

    static {
        Map<String, Parser<?>> parsers = new HashMap<>();
        parsers.put(ScriptSortBuilder.NAME, ScriptSortBuilder::fromXContent);
        //parsers.put(GeoDistanceSortBuilder.NAME, GeoDistanceSortBuilder::fromXContent);
        //parsers.put(GeoDistanceSortBuilder.ALTERNATIVE_NAME, GeoDistanceSortBuilder::fromXContent);
        parsers.put(ScoreSortBuilder.NAME, ScoreSortBuilder::fromXContent);
        // FieldSortBuilder gets involved if the user specifies a name that isn't one of these.
        PARSERS = unmodifiableMap(parsers);
    }


    public static List<SortBuilder<?>> fromXContent(XContentParser parser) throws IOException {
        List<SortBuilder<?>> sortFields = new ArrayList<>(2);
        XContentParser.Token token = parser.currentToken();
        if (token == XContentParser.Token.START_ARRAY) {
            while ((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
                if (token == XContentParser.Token.START_OBJECT) {
                    parseCompoundSortField(parser, sortFields);
                } else if (token == XContentParser.Token.VALUE_STRING) {
                    String fieldName = parser.text();
                    sortFields.add(fieldOrScoreSort(fieldName));
                } else {
                    throw new IllegalArgumentException("malformed sort format, "
                            + "within the sort array, an object, or an actual string are allowed");
                }
            }
        } else if (token == XContentParser.Token.VALUE_STRING) {
            String fieldName = parser.text();
            sortFields.add(fieldOrScoreSort(fieldName));
        } else if (token == XContentParser.Token.START_OBJECT) {
            parseCompoundSortField(parser, sortFields);
        } else {
            throw new IllegalArgumentException("malformed sort format, either start with array, object, or an actual string");
        }
        return sortFields;
    }

    private static void parseCompoundSortField(XContentParser parser, List<SortBuilder<?>> sortFields)
            throws IOException {
        XContentParser.Token token;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                String fieldName = parser.currentName();
                token = parser.nextToken();
                if (token == XContentParser.Token.VALUE_STRING) {
                    SortOrder order = SortOrder.fromString(parser.text());
                    sortFields.add(fieldOrScoreSort(fieldName).order(order));
                } else {
                    if (PARSERS.containsKey(fieldName)) {
                        sortFields.add(PARSERS.get(fieldName).fromXContent(parser, fieldName));
                    } else {
                        sortFields.add(FieldSortBuilder.fromXContent(parser, fieldName));
                    }
                }
            }
        }
    }


    private static SortBuilder<?> fieldOrScoreSort(String fieldName) {
        if (fieldName.equals(ScoreSortBuilder.NAME)) {
            return new ScoreSortBuilder();
        } else {
            return new FieldSortBuilder(fieldName);
        }
    }

    /**
     * 设置SortOrder属性
     *
     * @param order SortOrder
     * @return T
     */
    public T order(SortOrder order) {
        Objects.requireNonNull(order, "sort order cannot be null.");
        this.order = order;
        return (T) this;
    }

    protected static QueryBuilder parseNestedFilter(XContentParser parser, Object context) {
        try {
            return parseInnerQueryBuilder(parser, context);
        } catch (Exception e) {
            throw new ParsingException(parser.getTokenLocation(), "Expected " + NESTED_FILTER_FIELD.getPreferredName() + " element.", e);
        }
    }

    @FunctionalInterface
    private interface Parser<T extends SortBuilder<?>> {
        T fromXContent(XContentParser parser, String elementName) throws IOException;
    }
}
