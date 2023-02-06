package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.fetch;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-request-stored-fields.html#search-request-stored-fields
 *
 * @author daiyongjun
 */
public class StoredFieldsContext {
    public static final String _NONE_ = "_none_";


    private final List<String> fieldNames;
    private boolean fetchFields;

    private StoredFieldsContext(boolean fetchFields) {
        this.fetchFields = fetchFields;
        this.fieldNames = null;
    }


    private StoredFieldsContext(List<String> fieldNames) {
        Objects.requireNonNull(fieldNames, "fieldNames must not be null");
        this.fetchFields = true;
        this.fieldNames = new ArrayList<>(fieldNames);
    }

    public static StoredFieldsContext fromList(List<String> fieldNames) {
        if (fieldNames.size() == 1 && _NONE_.equals(fieldNames.get(0))) {
            return new StoredFieldsContext(false);
        }
        if (fieldNames.contains(_NONE_)) {
            throw new IllegalArgumentException("cannot combine _none_ with other fields");
        }
        return new StoredFieldsContext(fieldNames);
    }

    public static StoredFieldsContext fromXContent(String fieldName, XContentParser parser) throws IOException {
        XContentParser.Token token = parser.currentToken();

        if (token == XContentParser.Token.VALUE_STRING) {
            return fromList(Collections.singletonList(parser.text()));
        } else if (token == XContentParser.Token.START_ARRAY) {
            ArrayList<String> list = new ArrayList<>();
            while ((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
                list.add(parser.text());
            }
            return fromList(list);
        } else {
            throw new ParsingException(parser.getTokenLocation(),
                    "Expected [" + XContentParser.Token.VALUE_STRING + "] or ["
                            + XContentParser.Token.START_ARRAY + "] in [" + fieldName + "] but found [" + token + "]",
                    parser.getTokenLocation());
        }
    }

    public void toXContent(String preferredName, XContentBuilder builder) throws IOException {
        if (!fetchFields) {
            builder.field(preferredName, _NONE_);
        }
        if (fieldNames != null) {
            if (fieldNames.size() == 1) {
                builder.field(preferredName, fieldNames.get(0));
            } else {
                builder.startArray(preferredName);
                for (String fieldName : fieldNames) {
                    builder.value(fieldName);
                }
                builder.endArray();
            }
        }
    }
}
