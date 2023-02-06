package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author daiyongjun
 */
public class AggregationsBuilderFactory {
    public static final Pattern VALID_AGG_NAME = Pattern.compile("[^\\[\\]>]+");
    /**
     * 递归解析聚合获取AggregationsBuilder。（第一层次）
     *
     * @param parser XContentParser 正文解析器
     * @return AggregationBuilders
     * @throws IOException 解析异常
     */
    public static AggregationsBuilder parseAggregators(XContentParser parser) throws IOException {
        return parseAggregators(parser, 0);
    }

    /**
     * 递归解析聚合请求，AggregationsBuilder。（第N层次）
     *
     * @param parser XContentParser 正文解析器
     * @param level  第N层次
     * @return AggregatorFactories.Builder
     * @throws IOException 解析异常
     */
    private static AggregationsBuilder parseAggregators(XContentParser parser, int level) throws IOException {
        AggregationsBuilder aggregationsBuilder = new AggregationsBuilder();
        Matcher validAggMatcher = VALID_AGG_NAME.matcher("");
        XContentParser.Token token = null;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token != XContentParser.Token.FIELD_NAME) {
                throw new ParsingException(parser.getTokenLocation(),
                        "Unexpected token " + token + " in [aggs]: aggregations definitions must start with the name of the aggregation.");
            }
            final String aggregationName = parser.currentName();
            if (!validAggMatcher.reset(aggregationName).matches()) {
                throw new ParsingException(parser.getTokenLocation(), "Invalid aggregation name [" + aggregationName
                        + "]. Aggregation names must be alpha-numeric and can only contain '_' and '-'");
            }
            token = parser.nextToken();
            if (token != XContentParser.Token.START_OBJECT) {
                throw new ParsingException(parser.getTokenLocation(), "Aggregation definition for [" + aggregationName + " starts with a ["
                        + token + "], expected a [" + XContentParser.Token.START_OBJECT + "].");
            }
            AggregationBuilder aggregation = null;
            AggregationsBuilder subAggregations = null;
            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token != XContentParser.Token.FIELD_NAME) {
                    throw new ParsingException(
                            parser.getTokenLocation(), "Expected [" + XContentParser.Token.FIELD_NAME + "] under a ["
                            + XContentParser.Token.START_OBJECT + "], but got a [" + token + "] in [" + aggregationName + "]",
                            parser.getTokenLocation());
                }
                final String fieldName = parser.currentName();
                token = parser.nextToken();
                if (token == XContentParser.Token.START_OBJECT) {
                    switch (fieldName) {
                        case "aggregations":
                        case "aggs":
                            if (subAggregations != null) {
                                throw new ParsingException(parser.getTokenLocation(),
                                        "Found two sub aggregation definitions under [" + aggregationName + "]");
                            }
                            subAggregations = parseAggregators(parser, level + 1);
                            break;
                        default:
                            if (aggregation != null) {
                                throw new ParsingException(parser.getTokenLocation(), "Found two aggregation type definitions in ["
                                        + aggregationName + "]: [" + aggregation.getType() + "] and [" + fieldName + "]");
                            }
                            aggregation = parser.namedObject(AggregationBuilder.class, fieldName, aggregationName);
                    }
                } else {
                    throw new ParsingException(parser.getTokenLocation(), "Expected [" + XContentParser.Token.START_OBJECT + "] under ["
                            + fieldName + "], but got a [" + token + "] in [" + aggregationName + "]");
                }
            }
            if (aggregation == null) {
                throw new ParsingException(parser.getTokenLocation(), "Missing definition for aggregation [" + aggregationName + "]",
                        parser.getTokenLocation());
            } else {
                if (subAggregations != null) {
                    aggregation.subAggregations(subAggregations);
                }
                if (aggregation instanceof AggregationBuilder) {
                    aggregationsBuilder.addAggregationBuilder((AggregationBuilder) aggregation);
                }
            }
        }
        return aggregationsBuilder;
    }


}
