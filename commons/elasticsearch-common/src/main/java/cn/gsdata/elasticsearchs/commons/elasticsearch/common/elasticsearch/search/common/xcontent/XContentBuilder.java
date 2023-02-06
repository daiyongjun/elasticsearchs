package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent;


import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.*;

/**
 * 用于构建 XContent, Builder类似于构建者，可以理解为String和StringBuilder之间的关系。
 *
 * @author daiyongjun
 */
public final class XContentBuilder implements Closeable {


    /**
     * XContent内容输出流
     */
    private final OutputStream os;

    /***
     * XContent生成器（XContentGenerator），append(value())具体的详细操作，如generator.writeEndObject(); ....
     */
    private final XContentGenerator generator;

    /**
     * 注册Builder去拼接不同append(value)；b.value(Byte)、b.value(String)、b.value(Float)......
     */
    private static final Map<Class<?>, Writer> WRITERS;

    static {
        Map<Class<?>, Writer> writers = new HashMap<>();
        writers.put(Boolean.class, (b, v) -> b.value((Boolean) v));
        writers.put(Byte.class, (b, v) -> b.value((Byte) v));
        writers.put(Integer.class, (b, v) -> b.value((Integer) v));
        writers.put(Float.class, (b, v) -> b.value((Float) v));
        writers.put(String.class, (b, v) -> b.value((String) v));
        writers.put(String[].class, (b, v) -> b.values((String[]) v));
        WRITERS = Collections.unmodifiableMap(writers);
    }

    /**
     * XContentBuilder构造方法
     */
    public static XContentBuilder builder(XContent xContent) throws IOException {
        return new XContentBuilder(xContent, new ByteArrayOutputStream());
    }

    /**
     * XContentBuilder构造方法
     */
    public XContentBuilder(XContent xContent, OutputStream os) throws IOException {
        this.os = os;
        this.generator = xContent.createGenerator(os);
    }

    /**
     * 获取当前XContentBuilder 的contentType类型
     *
     * @return XContentType
     */
    public XContentType contentType() {
        return generator.contentType();
    }

    /**
     * xContentBuilder.append(XContent开头的内容）
     *
     * @return XContentBuilder
     * @throws IOException xContentBuilder.append(XContent开头的内容）异常
     */
    public XContentBuilder startObject() throws IOException {
        generator.writeStartObject();
        return this;
    }

    /**
     * xContentBuilder.append(XContent设置Filed的名称同时设置开头的内容）
     *
     * @param name 字段名称
     * @throws IOException xContentBuilder.append(XContent设置Filed的名称同时设置开头的内容）异常
     */
    public XContentBuilder startObject(String name) throws IOException {
        return field(name).startObject();
    }

    /**
     * xContentBuilder.append(XContent尾部的内容）
     *
     * @return XContentBuilder
     * @throws IOException xContentBuilder.append(XContent尾部的内容）异常
     */
    public XContentBuilder endObject() throws IOException {
        generator.writeEndObject();
        return this;
    }

    /**
     * xContentBuilder.append(XContent设置Filed的名称）
     *
     * @param name Field的名称
     * @throws IOException xContentBuilder.append(XContent设置Filed的名称）异常
     */
    public XContentBuilder field(String name) throws IOException {
        ensureNameNotNull(name);
        generator.writeFieldName(name);
        return this;
    }

    /**
     * xContentBuilder.append(Field以及Field对应的Value(Object类型)）
     *
     * @param name  Field
     * @param value Value(Object)
     * @throws IOException xContentBuilder.append(Field以及Field对应的Value(Object类型))发生的异常
     */
    public XContentBuilder field(String name, Object value) throws IOException {
        return field(name).value(value);
    }

    /**
     * xContentBuilder.append(Field以及Field对应的Value(String类型)）
     *
     * @param name  Field
     * @param value Value(String)
     * @throws IOException xContentBuilder.append(Field以及Field对应的Value(String类型)）发生的异常
     */
    public XContentBuilder field(String name, String value) throws IOException {
        if (value == null) {
            return nullField(name);
        }
        ensureNameNotNull(name);
        generator.writeStringField(name, value);
        return this;
    }

    /**
     * xContentBuilder.append(Field以及Field对应的Values( Iterable<?>)，集合）
     *
     * @param name   Field
     * @param values Values( Iterable<?>)
     * @throws IOException xContentBuilder.append(Field以及Field对应的Values( Iterable<?>)，集合）发生的异常
     */
    public XContentBuilder field(String name, Iterable<?> values) throws IOException {
        return field(name).value(values);
    }


    /**
     * xContentBuilder.append(指定field名称为NUll的同时并设置field为null所对应的value)
     *
     * @param name Value(String)
     * @throws IOException xContentBuilder.append(指定field名称为NUll的同时并设置field为null所对应的value)发生的异常
     */
    public XContentBuilder nullField(String name) throws IOException {
        ensureNameNotNull(name);
        generator.writeNullField(name);
        return this;
    }

    /**
     * xContentBuilder.append(Field对应的Value(Object类型)）
     *
     * @param value Value(Object)
     * @throws IOException xContentBuilder.append(Field以及Field对应的Value(Object类型))发生的异常
     */
    public XContentBuilder value(Object value) throws IOException {
        unknownValue(value);
        return this;
    }

    /**
     * xContentBuilder.append(Field对应的Value(String类型)）
     *
     * @param value Value(String)
     * @throws IOException xContentBuilder.append(Field以及Field对应的Value(String类型))发生的异常
     */
    public XContentBuilder value(String value) throws IOException {
        if (value == null) {
            return nullValue();
        }
        generator.writeString(value);
        return this;
    }

    /**
     * xContentBuilder.append(Field对应的Value(ToXContent类型)）
     *
     * @param value Value(ToXContent)
     * @throws IOException xContentBuilder.append(Field以及Field对应的Value(ToXContent类型))发生的异常
     */
    private XContentBuilder value(ToXContent value) throws IOException {
        return value.toXContent(this);
    }

    private XContentBuilder value(Iterable<?> values) throws IOException {
        if (values == null) {
            return nullValue();
        }

        if (values instanceof Path) {
            //treat as single value
            value((Path) values);
        } else {
            startArray();
            for (Object value : values) {
                unknownValue(value);
            }
            endArray();
        }
        return this;
    }

    /**
     * xContentBuilder.append(array的开始部分[）
     *
     * @throws IOException xContentBuilder.append(array的开始部分[)发生的异常
     */
    public XContentBuilder startArray() throws IOException {
        generator.writeStartArray();
        return this;
    }

    /**
     * xContentBuilder.append(array的开始部分的字段名称）
     *
     * @throws IOException xContentBuilder.append(array的开始部分[)发生的异常
     */
    public XContentBuilder startArray(String name) throws IOException {
        return field(name).startArray();
    }

    public XContentBuilder array(String name, String... values) throws IOException {
        return field(name).values(values);
    }

    private XContentBuilder values(String[] values) throws IOException {
        if (values == null) {
            return nullValue();
        }
        startArray();
        for (String s : values) {
            value(s);
        }
        endArray();
        return this;
    }

    /**
     * xContentBuilder.append(array的结束部分]）
     *
     * @throws IOException xContentBuilder.append(array的开始部分])发生的异常
     */
    public XContentBuilder endArray() throws IOException {
        generator.writeEndArray();
        return this;
    }

    /**
     * 往Json Generator构建器写入int类型的值
     *
     * @param value int
     * @return XContentBuilder
     * @throws IOException 字段写入异常
     */
    public XContentBuilder value(int value) throws IOException {
        generator.writeNumber(value);
        return this;
    }

    /**
     * Integer 类型转换为int类型的参数
     *
     * @param value Integer
     * @return XContentBuilder
     * @throws IOException 字段写入异常
     */
    public XContentBuilder value(Integer value) throws IOException {
        return (value == null) ? nullValue() : value(value.intValue());
    }

    /**
     * xContentBuilder.append(Field对应的Value(Float类型)）
     *
     * @param value Float类型的值
     * @throws IOException xContentBuilder.append(Field以及Field对应的Value(Float类型))发生的异常
     */
    public XContentBuilder value(Float value) throws IOException {
        return (value == null) ? nullValue() : value(value.floatValue());
    }

    /**
     * xContentBuilder.append(Field对应的Value(float类型)）
     *
     * @param value float类型的值
     * @throws IOException xContentBuilder.append(Field以及Field对应的Value(float类型))发生的异常
     */
    public XContentBuilder value(float value) throws IOException {
        generator.writeNumber(value);
        return this;
    }

    /**
     * xContentBuilder.append(Field对应的Value(Boolean类型))
     *
     * @param value Boolean类型的值
     * @return XContentBuilder
     * @throws IOException xContentBuilder.append(Field对应的Value(Boolean类型))发生的异常
     */
    public XContentBuilder value(Boolean value) throws IOException {
        return (value == null) ? nullValue() : value(value.booleanValue());
    }

    public XContentBuilder value(boolean value) throws IOException {
        generator.writeBoolean(value);
        return this;
    }


    /**
     * 选择器，使用开头注册的append(value类型)
     *
     * @param value Value(Object)
     * @throws IOException 选择器，使用开头注册的append(value类型)发生的异常
     */
    private void unknownValue(Object value) throws IOException {
        if (value == null) {
            nullValue();
            return;
        }
        Writer writer = WRITERS.get(value.getClass());
        if (writer != null) {
            writer.write(this, value);
        } else if (value instanceof Path) {
            //Path implements Iterable<Path> and causes endless recursion and a StackOverFlow if treated as an Iterable here
            value((Path) value);
        } else if (value instanceof Iterable) {
            value((Iterable<?>) value);
        } else if (value instanceof Calendar) {
            value((Calendar) value);
        } else if (value instanceof ToXContent) {
            value((ToXContent) value);
        } else {
            value(Objects.toString(value));
        }
    }


    public XContentBuilder nullValue() throws IOException {
        generator.writeNull();
        return this;
    }

    static void ensureNameNotNull(String name) {
        ensureNotNull(name, "Field name cannot be null");
    }

    /**
     * 复制当前语句结构
     *
     * @param parser XContentParser
     * @return XContentBuilder
     * @throws IOException copy异常
     */
    public XContentBuilder copyCurrentStructure(XContentParser parser) throws IOException {
        generator.copyCurrentStructure(parser);
        return this;
    }

    static void ensureNotNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 获取Builder构建器的输出流
     *
     * @return OutputStream
     */
    public OutputStream bytes() {
        close();
        return os;
    }

    /**
     * 将Builder输出流转换成字符串
     *
     * @return String返回字符串表示形式
     * @throws IOException 异常信息
     */
    public String string() throws IOException {
        return bytes().toString();
    }

    /**
     * 重写的类；关闭生成器资源
     */
    @Override
    public void close() {
        try {
            generator.close();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to close the XContentBuilder", e);
        }
    }

    /**
     * Builder类的append(String)方法
     */
    @FunctionalInterface
    private interface Writer {
        /**
         * 可以理解为StringBuilder.append(String);
         *
         * @param builder XContentBuilder
         * @param value   待写入的XContentBuilder的内容
         * @throws IOException 写入异常
         */
        void write(XContentBuilder builder, Object value) throws IOException;
    }
}
