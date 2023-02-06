package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.fetch.subphase;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ToXContent;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.Strings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * source资源，定义包含的参数，与排除的参数
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-request-source-filtering.html
 *
 * @author daiyongjun
 */
public class FetchSourceContext implements ToXContent {

    public static final ParseField INCLUDES_FIELD = new ParseField("includes", "include");
    public static final ParseField EXCLUDES_FIELD = new ParseField("excludes", "exclude");
    /**
     * 是否设置source资源
     */
    private boolean fetchSource;
    private final String[] includes;
    private final String[] excludes;

    public FetchSourceContext(Boolean fetchSource, String[] includes, String[] excludes) {
        this.fetchSource = fetchSource;
        this.includes = includes == null ? Strings.EMPTY_ARRAY : includes;
        this.excludes = excludes == null ? Strings.EMPTY_ARRAY : excludes;
    }

    public FetchSourceContext(boolean fetchSource) {
        this(fetchSource, Strings.EMPTY_ARRAY, Strings.EMPTY_ARRAY);
    }

    public static FetchSourceContext fromXContent(XContentParser parser) throws IOException {
        XContentParser.Token token = parser.currentToken();
        boolean fetchSource = true;
        String[] includes = Strings.EMPTY_ARRAY;
        String[] excludes = Strings.EMPTY_ARRAY;
        if (token == XContentParser.Token.VALUE_BOOLEAN) {
            fetchSource = parser.booleanValue();
        } else if (token == XContentParser.Token.VALUE_STRING) {
            includes = new String[]{parser.text()};
        } else if (token == XContentParser.Token.START_ARRAY) {
            ArrayList<String> list = new ArrayList<>();
            while ((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
                list.add(parser.text());
            }
            includes = list.toArray(new String[list.size()]);
        } else if (token == XContentParser.Token.START_OBJECT) {
            String currentFieldName = null;
            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    currentFieldName = parser.currentName();
                } else if (token == XContentParser.Token.START_ARRAY) {
                    if (INCLUDES_FIELD.match(currentFieldName)) {
                        List<String> includesList = new ArrayList<>();
                        while ((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
                            if (token == XContentParser.Token.VALUE_STRING) {
                                includesList.add(parser.text());
                            } else {
                                throw new ParsingException(parser.getTokenLocation(), "Unknown key for a " + token
                                        + " in [" + currentFieldName + "].", parser.getTokenLocation());
                            }
                        }
                        includes = includesList.toArray(new String[includesList.size()]);
                    } else if (EXCLUDES_FIELD.match(currentFieldName)) {
                        List<String> excludesList = new ArrayList<>();
                        while ((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
                            if (token == XContentParser.Token.VALUE_STRING) {
                                excludesList.add(parser.text());
                            } else {
                                throw new ParsingException(parser.getTokenLocation(), "Unknown key for a " + token
                                        + " in [" + currentFieldName + "].", parser.getTokenLocation());
                            }
                        }
                        excludes = excludesList.toArray(new String[excludesList.size()]);
                    } else {
                        throw new ParsingException(parser.getTokenLocation(), "Unknown key for a " + token
                                + " in [" + currentFieldName + "].", parser.getTokenLocation());
                    }
                } else if (token == XContentParser.Token.VALUE_STRING) {
                    if (INCLUDES_FIELD.match(currentFieldName)) {
                        includes = new String[]{parser.text()};
                    } else if (EXCLUDES_FIELD.match(currentFieldName)) {
                        excludes = new String[]{parser.text()};
                    } else {
                        throw new ParsingException(parser.getTokenLocation(), "Unknown key for a " + token
                                + " in [" + currentFieldName + "].", parser.getTokenLocation());
                    }
                } else {
                    throw new ParsingException(parser.getTokenLocation(), "Unknown key for a " + token + " in [" + currentFieldName + "].",
                            parser.getTokenLocation());
                }
            }
        } else {
            throw new ParsingException(parser.getTokenLocation(), "Expected one of [" + XContentParser.Token.VALUE_BOOLEAN + ", "
                    + XContentParser.Token.START_OBJECT + "] but found [" + token + "]", parser.getTokenLocation());
        }
        return new FetchSourceContext(fetchSource, includes, excludes);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        if (fetchSource) {
            builder.startObject();
            builder.array(INCLUDES_FIELD.getPreferredName(), includes);
            builder.array(EXCLUDES_FIELD.getPreferredName(), excludes);
            builder.endObject();
        } else {
            builder.value(false);
        }
        return builder;
    }
}
