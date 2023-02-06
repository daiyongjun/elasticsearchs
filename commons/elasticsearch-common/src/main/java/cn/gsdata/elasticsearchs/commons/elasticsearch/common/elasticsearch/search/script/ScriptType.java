package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.script;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;

/**
 * Script枚举类型,内置source/id两种类型
 *
 * @author daiyongjun
 */
public enum ScriptType {
    /**
     * 内置脚本类型
     */
    INLINE(0, new ParseField("source", "inline"), false),
    STORED(1, new ParseField("id", "stored"), false);
    /**
     * 枚举类型的ID
     */
    private final int id;
    /**
     * 定义枚举类型的source与id的ParseFile
     */
    private final ParseField parseField;
    /**
     * 默认值
     */
    private final boolean defaultEnabled;

    ScriptType(int id, ParseField parseField, boolean defaultEnabled) {
        this.id = id;
        this.parseField = parseField;
        this.defaultEnabled = defaultEnabled;
    }

    /**
     * 获取ParseField属性的值
     *
     * @return ParseField
     */
    public ParseField getParseField() {
        return parseField;
    }
}
