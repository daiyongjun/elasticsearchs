package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.script;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/modules-scripting-using.html
 * 基础的语法格式：
 * "script": {
 * "lang":   "...",
 * "source" | "id": "...",
 * "params": { ... }
 * }
 * lang: 脚本编写的语言，默认为painless。
 * source|id: 脚本本身，可以指定为source内联脚本或id存储脚本。
 * params: 应该传递到脚本中的任何命名参数。
 *
 * @author daiyongjun
 */
public class Script implements ToXContent {
    /**
     * 默认脚本语言的名称。
     */
    public static final String DEFAULT_SCRIPT_LANG = "painless";
    /**
     * 用于模板的编译器选项。
     */
    public static final String CONTENT_TYPE_OPTION = "content_type";

    public static final ParseField SCRIPT_PARSE_FIELD = new ParseField("script");
    public static final ParseField LANG_PARSE_FIELD = new ParseField("lang");
    public static final ParseField SOURCE_PARSE_FIELD = new ParseField("source");
    public static final ParseField OPTIONS_PARSE_FIELD = new ParseField("options");
    public static final ParseField PARAMS_PARSE_FIELD = new ParseField("params");

    /**
     * 定义的id/source的枚举类型
     */
    private final ScriptType type;
    /**
     * lang: 脚本编写的语言，默认为painless。
     */
    private final String lang;
    /**
     * source|id: 的字符串值，用于获取ScriptType的枚举类型
     */
    private final String idOrCode;
    /**
     * 内部的脚本配置，不支持对外修改
     */
    private final Map<String, String> options;
    /**
     * params: 应该传递到脚本中的任何命名参数。
     */
    private final Map<String, Object> params;

    public Script(ScriptType type, String lang, String idOrCode, Map<String, Object> params) {
        this(type, lang, idOrCode, type == ScriptType.INLINE ? Collections.emptyMap() : null, params);
    }

    public Script(ScriptType type, String lang, String idOrCode, Map<String, String> options, Map<String, Object> params) {
        this.type = type;
        this.lang = lang;
        this.idOrCode = idOrCode;
        this.options = options;
        this.params = params;
    }

    private static final ObjectParser<Void, ScriptBuilder> PARSER = new ObjectParser<>("script", ScriptBuilder::new);

    static {
        //设置Script的构建类ScriptBuilder
        PARSER.declareField(ScriptBuilder::setInline, parser -> parser, ScriptType.INLINE.getParseField(), AbstractObjectParser.ValueType.OBJECT_OR_STRING);
        PARSER.declareString(ScriptBuilder::setStored, ScriptType.STORED.getParseField());
        PARSER.declareString(ScriptBuilder::setLang, LANG_PARSE_FIELD);
        PARSER.declareField(ScriptBuilder::setOptions, XContentParser::mapStrings, OPTIONS_PARSE_FIELD, AbstractObjectParser.ValueType.OBJECT);
        PARSER.declareField(ScriptBuilder::setParams, XContentParser::map, PARAMS_PARSE_FIELD, AbstractObjectParser.ValueType.OBJECT);
    }

    public static Script parse(XContentParser parser) throws IOException {
        return parse(parser, DEFAULT_SCRIPT_LANG);
    }

    public static Script parse(XContentParser parser, String defaultLang) throws IOException {
        Objects.requireNonNull(defaultLang);
        XContentParser.Token token = parser.currentToken();
        if (token == null) {
            token = parser.nextToken();
        }
        if (token == XContentParser.Token.VALUE_STRING) {
            return new Script(ScriptType.INLINE, defaultLang, parser.text(), Collections.emptyMap());
        }
        return PARSER.apply(parser, null).build(defaultLang);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        builder.startObject();
        String contentType = options == null ? null : options.get(CONTENT_TYPE_OPTION);
        if (type == ScriptType.INLINE) {
            if (contentType != null && builder.contentType().mediaType().equals(contentType)) {
                builder.field(SOURCE_PARSE_FIELD.getPreferredName(), idOrCode);
            } else {
                builder.field(SOURCE_PARSE_FIELD.getPreferredName(), idOrCode);
            }
        } else {
            builder.field("id", idOrCode);
        }
        if (lang != null) {
            builder.field(LANG_PARSE_FIELD.getPreferredName(), lang);
        }
        if (options != null && !options.isEmpty()) {
            builder.field(OPTIONS_PARSE_FIELD.getPreferredName(), options);
        }
        if (!params.isEmpty()) {
            builder.field(PARAMS_PARSE_FIELD.getPreferredName(), params);
        }
        builder.endObject();
        return builder;
    }
}
