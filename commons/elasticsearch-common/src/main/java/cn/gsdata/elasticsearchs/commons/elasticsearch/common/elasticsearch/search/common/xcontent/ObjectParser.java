package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent;


import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * ObjectParser<Context, Value> ，使用XContentParser解析成指定Object,内置支持绑定Field以及FieldParser解析器
 * 拓展知识Lambda表达式：Class::method的写法分两种情况
 * 情况一、method是静态方法表达的意思是 (args) -> Class.method(args);
 * 情况二、method是非静态方法表达的意思是 (class,args) -> class.method(args);
 *
 * @author daiyongjun
 */
public class ObjectParser<Context, Value> extends AbstractObjectParser<Context, Value> {
    /**
     * ObjectParser的Object的名称，name用于异常信息的打印
     */
    private final String name;

    /**
     * ObjectParse内置的声明属性的解析方式以及属性内容注入Object方式（set/get方式注入或者理解是单个字段设置）
     */
    private final Map<String, FieldParser<Context, Value>> fieldParserMap = new HashMap<>();

    /**
     * 设置ContentParse<Context,Value> 预设值Object的实体
     * 如HistogramAggregationBuilder 的属性EXTENDED_BOUNDS_PARSER
     * private static final ObjectParser<double[], Void> EXTENDED_BOUNDS_PARSER = new ObjectParser<>(
     * EXTENDED_BOUNDS_FIELD.getPreferredName(),
     * () -> new double[]{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY});
     * 其中的Object实体类则是new double[]{}
     * 设置属性的值，为Object (new double[]{}) (bounds,d) -> bounds[0] = d;(bounds, d) -> bounds[1] = d
     * EXTENDED_BOUNDS_PARSER.declareDouble((bounds, d) -> bounds[0] = d, new ParseField("min"));
     * EXTENDED_BOUNDS_PARSER.declareDouble((bounds, d) -> bounds[1] = d, new ParseField("max"));
     * valueSupplier<Value> ->  (Value) get();
     */
    private final Supplier<Value> valueSupplier;

    /**
     * ObjectParser<Value,Context>的构造方法
     *
     * @param name String  预设ObjectParse的name属性值
     */
    public ObjectParser(String name) {
        this(name, null);
    }

    /**
     * ObjectParser<Value,Context>的构造方法
     *
     * @param name          String  预设ObjectParse的name属性值
     * @param valueSupplier Supplier<Value> 预设 ObjectParser<Context, Value> 中的Value（Object）类型
     */
    public ObjectParser(String name, Supplier<Value> valueSupplier) {
        this.name = name;
        this.valueSupplier = valueSupplier;
    }


    @Override
    public String getName() {
        return this.name;
    }

    public static <Value, ElementValue> BiConsumer<Value, List<ElementValue>> fromList(Class<ElementValue> c,
                                                                                       BiConsumer<Value, ElementValue[]> consumer) {
        return (Value v, List<ElementValue> l) -> {
            @SuppressWarnings("unchecked")
            ElementValue[] array = (ElementValue[]) Array.newInstance(c, l.size());
            consumer.accept(v, l.toArray(array));
        };
    }

    public void declareStringArray(BiConsumer<Value, List<String>> consumer, ParseField field) {
        declareField(consumer, (p, c) -> parseArray(p, p::text), field, ValueType.STRING_ARRAY);
    }

    /**
     * ContextParser<Context, Value> -> (Value) parse(XContentParser parse, Context context) throws IOException; 的实现类
     *
     * @param parser  XContentParser
     * @param context Context（预留字段）
     * @return Value
     * @throws IOException (Value)parse解析过程中异常
     */
    @Override
    public Value parse(XContentParser parser, Context context) throws IOException {
        if (valueSupplier == null) {
            throw new NullPointerException("valueSupplier is not set");
        }
        return parse(parser, context, valueSupplier.get());
    }

    /**
     * 重载ContextParser<Context, Value> 的 Value parse(XContentParser parser, Context context) throws IOException;的parse方法
     * 增加Value value参数(支持指定Object)Value parse(XContentParser parser, Value value, Context context) throws IOException;
     * ObjectParser<>.parse(parser,valueSupplier<T>，使用ObjectParser（支持内置Object中的属性内容绑定和属性解析器）为初始化的object(valueSupplier<T>)设置传入的属性
     *
     * @param parser  XContentParser内置的解析器（目前只拥有JsonParser）
     * @param context ContextParser拓展字段中的Context
     * @param value   内置初始化的Object信息
     * @return Value
     * @throws IOException 解析异常
     */
    public Value parse(XContentParser parser, Context context, Value value) throws IOException {
        XContentParser.Token token;
        if (parser.currentToken() == XContentParser.Token.START_OBJECT) {
            token = parser.currentToken();
        } else {
            token = parser.nextToken();
            if (token != XContentParser.Token.START_OBJECT) {
                throw new ParsingException(parser.getTokenLocation(), "[" + name + "] Expected START_OBJECT but was: " + token);
            }
        }
        FieldParser<Context, Value> fieldParser = null;
        String currentFieldName = null;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
                fieldParser = getParser(currentFieldName);
            } else {
                if (currentFieldName == null) {
                    throw new ParsingException(parser.getTokenLocation(), "[" + name + "] no field found");
                }
                if (fieldParser == null) {
                    parser.skipChildren(); // noop if parser points to a value, skips children if parser is start object or start array
                } else {
                    fieldParser.assertSupports(name, token, currentFieldName, parser.getTokenLocation());
                    parseField(parser, fieldParser, currentFieldName, context, value);
                }
                fieldParser = null;
            }
        }
        return value;
    }

    /**
     * 按指定的字段名称获取注册的解析器
     *
     * @param fieldName 字段名称
     * @return FieldParser
     */
    private FieldParser<Context, Value> getParser(String fieldName) {
        FieldParser<Context, Value> parser = fieldParserMap.get(fieldName);
        if (parser == null) {
            throw new IllegalArgumentException("[" + name + "] unknown field [" + fieldName + "], parser not found");
        }
        return parser;
    }

    /**
     * 解析Value(Object)内置的Field的具体的值，并将Field值设置到Value中
     *
     * @param parser           XContentParser   XContentParser内置的解析器（目前只拥有JsonParser）
     * @param fieldParser      FieldParser<Value, Context>  Value(Object)中声明field的字段的解析器
     * @param currentFieldName String   当前解析field字段名称
     * @param context          Context ContentParser<Context,Value>的待拓展字段
     * @param value            Value Value(object)
     * @throws IOException parseField 过程中产生的异常信息
     */
    private void parseField(XContentParser parser, FieldParser<Context, Value> fieldParser, String currentFieldName, Context context, Value value) throws IOException {
        final XContentParser.Token token = parser.currentToken();
        switch (token) {
            case START_OBJECT:
            case VALUE_STRING:
            case VALUE_NUMBER:
            case VALUE_BOOLEAN:
            case VALUE_EMBEDDED_OBJECT:
            case VALUE_NULL:
                parseValue(parser, fieldParser, currentFieldName, context, value);
                break;
            case START_ARRAY:
                parseArray(parser, fieldParser, currentFieldName, context, value);
                break;
            case END_OBJECT:
            case END_ARRAY:
            case FIELD_NAME:
                throw new ParsingException(parser.getTokenLocation(), "[" + name + "]" + token + " is unexpected");
            default:
                break;
        }
    }


    /**
     * 使用FieldParser<Context,Value> 内置的Parser<Context,Value> void parse(XContentParser parser, Context context, Value value) throws IOException;
     * 将Field解析并赋值给Value
     *
     * @param parser           XContentParser   XContentParser内置的解析器（目前只拥有JsonParser）
     * @param fieldParser      Value(object)声明的field的赋值逻辑与解析逻辑 如:HistogramAggregationBuilder::minDocCount,XContentParser::booleanValue
     * @param currentFieldName 当前解析field字段名称
     * @param context          Context ContentParser<Context,Value>的待拓展字段
     * @param value            Value Value(object)
     * @throws IOException parseField 过程中产生的异常信息
     */
    private void parseValue(XContentParser parser, FieldParser<Context, Value> fieldParser, String currentFieldName, Context context, Value value) throws IOException {
        try {
            fieldParser.parser().parse(parser, context, value);
        } catch (Exception ex) {
            throw new ParsingException(parser.getTokenLocation(), "[" + name + "] failed to parse field [" + currentFieldName + "]", ex);
        }
    }

    /**
     * 使用FieldParser<Context,Value> 内置的Parser<Context,Value> void parse(XContentParser parser, Context context, Value value) throws IOException;
     * 将Field解析并赋值给Value
     *
     * @param parser           XContentParser   XContentParser内置的解析器（目前只拥有JsonParser）
     * @param fieldParser      Value(object)声明的field的赋值逻辑与解析逻辑 如:HistogramAggregationBuilder::minDocCount,XContentParser::booleanValue
     * @param currentFieldName 当前解析field字段名称
     * @param context          Context ContentParser<Context,Value>的待拓展字段
     * @param value            Value Value(object)
     * @throws IOException parseField 过程中产生的异常信息
     */
    private void parseArray(XContentParser parser, FieldParser<Context, Value> fieldParser, String currentFieldName, Context context, Value value)
            throws IOException {
        assert parser.currentToken() == XContentParser.Token.START_ARRAY : "Token was: " + parser.currentToken();
        parseValue(parser, fieldParser, currentFieldName, context, value);
    }


    /**
     * BiFunction<T, U, R> 具体的实现类 R apply(T t, U u)
     * BiFunction<XContentParser, Context, Value> 具体的实现类 Value apply(XContentParser parser,Context context)
     *
     * @param parser  XContentParser   XContentParser内置的解析器（目前只拥有JsonParser）
     * @param context Context ContentParser<Context,Value>的待拓展字段
     * @return Value
     */
    @Override
    public Value apply(XContentParser parser, Context context) {
        if (valueSupplier == null) {
            throw new NullPointerException("valueSupplier is not set");
        }
        try {
            return parse(parser, context, valueSupplier.get());
        } catch (IOException e) {
            throw new ParsingException(parser.getTokenLocation(), "[" + name + "] failed to parse object", e);
        }
    }

    /**
     * 声明double类型的field
     *
     * @param consumer BiConsumer<Value, Double>   预设的当前字段解析成特定的值的逻辑 如:p.doubleValue();
     * @param field    ParseField   声明的Field的信息   new ParseField("field")
     */
    public void declareDouble(BiConsumer<Value, Double> consumer, ParseField field) {
        declareField(consumer, p -> p.doubleValue(), field, ValueType.DOUBLE);
    }

    /**
     * 声明Boolean类型的field
     *
     * @param consumer BiConsumer<Value, Boolean>   预设的当前字段解析成特定的值的逻辑 如:XContentParser::booleanValue
     * @param field    ParseField   声明的Field的信息   new ParseField("field")
     */
    public void declareBoolean(BiConsumer<Value, Boolean> consumer, ParseField field) {
        declareField(consumer, XContentParser::booleanValue, field, ValueType.BOOLEAN);
    }

    /**
     * 声明Long类型的field
     *
     * @param consumer BiConsumer<Value, Long>   预设的当前字段解析成特定的值的逻辑 如:p.longValue()
     * @param field    ParseField   声明的Field的信息   new ParseField("field")
     */
    public void declareLong(BiConsumer<Value, Long> consumer, ParseField field) {
        declareField(consumer, p -> p.longValue(), field, ValueType.LONG);
    }

    /**
     * 声明ObjectArray类型的field
     *
     * @param consumer     BiConsumer<Value, List<T>>
     * @param objectParser ContextParser<Context, T>
     * @param field        ParseField
     * @param <T>          范形T类型声明
     */
    public <T> void declareObjectArray(BiConsumer<Value, List<T>> consumer, ContextParser<Context, T> objectParser, ParseField field) {
        declareField(consumer, (p, c) -> parseArray(p, () -> objectParser.parse(p, c)), field, ValueType.OBJECT_ARRAY);
    }

    /**
     * 声明ObjectArray类型中间处理类
     *
     * @param parser   XContentParser
     * @param supplier IOSupplier<T>
     * @param <T>      T 声明的范型T
     * @return List<T>
     * @throws IOException 数据解析异常
     */
    private static <T> List<T> parseArray(XContentParser parser, IOSupplier<T> supplier) throws IOException {
        List<T> list = new ArrayList<>();
        if (parser.currentToken().isValue() || parser.currentToken() == XContentParser.Token.START_OBJECT) {
            list.add(supplier.get());
        } else {
            while (parser.nextToken() != XContentParser.Token.END_ARRAY) {
                if (parser.currentToken().isValue() || parser.currentToken() == XContentParser.Token.START_OBJECT) {
                    list.add(supplier.get());
                } else {
                    throw new IllegalStateException("expected value but got [" + parser.currentToken() + "]");
                }
            }
        }
        return list;
    }

    /**
     * Supplier<T> -> T get();
     * IOSupplier<T> -> T get() throws IOException;
     *
     * @param <T>
     */
    private interface IOSupplier<T> {
        /**
         * 获取定义范型的类T
         *
         * @return T
         * @throws IOException 获取范型类T异常
         */
        T get() throws IOException;
    }

    @Override
    public <T> void declareField(BiConsumer<Value, T> consumer, ContextParser<Context, T> parser, ParseField parseField,
                                 ValueType type) {
        if (consumer == null) {
            throw new IllegalArgumentException("[consumer] is required");
        }
        if (parser == null) {
            throw new IllegalArgumentException("[parser] is required");
        }
        setFieldParserMap((p, c, v) -> consumer.accept(v, parser.parse(p, c)), parseField, type);
    }

    /**
     * 设置ObjectParser<Context,Value>内置的FieldParserMap属性Map<String, FieldParser<Context, Value>>
     *
     * @param parser     (context,value) -> consumer.accept(v, parser.parse(p) -> (p, v) -> v.method1(p.method2());绑定逻辑
     * @param parseField 待声明的Field的信息   new ParseField("field")
     * @param type       Field的字段类型 ValueType.STRING
     */
    public void setFieldParserMap(Parser<Context, Value> parser, ParseField parseField, ValueType type) {
        if (parseField == null) {
            throw new IllegalArgumentException("[parseField] is required");
        }
        if (type == null) {
            throw new IllegalArgumentException("[type] is required");
        }
        FieldParser<Context, Value> fieldParser = new FieldParser<>(parser, type.supportedTokens(), parseField, type);
        for (String fieldValue : parseField.getAllNamesIncludedDeprecated()) {
            fieldParserMap.putIfAbsent(fieldValue, fieldParser);
        }
    }


    public <T> void declareNamedObjects(BiConsumer<Value, List<T>> consumer, NamedObjectParser<T, Context> namedObjectParser,
                                        ParseField field) {
        Consumer<Value> orderedModeCallback = (v) -> {
            throw new IllegalArgumentException("[" + field + "] doesn't support arrays. Use a single object with multiple fields.");
        };
        declareNamedObjects(consumer, namedObjectParser, orderedModeCallback, field);
    }

    public <T> void declareNamedObjects(BiConsumer<Value, List<T>> consumer, NamedObjectParser<T, Context> namedObjectParser,
                                        Consumer<Value> orderedModeCallback, ParseField field) {
        // This creates and parses the named object
        BiFunction<XContentParser, Context, T> objectParser = (XContentParser p, Context c) -> {
            if (p.currentToken() != XContentParser.Token.FIELD_NAME) {
                throw new ParsingException(p.getTokenLocation(), "[" + field + "] can be a single object with any number of "
                        + "fields or an array where each entry is an object with a single field");
            }
            // This messy exception nesting has the nice side effect of telling the use which field failed to parse
            try {
                String name = p.currentName();
                try {
                    return namedObjectParser.parse(p, c, name);
                } catch (Exception e) {
                    throw new ParsingException(p.getTokenLocation(), "[" + field + "] failed to parse field [" + name + "]", e);
                }
            } catch (IOException e) {
                throw new ParsingException(p.getTokenLocation(), "[" + field + "] error while parsing", e);
            }
        };
        declareField((XContentParser p, Context c, Value v) -> {
            List<T> fields = new ArrayList<>();
            XContentParser.Token token;
            if (p.currentToken() == XContentParser.Token.START_OBJECT) {
                // Fields are just named entries in a single object
                while ((token = p.nextToken()) != XContentParser.Token.END_OBJECT) {
                    fields.add(objectParser.apply(p, c));
                }
            } else if (p.currentToken() == XContentParser.Token.START_ARRAY) {
                // Fields are objects in an array. Each object contains a named field.
                orderedModeCallback.accept(v);
                while ((token = p.nextToken()) != XContentParser.Token.END_ARRAY) {
                    if (token != XContentParser.Token.START_OBJECT) {
                        throw new ParsingException(p.getTokenLocation(), "[" + field + "] can be a single object with any number of "
                                + "fields or an array where each entry is an object with a single field");
                    }
                    p.nextToken(); // Move to the first field in the object
                    fields.add(objectParser.apply(p, c));
                    p.nextToken(); // Move past the object, should be back to into the array
                    if (p.currentToken() != XContentParser.Token.END_OBJECT) {
                        throw new ParsingException(p.getTokenLocation(), "[" + field + "] can be a single object with any number of "
                                + "fields or an array where each entry is an object with a single field");
                    }
                }
            }
            consumer.accept(v, fields);
        }, field, ValueType.OBJECT_ARRAY);
    }

    public void declareField(Parser<Context, Value> p, ParseField parseField, ValueType type) {
        if (parseField == null) {
            throw new IllegalArgumentException("[parseField] is required");
        }
        if (type == null) {
            throw new IllegalArgumentException("[type] is required");
        }
        FieldParser fieldParser = new FieldParser(p, type.supportedTokens(), parseField, type);
        for (String fieldValue : parseField.getAllNamesIncludedDeprecated()) {
            fieldParserMap.putIfAbsent(fieldValue, fieldParser);
        }
    }

    /**
     * 用于实例化和解析命名对象的功能接口。请参阅 ObjectParserTestsNamedObject 以了解为本身具有解析器的对象实现此功能的规范方法。
     */
    @FunctionalInterface
    public interface NamedObjectParser<T, Context> {
        T parse(XContentParser p, Context c, String name) throws IOException;
    }

    /**
     * 与XContentParser<Value,Content> Value Parse(XContentParser parser,Context context)类似
     * 不同的是Parser<Value, Context> void parse(XContentParser parser, Value value, Context context)没有返回值
     * setXXX(Field xxx){this.field = xxx;return this} setXXX(Field xxx,This this){this.field}的区别
     * 由此Parser<Value, Context> 应用与FieldParse中，用于给Value进行赋值
     *
     * @param <Context>
     * @param <Value>
     * @author daiyongjun
     */
    @FunctionalInterface
    public interface Parser<Context, Value> {
        /**
         * 使用XContent解析器，解析指定<Value> field的值，并设置到Value中
         *
         * @param parser  指定XContent解析器
         * @param context Parser日常扩展字段
         * @param value   Value
         * @throws IOException 解析异常
         */
        void parse(XContentParser parser, Context context, Value value) throws IOException;
    }
}


