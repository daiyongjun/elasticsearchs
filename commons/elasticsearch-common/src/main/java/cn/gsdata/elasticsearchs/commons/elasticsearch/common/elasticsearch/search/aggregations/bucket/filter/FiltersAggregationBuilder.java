package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.bucket.filter;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query.AbstractQueryBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query.QueryBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.AbstractAggregationBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-aggregations-bucket-filters-aggregation.html
 *
 * @author daiyongjun
 */
public class FiltersAggregationBuilder extends AbstractAggregationBuilder<FiltersAggregationBuilder> {
    public static final String NAME = "filters";

    private static final ParseField FILTERS_FIELD = new ParseField("filters");
    private static final ParseField OTHER_BUCKET_FIELD = new ParseField("other_bucket");
    private static final ParseField OTHER_BUCKET_KEY_FIELD = new ParseField("other_bucket_key");

    private final List<KeyedFilter> filters;
    private final boolean keyed;
    private boolean otherBucket = false;
    private String otherBucketKey = "_other_";

    public FiltersAggregationBuilder(String name, KeyedFilter... filters) {
        this(name, Arrays.asList(filters), true);
    }

    public FiltersAggregationBuilder(String name, QueryBuilder... filters) {
        super(name);
        List<KeyedFilter> keyedFilters = new ArrayList<>(filters.length);
        for (int i = 0; i < filters.length; i++) {
            keyedFilters.add(new KeyedFilter(String.valueOf(i), filters[i]));
        }
        this.filters = keyedFilters;
        this.keyed = false;
    }

    private FiltersAggregationBuilder(String name, List<KeyedFilter> filters, boolean keyed) {
        super(name);
        this.filters = new ArrayList<>(filters);
        if (keyed) {
            // internally we want to have a fixed order of filters, regardless of the order of the filters in the request
            Collections.sort(this.filters, (KeyedFilter kf1, KeyedFilter kf2) -> kf1.key().compareTo(kf2.key()));
            this.keyed = true;
        } else {
            this.keyed = false;
        }
    }

    public static FiltersAggregationBuilder parse(String aggregationName, XContentParser parser)
            throws IOException {
        List<KeyedFilter> keyedFilters = null;
        List<QueryBuilder> nonKeyedFilters = null;
        XContentParser.Token token = null;
        String currentFieldName = null;
        String otherBucketKey = null;
        Boolean otherBucket = null;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.VALUE_BOOLEAN) {
                if (OTHER_BUCKET_FIELD.match(currentFieldName)) {
                    otherBucket = parser.booleanValue();
                } else {
                    throw new ParsingException(parser.getTokenLocation(),
                            "Unknown key for a " + token + " in [" + aggregationName + "]: [" + currentFieldName + "].");
                }
            } else if (token == XContentParser.Token.VALUE_STRING) {
                if (OTHER_BUCKET_KEY_FIELD.match(currentFieldName)) {
                    otherBucketKey = parser.text();
                } else {
                    throw new ParsingException(parser.getTokenLocation(),
                            "Unknown key for a " + token + " in [" + aggregationName + "]: [" + currentFieldName + "].");
                }
            } else if (token == XContentParser.Token.START_OBJECT) {
                if (FILTERS_FIELD.match(currentFieldName)) {
                    keyedFilters = new ArrayList<>();
                    String key = null;
                    while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                        if (token == XContentParser.Token.FIELD_NAME) {
                            key = parser.currentName();
                        } else {
                            QueryBuilder filter = AbstractQueryBuilder.parseInnerQueryBuilder(parser,"");
                            keyedFilters.add(new KeyedFilter(key, filter));
                        }
                    }
                } else {
                    throw new ParsingException(parser.getTokenLocation(),
                            "Unknown key for a " + token + " in [" + aggregationName + "]: [" + currentFieldName + "].");
                }
            } else if (token == XContentParser.Token.START_ARRAY) {
                if (FILTERS_FIELD.match(currentFieldName)) {
                    nonKeyedFilters = new ArrayList<>();
                    while ((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
                        QueryBuilder filter = AbstractQueryBuilder.parseInnerQueryBuilder(parser,"");
                        nonKeyedFilters.add(filter);
                    }
                } else {
                    throw new ParsingException(parser.getTokenLocation(),
                            "Unknown key for a " + token + " in [" + aggregationName + "]: [" + currentFieldName + "].");
                }
            } else {
                throw new ParsingException(parser.getTokenLocation(),
                        "Unknown key for a " + token + " in [" + aggregationName + "]: [" + currentFieldName + "].");
            }
        }

        if (otherBucket == null && otherBucketKey != null) {
            // automatically enable the other bucket if a key is set, as per the doc
            otherBucket = true;
        }

        FiltersAggregationBuilder factory;
        if (keyedFilters != null) {
            factory = new FiltersAggregationBuilder(aggregationName,
                    keyedFilters.toArray(new KeyedFilter[keyedFilters.size()]));
        } else {
            factory = new FiltersAggregationBuilder(aggregationName,
                    nonKeyedFilters.toArray(new QueryBuilder[nonKeyedFilters.size()]));
        }
        if (otherBucket != null) {
            factory.otherBucket(otherBucket);
        }
        if (otherBucketKey != null) {
            factory.otherBucketKey(otherBucketKey);
        }
        return factory;
    }


    @Override
    protected XContentBuilder internalXContent(XContentBuilder builder) throws IOException {
        builder.startObject();
        if (keyed) {
            builder.startObject(FILTERS_FIELD.getPreferredName());
            for (KeyedFilter keyedFilter : filters) {
                builder.field(keyedFilter.key(), keyedFilter.filter());
            }
            builder.endObject();
        } else {
            builder.startArray(FILTERS_FIELD.getPreferredName());
            for (KeyedFilter keyedFilter : filters) {
                builder.value(keyedFilter.filter());
            }
            builder.endArray();
        }
        builder.field(OTHER_BUCKET_FIELD.getPreferredName(), otherBucket);
        builder.field(OTHER_BUCKET_KEY_FIELD.getPreferredName(), otherBucketKey);
        builder.endObject();
        return builder;
    }

    public FiltersAggregationBuilder otherBucket(boolean otherBucket) {
        this.otherBucket = otherBucket;
        return this;
    }


    public FiltersAggregationBuilder otherBucketKey(String otherBucketKey) {
        if (otherBucketKey == null) {
            throw new IllegalArgumentException("[otherBucketKey] must not be null: [" + name + "]");
        }
        this.otherBucketKey = otherBucketKey;
        return this;
    }

    @Override
    public String getType() {
        return NAME;
    }
}
