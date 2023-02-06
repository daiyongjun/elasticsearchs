package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent;


import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.CheckedFunction;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;

import java.io.IOException;
import java.util.EnumSet;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * ContextParser<Context, Value>与BiFunction<XContentParser, Context, Value>的抽象是实现类
 * ContextParser<Context, Value> =====> (Value) parse(XContentParser parse, Context context) throws IOException;
 * BiFunction<XContentParser, Context, Value> =====> (Value) apply(XContentParser parse, Context context);
 * 具体的实现类ObjectParse<Context,Value>、ConstructingObjectParser<Context, Value>, 以上都是通过declareField方法声明Object的内置的field属性的值，两者不同的是通过不同方法Object的属性值的内容，前者使用的是set/get，后者使用的构造方法
 *
 * @author daiyongjun
 */
public abstract class AbstractObjectParser<Context, Value> implements BiFunction<XContentParser, Context, Value>, ContextParser<Context, Value> {

    /**
     * 获取当前ObjectParser具体实现的Object名称
     *
     * @return String
     */
    public abstract String getName();

    /**
     * parser.apply(p) 参数B.apply(p)
     * B.apply -> 预设的当前字段解析成特定的值的逻辑 如：p.doubleValue()、
     *
     * @param consumer   BiConsumer<Value, T> -> void accept(Context context, T t);
     * @param parser     CheckedFunction<XContentParser, T, IOException> -> T apply(XContentParser parser) throws IOException;
     * @param parseField 声明的Field的信息   new ParseField("field")
     * @param type       Field的字段类型 ValueType
     * @param <T>        声明范型T
     */
    public <T> void declareField(BiConsumer<Value, T> consumer, CheckedFunction<XContentParser, T, IOException> parser, ParseField parseField, ValueType type) {
        if (parser == null) {
            throw new IllegalArgumentException("[parser] is required");
        }
        declareField(consumer, (p, c) -> parser.apply(p), parseField, type);
    }


    public void declareInt(BiConsumer<Value, Integer> consumer, ParseField field) {
        declareField(consumer, p -> p.intValue(), field, ValueType.INT);
    }

    public void declareFloat(BiConsumer<Value, Float> consumer, ParseField field) {
        declareField(consumer, p -> p.floatValue(), field, ValueType.FLOAT);
    }

    public void declareString(BiConsumer<Value, String> consumer, ParseField field) {
        declareField(consumer, XContentParser::text, field, ValueType.STRING);
    }

    public <T> void declareObject(BiConsumer<Value, T> consumer, ContextParser<Context, T> objectParser, ParseField field) {
        declareField(consumer, (p, c) -> objectParser.parse(p, c), field, ValueType.OBJECT);
    }

    /**
     * consumer.accept(v, parser.parse(p, c))  参数A.accept(Value value,参数B.parse(p, c))
     * A.accpet -> 预设设置Value(Object)属性的值的逻辑
     * B.parse -> 预设的当前字段解析成特定的值的逻辑
     * A.accept(Value value,参数B.parse(p, c)) -> Value(Object)设置属性值为将字段解析的值
     *
     * @param consumer   BiConsumer<Value, T> -> void accept(Context context, T t);
     * @param parser     ContextParser<Context, T> -> T parse(XContentParser parser, Context context) throws IOException;
     * @param parseField 声明的Field的信息   new ParseField("field")
     * @param type       Field的字段类型 ValueType
     * @param <T>        声明范型T
     */
    public abstract <T> void declareField(BiConsumer<Value, T> consumer, ContextParser<Context, T> parser, ParseField parseField,
                                          ValueType type);

    /**
     * Object 内置的Field的枚举类型
     */
    public enum ValueType {
        /**
         * Object 声明的Field的类型
         */
        STRING(XContentParser.Token.VALUE_STRING),
        STRING_OR_NULL(XContentParser.Token.VALUE_STRING, XContentParser.Token.VALUE_NULL),
        FLOAT(XContentParser.Token.VALUE_NUMBER, XContentParser.Token.VALUE_STRING),
        FLOAT_OR_NULL(XContentParser.Token.VALUE_NUMBER, XContentParser.Token.VALUE_STRING, XContentParser.Token.VALUE_NULL),
        DOUBLE(XContentParser.Token.VALUE_NUMBER, XContentParser.Token.VALUE_STRING),
        DOUBLE_OR_NULL(XContentParser.Token.VALUE_NUMBER, XContentParser.Token.VALUE_STRING, XContentParser.Token.VALUE_NULL),
        LONG(XContentParser.Token.VALUE_NUMBER, XContentParser.Token.VALUE_STRING),
        LONG_OR_NULL(XContentParser.Token.VALUE_NUMBER, XContentParser.Token.VALUE_STRING, XContentParser.Token.VALUE_NULL),
        INT(XContentParser.Token.VALUE_NUMBER, XContentParser.Token.VALUE_STRING),
        BOOLEAN(XContentParser.Token.VALUE_BOOLEAN, XContentParser.Token.VALUE_STRING),
        STRING_ARRAY(XContentParser.Token.START_ARRAY, XContentParser.Token.VALUE_STRING),
        FLOAT_ARRAY(XContentParser.Token.START_ARRAY, XContentParser.Token.VALUE_NUMBER, XContentParser.Token.VALUE_STRING),
        DOUBLE_ARRAY(XContentParser.Token.START_ARRAY, XContentParser.Token.VALUE_NUMBER, XContentParser.Token.VALUE_STRING),
        LONG_ARRAY(XContentParser.Token.START_ARRAY, XContentParser.Token.VALUE_NUMBER, XContentParser.Token.VALUE_STRING),
        INT_ARRAY(XContentParser.Token.START_ARRAY, XContentParser.Token.VALUE_NUMBER, XContentParser.Token.VALUE_STRING),
        BOOLEAN_ARRAY(XContentParser.Token.START_ARRAY, XContentParser.Token.VALUE_BOOLEAN),
        OBJECT(XContentParser.Token.START_OBJECT),
        OBJECT_ARRAY(XContentParser.Token.START_OBJECT, XContentParser.Token.START_ARRAY),
        OBJECT_OR_BOOLEAN(XContentParser.Token.START_OBJECT, XContentParser.Token.VALUE_BOOLEAN),
        OBJECT_OR_STRING(XContentParser.Token.START_OBJECT, XContentParser.Token.VALUE_STRING),
        OBJECT_ARRAY_BOOLEAN_OR_STRING(XContentParser.Token.START_OBJECT, XContentParser.Token.START_ARRAY, XContentParser.Token.VALUE_BOOLEAN, XContentParser.Token.VALUE_STRING),
        OBJECT_ARRAY_OR_STRING(XContentParser.Token.START_OBJECT, XContentParser.Token.START_ARRAY, XContentParser.Token.VALUE_STRING),
        VALUE(XContentParser.Token.VALUE_BOOLEAN, XContentParser.Token.VALUE_NULL, XContentParser.Token.VALUE_EMBEDDED_OBJECT, XContentParser.Token.VALUE_NUMBER, XContentParser.Token.VALUE_STRING),
        VALUE_OBJECT_ARRAY(XContentParser.Token.VALUE_BOOLEAN, XContentParser.Token.VALUE_NULL, XContentParser.Token.VALUE_EMBEDDED_OBJECT, XContentParser.Token.VALUE_NUMBER, XContentParser.Token.VALUE_STRING, XContentParser.Token.START_OBJECT, XContentParser.Token.START_ARRAY);

        private final EnumSet<XContentParser.Token> tokens;

        ValueType(XContentParser.Token first, XContentParser.Token... rest) {
            this.tokens = EnumSet.of(first, rest);
        }

        public EnumSet<XContentParser.Token> supportedTokens() {
            return this.tokens;
        }
    }
}
