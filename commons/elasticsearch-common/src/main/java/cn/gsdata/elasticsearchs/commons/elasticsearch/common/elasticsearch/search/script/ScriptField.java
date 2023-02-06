package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.script;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ToXContent;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.sort.ScriptSortBuilder;

import java.io.IOException;

/**
 * 基于脚本自定义相关字段
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/search-request-script-fields.html
 * {
 * "query" : {
 * "match_all": {}
 * },
 * "script_fields" : {
 * "test1" : {
 * "script" : {
 * "lang": "painless",
 * "source": "doc['my_field_name'].value * 2"
 * }
 * }
 * }
 * }
 *
 * @author daiyongjun
 */
public class ScriptField implements ToXContent {
    public static final ParseField IGNORE_FAILURE_FIELD = new ParseField("ignore_failure");
    /**
     * 是否忽视错误情况
     */
    private final boolean ignoreFailure;
    /**
     * 自定义字段的名称
     */
    private final String fieldName;
    /**
     * script 的内容，有lang、source、params
     */
    private final Script script;

    public ScriptField(String fieldName, Script script, boolean ignoreFailure) {
        this.fieldName = fieldName;
        this.script = script;
        this.ignoreFailure = ignoreFailure;
    }

    public ScriptField(XContentParser parser) throws IOException {
        boolean ignoreFailure = false;
        String scriptFieldName = parser.currentName();
        Script script = null;
        XContentParser.Token token;
        token = parser.nextToken();
        if (token == XContentParser.Token.START_OBJECT) {
            String currentFieldName = null;
            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    currentFieldName = parser.currentName();
                } else if (token.isValue()) {
                    if (ScriptSortBuilder.SCRIPT_FIELD.match(currentFieldName)) {
                        script = Script.parse(parser);
                    } else if (IGNORE_FAILURE_FIELD.match(currentFieldName)) {
                        ignoreFailure = parser.booleanValue();
                    } else {
                        throw new ParsingException(parser.getTokenLocation(), "Unknown key for a " + token + " in [" + currentFieldName
                                + "].", parser.getTokenLocation());
                    }
                } else if (token == XContentParser.Token.START_OBJECT) {
                    if (ScriptSortBuilder.SCRIPT_FIELD.match(currentFieldName)) {
                        script = Script.parse(parser);
                    } else {
                        throw new ParsingException(parser.getTokenLocation(), "Unknown key for a " + token + " in [" + currentFieldName
                                + "].", parser.getTokenLocation());
                    }
                } else {
                    throw new ParsingException(parser.getTokenLocation(), "Unknown key for a " + token + " in [" + currentFieldName
                            + "].", parser.getTokenLocation());
                }
            }
            this.ignoreFailure = ignoreFailure;
            this.fieldName = scriptFieldName;
            this.script = script;
        } else {
            throw new ParsingException(parser.getTokenLocation(), "Expected [" + XContentParser.Token.START_OBJECT + "] in ["
                    + parser.currentName() + "] but found [" + token + "]", parser.getTokenLocation());
        }
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        builder.startObject(fieldName);
        builder.field(ScriptSortBuilder.SCRIPT_FIELD.getPreferredName(), script);
        builder.field(IGNORE_FAILURE_FIELD.getPreferredName(), ignoreFailure);
        builder.endObject();
        return builder;
    }
}
