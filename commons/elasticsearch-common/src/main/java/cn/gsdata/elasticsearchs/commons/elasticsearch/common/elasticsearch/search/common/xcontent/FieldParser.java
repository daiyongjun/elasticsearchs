package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ObjectParser.Parser;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * FieldParser<Context, Value> Value内的Field解析类，内置Parser<Context, Value> parser{  void parse(XContentParser parser, Value value, Context context) throws IOException;}
 * 将XContentParser parser中的Filed信息解析出来并void setField(Field field,Value value) {value.field= field}
 *
 * @param <Context>
 * @param <Value>
 * @author daiyongjun
 */
class FieldParser<Context, Value> {
    private final Parser<Context, Value> parser;
    private final EnumSet<XContentParser.Token> supportedTokens;
    private final ParseField parseField;
    private final ObjectParser.ValueType type;

    FieldParser(Parser<Context, Value> parser, EnumSet<XContentParser.Token> supportedTokens, ParseField parseField, ObjectParser.ValueType type) {
        this.parser = parser;
        this.supportedTokens = supportedTokens;
        this.parseField = parseField;
        this.type = type;
    }

    public Parser<Context, Value> parser() {
        return this.parser;
    }

    void assertSupports(String parserName, XContentParser.Token token, String currentFieldName, XContentLocation location) {
        if (!parseField.match(currentFieldName)) {
            throw new ParsingException(location, "[" + parserName + "] parsefield doesn't accept: " + currentFieldName);
        }
        if (!supportedTokens.contains(token)) {
            throw new ParsingException(location,
                    "[" + parserName + "] " + currentFieldName + " doesn't support values of type: " + token);
        }
    }

    @Override
    public String toString() {
        String[] deprecatedNames = parseField.getDeprecatedNames();
        String allReplacedWith = parseField.getAllReplacedWith();
        String deprecated = "";
        if (deprecatedNames != null && deprecatedNames.length > 0) {
            deprecated = ", deprecated_names=" + Arrays.toString(deprecatedNames);
        }
        return "FieldParser{" +
                "preferred_name=" + parseField.getPreferredName() +
                ", supportedTokens=" + supportedTokens +
                deprecated +
                (allReplacedWith == null ? "" : ", replaced_with=" + allReplacedWith) +
                ", type=" + type.name() +
                '}';
    }
}
