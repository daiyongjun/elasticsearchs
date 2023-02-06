package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.script;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentFactory;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentType;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.script.Script.CONTENT_TYPE_OPTION;
import static cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.script.Script.OPTIONS_PARSE_FIELD;

/**
 * ScriptBuilder构建模式的构建类
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/modules-scripting-using.html
 *
 * @author daiyongjun
 */
public class ScriptBuilder {
    /**
     * 定义的id/source的枚举类型
     */
    private ScriptType type;
    /**
     * lang: 脚本编写的语言，默认为painless。
     */
    private String lang;
    /**
     * source|id: Script语法内容或者预先存储的script语法脚本的ID
     * 如：if((doc['field1'].value=='eq1') && (doc['field2'].value=='eq2')){//todo}
     */
    private String idOrCode;
    /**
     * 内部的脚本配置
     */
    private Map<String, String> options;
    /**
     * params: 传递到脚本中的任何命名参数。
     */
    private Map<String, Object> params;

    public ScriptBuilder() {
        this.options = new HashMap<>();
        this.params = Collections.emptyMap();
    }

    protected Script build(String defaultLang) {
        if (type == null) {
            throw new IllegalArgumentException("must specify either [source] for an inline script or [id] for a stored script");
        }
        if (type == ScriptType.INLINE) {
            if (lang == null) {
                lang = defaultLang;
            }
            if (idOrCode == null) {
                throw new IllegalArgumentException(
                        "must specify <id> for an inline script");
            }
            if (options.size() > 1 || options.size() == 1 && options.get(CONTENT_TYPE_OPTION) == null) {
                options.remove(CONTENT_TYPE_OPTION);
                throw new IllegalArgumentException("illegal compiler options [" + options + "] specified");
            }
        } else if (type == ScriptType.STORED) {
            if (lang != null) {
                throw new IllegalArgumentException(
                        "illegally specified <lang> for a stored script");
            }
            if (idOrCode == null) {
                throw new IllegalArgumentException(
                        "must specify <code> for a stored script");
            }

            if (options.isEmpty()) {
                options = null;
            } else {
                throw new IllegalArgumentException("field [" + OPTIONS_PARSE_FIELD.getPreferredName() + "] " +
                        "cannot be specified using a stored script");
            }
        }
        return new Script(type, lang, idOrCode, options, params);
    }


    /**
     * 由于内联脚本可以接受代码而不仅仅是一个 id，它们还必须能够处理模板解析，因此需要自定义解析代码。
     * 模板可以由 JSON 对象组成。如果发现 JSON 对象，则内容类型选项也必须保存为编译器选项。
     *
     * @param parser XContentParser
     */
    protected void setInline(XContentParser parser) {
        try {
            if (type != null) {
                throwOnlyOneOfType();
            }
            type = ScriptType.INLINE;
            if (parser.currentToken() == XContentParser.Token.START_OBJECT) {
                XContentBuilder builder = XContentFactory.jsonBuilder();
                idOrCode = builder.copyCurrentStructure(parser).string();
                options.put(CONTENT_TYPE_OPTION, XContentType.JSON.mediaType());
            } else {
                idOrCode = parser.text();
            }
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }


    /**
     * 设置存储脚本的 id 和类型。
     *
     * @param idOrCode String
     */
    protected void setStored(String idOrCode) {
        if (type != null) {
            throwOnlyOneOfType();
        }
        type = ScriptType.STORED;
        this.idOrCode = idOrCode;
    }

    /**
     * 设置lang: 脚本编写的语言，默认为painless。
     *
     * @param lang String
     */
    protected void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * 设置指定了内联模板
     *
     * @param options Map<String, String>
     */
    protected void setOptions(Map<String, String> options) {
        this.options.putAll(options);
    }

    /**
     * 设置params: 传递到脚本中的任何命名参数。
     *
     * @param params Map<String, Object> params
     */
    protected void setParams(Map<String, Object> params) {
        this.params = params;
    }

    /**
     * 如果指定了一种以上类型的ScriptType，抛出指定异常信息
     */
    private void throwOnlyOneOfType() {
        throw new IllegalArgumentException("must only use one of [" +
                ScriptType.INLINE.getParseField().getPreferredName() + ", " +
                ScriptType.STORED.getParseField().getPreferredName() + "]" +
                " when specifying a script");
    }
}
