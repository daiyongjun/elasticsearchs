package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.Strings;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.lucene.search.Query;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/query-dsl-ids-query.html
 * 过滤仅具有提供的 ID 的文档。请注意，此查询使用_uid字段
 * 是可选的type，可以省略，也可以接受值数组。如果未指定类型，则尝试索引映射中定义的所有类型。
 *
 * @author daiyongjun
 */
public class IdsQueryBuilder extends AbstractQueryBuilder<IdsQueryBuilder> {
    public static final String NAME = "ids";

    private static final ParseField TYPE_FIELD = new ParseField("type");
    private static final ParseField VALUES_FIELD = new ParseField("values");

    private final Set<String> ids = new HashSet<>();
    /**
     *
     */
    private String[] types = Strings.EMPTY_ARRAY;

    private static ObjectParser<Void, IdsQueryBuilder> PARSER = new ObjectParser<>(IdsQueryBuilder.NAME, IdsQueryBuilder::new);

    static {
        PARSER.declareStringArray(ObjectParser.fromList(String.class, IdsQueryBuilder::types), IdsQueryBuilder.TYPE_FIELD);
        PARSER.declareStringArray(ObjectParser.fromList(String.class, IdsQueryBuilder::addIds), IdsQueryBuilder.VALUES_FIELD);

        PARSER.declareFloat(QueryBuilder::boost, BOOST_FIELD);
        PARSER.declareString(QueryBuilder::queryName, NAME_FIELD);
    }


    public IdsQueryBuilder() {
        // nothing to do
    }


    public IdsQueryBuilder types(String... types) {
        if (types == null) {
            throw new IllegalArgumentException("[" + NAME + "] types cannot be null");
        }
        this.types = types;
        return this;
    }


    public IdsQueryBuilder addIds(String... ids) {
        if (ids == null) {
            throw new IllegalArgumentException("[" + NAME + "] ids cannot be null");
        }
        Collections.addAll(this.ids, ids);
        return this;
    }

    public static IdsQueryBuilder fromXContent(XContentParser parser, Object Context) {
        try {
            return PARSER.apply(parser, null);
        } catch (IllegalArgumentException e) {
            throw new ParsingException(parser.getTokenLocation(), e.getMessage(), e);
        }
    }

    @Override
    protected void doXContent(XContentBuilder builder) throws IOException {
        builder.startObject(NAME);
        builder.array(TYPE_FIELD.getPreferredName(), types);
        builder.startArray(VALUES_FIELD.getPreferredName());
        for (String value : ids) {
            builder.value(value);
        }
        builder.endArray();
        printBoostAndQueryName(builder);
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
