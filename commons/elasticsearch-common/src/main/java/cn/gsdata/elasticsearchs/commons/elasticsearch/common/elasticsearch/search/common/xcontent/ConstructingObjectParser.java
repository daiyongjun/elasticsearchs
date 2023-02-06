package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent;


import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 有感：ConstructingObjectParser与target关系
 * 有点类似与之前定义ExtendedBounds（已经修订成Double[]形式了），定义了两套ObjectParser，但是这个类更高级，将院原先定义的两套ObjectParser改成一套
 * <p>
 * ConstructingObjectParser<Context, Value> 和ObjectParser<Context, Value>  类似，但适用于具有构造函数的对象
 * ConstructingObjectParser} 按照它们在 XContent 中的顺序解析字段，收集构造函数参数并解析和排队普通字段，直到解析所有构造函数参数。
 * 然后它构建目标对象并重放排队的字段。在最后一个构造函数参数之后进入的任何字段都会被解析并立即应用于目标对象
 *
 * @author daiyongjun
 */
public class ConstructingObjectParser<Context, Value> extends AbstractObjectParser<Context, Value> {

    /**
     * 构造函数参数列表信息
     */
    private final List<ConstructorArgInfo> constructorArgInfo = new ArrayList<>();

    /**
     * targetObject 上的字段数
     */
    private int numberOfFields = 0;


    /**
     * ConstructingObjectParser内置ObjectParser<Context, Target>属性，下述的double[]格式与Target的格式类似，我们将构造方法需要的参数
     * 参考:将多个参数作为double[]的列进行存储。
     * private static final ObjectParser<Void, double[]> EXTENDED_BOUNDS_PARSER = new ObjectParser<>(
     * EXTENDED_BOUNDS_FIELD.getPreferredName(),
     * () -> new double[]{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY});
     * static {
     * EXTENDED_BOUNDS_PARSER.declareDouble((bounds, d) -> bounds[0] = d, new ParseField("min"));
     * EXTENDED_BOUNDS_PARSER.declareDouble((bounds, d) -> bounds[1] = d, new ParseField("max"));
     * }
     * static{
     * PARSER.declareField((histogram, extendedBounds) -> {histogram.extendedBounds(extendedBounds[0], extendedBounds[1]);},
     * parser -> EXTENDED_BOUNDS_PARSER.apply(parser, null), EXTENDED_BOUNDS_FIELD, ObjectParser.ValueType.OBJECT);
     * }
     */
    private final ObjectParser<Context, Target> objectParser;


    private final BiFunction<Object[], Context, Value> builder;

    public ConstructingObjectParser(String name, Function<Object[], Value> builder) {
        this(name, (args, context) -> builder.apply(args));
    }

    public ConstructingObjectParser(String name, BiFunction<Object[], Context, Value> builder) {
        this.objectParser = new ObjectParser<>(name, null);
        this.builder = builder;
    }

    @Override
    public String getName() {
        return objectParser.getName();
    }

    @Override
    public <T> void declareField(BiConsumer<Value, T> consumer, ContextParser<Context, T> parser, ParseField parseField, ValueType type) {
        if (consumer == null) {
            throw new IllegalArgumentException("[consumer] is required");
        }
        if (parser == null) {
            throw new IllegalArgumentException("[parser] is required");
        }
        if (parseField == null) {
            throw new IllegalArgumentException("[parseField] is required");
        }
        if (type == null) {
            throw new IllegalArgumentException("[type] is required");
        }
        if (consumer == REQUIRED_CONSTRUCTOR_ARG_MARKER || consumer == OPTIONAL_CONSTRUCTOR_ARG_MARKER) {
            //记录声明参数中，参数标记为构造方法的参数，同时标记参数的顺序
            int position = constructorArgInfo.size();
            boolean required = consumer == REQUIRED_CONSTRUCTOR_ARG_MARKER;
            constructorArgInfo.add(new ConstructorArgInfo(parseField, required));
            objectParser.declareField((target, v) -> target.constructorArg(position, parseField, v), parser, parseField, type);
        } else {
            //记录声明参数中，参数标记为非构造方法的参数，记录相关数量
            numberOfFields += 1;
            objectParser.declareField(queueingConsumer(consumer, parseField), parser, parseField, type);
        }
    }

    @Override
    public Value parse(XContentParser parser, Context context) throws IOException {
        return objectParser.parse(parser, context, new Target(parser, context)).finish();
    }

    @Override
    public Value apply(XContentParser parser, Context context) {
        try {
            return parse(parser, context);
        } catch (IOException e) {
            throw new ParsingException(parser.getTokenLocation(), "[" + objectParser.getName() + "] failed to parse object", e);
        }
    }

    /**
     * 将参数标记为构造方法的参数(参数为必传参数)
     *
     * @param <V> 声明的范型V
     * @param <T> 声明的范型T
     * @return BiConsumer<V, T>
     */
    @SuppressWarnings("unchecked")
    public static <V, T> BiConsumer<V, T> constructorArg() {
        return (BiConsumer<V, T>) REQUIRED_CONSTRUCTOR_ARG_MARKER;
    }

    /**
     * 将参数标记为构造方法的参数(参数为可选参数)
     *
     * @param <V> 声明的范型V
     * @param <T> 声明的范型T
     * @return BiConsumer<V, T>
     */
    @SuppressWarnings("unchecked")
    public static <V, T> BiConsumer<V, T> optionalConstructorArg() {
        return (BiConsumer<V, T>) OPTIONAL_CONSTRUCTOR_ARG_MARKER;
    }

    /**
     * 将字段标记为构造函数参数(必须传递参数)
     */
    private static final BiConsumer<?, ?> REQUIRED_CONSTRUCTOR_ARG_MARKER = (a, b) -> {
        throw new UnsupportedOperationException("I am just a marker I should never be called.");
    };

    /**
     * 将字段标记为构造函数参数(可选参数)
     */
    private static final BiConsumer<?, ?> OPTIONAL_CONSTRUCTOR_ARG_MARKER = (a, b) -> {
        throw new UnsupportedOperationException("I am just a marker I should never be called.");
    };

    /**
     * 创建一个targetObject 队列的消费队列，队列存储targetObject中的field字段
     *
     * @param consumer   BiConsumer<Value, T> 字段声明的consumer
     * @param parseField ParseField 字段信息
     * @param <T>        声明的泛型类
     * @return BiConsumer<Target, T>
     */
    private <T> BiConsumer<Target, T> queueingConsumer(BiConsumer<Value, T> consumer, ParseField parseField) {
        return (target, v) -> {
            if (target.targetObject != null) {
                consumer.accept(target.targetObject, v);
                return;
            }
            XContentLocation location = target.parser.getTokenLocation();
            target.queue(targetObject -> {
                try {
                    consumer.accept(targetObject, v);
                } catch (Exception e) {
                    throw new ParsingException(location,
                            "[" + objectParser.getName() + "] failed to parse field [" + parseField.getPreferredName() + "]", e);
                }
            });
        };
    }

    /**
     * 用于存储构造方法中的所有参数类,类似于new double[]类
     */
    private class Target {
        /**
         * 按Field字段声明解析规则后的值，并储存在Object[]中
         */
        private final Object[] constructorArgs = new Object[constructorArgInfo.size()];
        /**
         * 用于记录当前收集了多少个构造函数的参数
         */
        private int constructorArgsCollected = 0;

        /**
         * 目标对象的字段属性的Consumer<Value>队列
         */
        private Consumer<Value>[] queuedFields;

        /**
         * 已排队的字段数，queuedFields已经设置的值
         */
        private int queuedFieldsCount = 0;

        private Value targetObject;

        /**
         * 由ConstructingObjectParser<Context, Value>传递的XContentParser类
         */
        private final XContentParser parser;

        /**
         * 由ConstructingObjectParser<Context, Value>传递的Context类
         */
        private final Context context;

        Target(XContentParser parser, Context context) {
            this.parser = parser;
            this.context = context;
        }

        /**
         * PARSER.declareField(optionalConstructorArg(), longOrString, MIN_FIELD, ObjectParser.ValueType.LONG_OR_NULL);
         * 按上述声明的解析方式解析Filed字段，并将Filed字段写入Target中
         *
         * @param position   int 参数所在构造方法的位置,按declaredField的顺序
         * @param parseField ParseField
         * @param value      Object 字段解析后的值
         */
        private void constructorArg(int position, ParseField parseField, Object value) {
            if (constructorArgs[position] != null) {
                throw new IllegalArgumentException("Can't repeat param [" + parseField + "]");
            }
            constructorArgs[position] = value;
            constructorArgsCollected++;
            //构造方法当设置到最后一个值时，设置targetObject的值
            if (constructorArgsCollected == constructorArgInfo.size()) {
                buildTarget();
            }
        }

        private void queue(Consumer<Value> queueMe) {
            assert targetObject == null : "Don't queue after the targetObject has been built! Just apply the consumer directly.";
            if (queuedFields == null) {
                @SuppressWarnings("unchecked")
                Consumer<Value>[] queuedFields = new Consumer[numberOfFields];
                this.queuedFields = queuedFields;
            }
            queuedFields[queuedFieldsCount] = queueMe;
            queuedFieldsCount++;
        }

        /**
         * 设置TargetObject<Value>的值
         */
        private void buildTarget() {
            this.targetObject = builder.apply(constructorArgs, context);
        }

        /**
         * 获取targetObject,如果targetObject==null,则打印constructorArgs中必传参数是否异常
         *
         * @return Value
         */
        private Value finish() {
            if (targetObject != null) {
                return targetObject;
            }
            StringBuilder message = null;
            //获取声明的constructorArgs构造函数参数是否为必传，解析过程中是否异常
            for (int i = 0; i < constructorArgs.length; i++) {
                if (constructorArgs[i] != null) {
                    continue;
                }
                ConstructorArgInfo arg = constructorArgInfo.get(i);
                //非必须传递参数
                if (!arg.required) {
                    continue;
                }
                if (message == null) {
                    message = new StringBuilder("Required [").append(arg.field);
                } else {
                    message.append(", ").append(arg.field);
                }
            }
            if (message != null) {
                // There were non-optional constructor arguments missing.
                throw new IllegalArgumentException(message.append(']').toString());
            }
            //排除constructorArgInfo所有参数为空
            assert !constructorArgInfo.isEmpty() : "[" + objectParser.getName() + "] must configure at least one constructor "
                    + "argument. If it doesn't have any it should use ObjectParser instead of ConstructingObjectParser. This is a bug "
                    + "in the parser declaration.";
            //重新构建Target的对象
            buildTarget();
            return targetObject;
        }
    }

    /**
     * 构造器中参数基本信息，ParseField 与 required（是否为必须传递参数）
     */
    private static class ConstructorArgInfo {
        final ParseField field;
        final boolean required;

        ConstructorArgInfo(ParseField field, boolean required) {
            this.field = field;
            this.required = required;
        }
    }
}