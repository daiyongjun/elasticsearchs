package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent;

import java.io.Closeable;
import java.io.IOException;

/**
 * XContent内容Generator生成器,Generator生成器和Parser解析器相对应
 *
 * @author daiyongjun
 */
public interface XContentGenerator extends Closeable {

    /**
     * XContent的类型格式
     *
     * @return XContentType
     */
    XContentType contentType();

    /**
     * 写XContent中Array部分开头的内容
     *
     * @throws IOException 写XContent中Array部分开头的内容异常
     */
    void writeStartArray() throws IOException;

    /**
     * 写XContent中Array部分结尾的内容
     *
     * @throws IOException 写XContent中Array部分结尾的内容异常
     */
    void writeEndArray() throws IOException;

    /**
     * 写XContent开头的内容
     *
     * @throws IOException 写XContent开头的内容异常
     */
    void writeStartObject() throws IOException;

    /**
     * 写XContent尾部的内容
     *
     * @throws IOException 写XContent尾部的内容异常
     */
    void writeEndObject() throws IOException;

    /**
     * 写XContent字段
     *
     * @param name 字段名称
     * @throws IOException 写XContent字段异常
     */
    void writeFieldName(String name) throws IOException;

    /**
     * 写XContent NULL值
     *
     * @throws IOException 写XContent NULL值异常
     */
    void writeNull() throws IOException;

    /**
     * 写XContent Field以及Field对应的Value为Null的值
     *
     * @param name Field的名称
     * @throws IOException 写XContent Field以及Field对应的Value为Null的值异常
     */
    void writeNullField(String name) throws IOException;

    /**
     * 写XContent Field以及Field对应的Value(String类型)
     * *
     *
     * @param name  Field
     * @param value Value
     * @throws IOException 写XContent Field以及Field对应的Value(String类型)发生的异常
     */
    void writeStringField(String name, String value) throws IOException;

    /**
     * 写XContent Field对应的Value(String类型)
     *
     * @param value Value
     * @throws IOException 写XContent Field以及Field对应的Value(String类型)发生的异常
     */
    void writeString(String value) throws IOException;

    /**
     * 写XContent Field对应的Value(String类型)
     *
     * @param text   char[]
     * @param offset 起始值
     * @param len    长度
     * @throws IOException 写入的字符串过程异常
     */
    void writeString(char[] text, int offset, int len) throws IOException;

    /**
     * 写XContent Field对应的Value(int类型)
     *
     * @param value int
     * @throws IOException 写入数据异常
     */
    void writeNumber(int value) throws IOException;

    /**
     * 写XContent float类型的数据
     *
     * @param value float
     * @throws IOException 写XContent float类型的数据异常
     */
    void writeNumber(float value) throws IOException;


    /**
     * 写XContent Double类型的数据
     *
     * @param value Double
     * @throws IOException 写XContent float类型的数据异常
     */
    void writeNumber(Double value) throws IOException;

    /**
     * 写XContent boolean类型的数据
     *
     * @param value boolean
     * @throws IOException 写XContent boolean类型的数据异常
     */
    void writeBoolean(boolean value) throws IOException;

    /**
     * 写XContent byte[]类型的数据
     *
     * @param value byte[]
     * @throws IOException 写XContent byte[] 类型的数据异常
     */
    void writeBinary(byte[] value) throws IOException;

    /**
     * 复制当前语句结构
     *
     * @param parser XContentParser
     * @throws IOException 复制结构异常
     */
    void copyCurrentStructure(XContentParser parser) throws IOException;
}
