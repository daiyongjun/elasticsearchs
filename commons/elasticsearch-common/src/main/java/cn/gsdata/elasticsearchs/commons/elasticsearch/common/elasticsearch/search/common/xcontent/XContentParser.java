package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

/**
 * XContent内容Parser解析器,Generator生成器和Parser解析器相对应
 *
 * @author daiyongjun
 */
public interface XContentParser extends Closeable {
    /**
     * XContentParser自定义XContent的Token（内含当前Token是否是值）
     * JDK1.5引入了新的类型——枚举
     * 参考文档：https://blog.csdn.net/qq_39949109/article/details/80432477
     */
    enum Token {
        //定义枚举类型 START_OBJECT{......},END_OBJECT{......},START_ARRAY{.......},
        //END_ARRAY{.......},FIELD_NAME{.......},VALUE_STRING{.......},
        // VALUE_NUMBER{.......},VALUE_NUMBER{.......},VALUE_BOOLEAN{.......},
        // VALUE_EMBEDDED_OBJECT{.......},VALUE_NULL{.......}
        START_OBJECT {
            @Override
            public boolean isValue() {
                return false;
            }
        }, END_OBJECT {
            @Override
            public boolean isValue() {
                return false;
            }
        }, START_ARRAY {
            @Override
            public boolean isValue() {
                return false;
            }
        }, END_ARRAY {
            @Override
            public boolean isValue() {
                return false;
            }
        }, FIELD_NAME {
            @Override
            public boolean isValue() {
                return false;
            }
        }, VALUE_STRING {
            @Override
            public boolean isValue() {
                return true;
            }
        }, VALUE_NUMBER {
            @Override
            public boolean isValue() {
                return true;
            }
        }, VALUE_BOOLEAN {
            @Override
            public boolean isValue() {
                return true;
            }
        }, VALUE_EMBEDDED_OBJECT {
            @Override
            public boolean isValue() {
                return true;
            }
        }, VALUE_NULL {
            @Override
            public boolean isValue() {
                return false;
            }
        };

        public abstract boolean isValue();
    }

    /**
     * 定义枚举类型 INT、LONG、FLOAT、DOUBLE
     */
    enum NumberType {
        // INT、LONG、FLOAT、DOUBLE
        INT,
        LONG,
        FLOAT,
        DOUBLE
    }

    /**
     * XContent 定义的类型
     *
     * @return XContentType
     */
    XContentType contentType();

    /**
     * XContent 的当前Token（该Token为XContentParser自定义的Token）
     *
     * @return Token
     */
    Token currentToken();

    /**
     * XContent 的下一个Token（该Token为XContentParser自定义的Token）
     *
     * @return Token
     * @throws IOException 获取下一个Token异常
     */
    Token nextToken() throws IOException;

    /**
     * 获取当前Token在XContent的位置
     *
     * @return XContentLocation
     */
    XContentLocation getTokenLocation();

    /**
     * 获取XContent的名称
     *
     * @return String
     * @throws IOException 解析异常
     */
    String currentName() throws IOException;

    /**
     * 获取待解析的Parser字段的Map<String,Object>
     *
     * @return Map<String, Object>
     * @throws IOException 获取当前Map<String,Object>类型异常
     */
    Map<String, Object> map() throws IOException;

    /**
     * 获取待解析的Parser字段的Map<String,String>
     *
     * @return Map<String, String>
     * @throws IOException 获取当前Map<String,String>类型异常
     */
    Map<String, String> mapStrings() throws IOException;

    /**
     * 获取待解析的Parser字段的数字类型
     *
     * @return NumberType
     * @throws IOException 获取当前数字类型异常
     */
    NumberType numberType() throws IOException;

    /**
     * 获取待解析字段的数字值
     *
     * @return Number
     * @throws IOException 获取具体的数字值异常
     */
    Number numberValue() throws IOException;

    byte[] binaryValue() throws IOException;


    long longValue(boolean coerce) throws IOException;

    /**
     * 获取当前Token,所对应的值（int）
     *
     * @return int
     * @throws IOException 解析异常
     */
    int intValue() throws IOException;

    /**
     * 获取当前Token,所对应的值（long）
     *
     * @return long
     * @throws IOException 解析异常
     */
    long longValue() throws IOException;

    /**
     * 获取当前Token,所对应的值（float）
     *
     * @return float
     * @throws IOException 解析异常
     */
    float floatValue() throws IOException;

    /**
     * 获取当前Token,所对应的值（double）
     *
     * @return String
     * @throws IOException 解析异常
     */
    double doubleValue() throws IOException;

    /**
     * 获取当前Token,所对应的值（boolean）
     *
     * @return boolean
     * @throws IOException 解析异常
     */
    boolean booleanValue() throws IOException;

    /**
     * 获取当前Token,所对应的值（String）
     *
     * @return String
     * @throws IOException 解析异常
     */
    String textOrNull() throws IOException;

    /**
     * 获取当前Token,所对应的值（String）
     *
     * @return String
     * @throws IOException 解析异常
     */
    String text() throws IOException;

    /**
     * 获取当前Token,所对应的值（Object类型，可能是int,float,string,boolean,null等类型）
     *
     * @return Object
     * @throws IOException 解析异常
     */
    Object objectText() throws IOException;


    /**
     * 可用于确定调用 textCharacters() 是否是访问当前事件解析器指向的文本内容,默认实现仅返回 false
     *
     * @return boolean
     */
    boolean hasTextCharacters();

    /**
     * 获取text内容的字符数组
     *
     * @return char[]
     * @throws IOException 解析异常
     */
    char[] textCharacters() throws IOException;

    /**
     * 获取text内容的字符数组的长度
     *
     * @return int
     * @throws IOException 解析异常
     */
    int textLength() throws IOException;

    /**
     * 获取text内容的字符数组的起始值
     *
     * @return int
     * @throws IOException 解析异常
     */
    int textOffset() throws IOException;

    /**
     * 使用指定的超类Class和指定的名称，去程序执行之前预定义的的注册器中解析核心方法（fromXContent）将Xcontent转换成Pojo
     * parser.namedObject(QueryBuilder.class, queryName); -> xContentRegistry.parseNamedObject(categoryClass, name, this);
     * categoryClass.cast(entry.parser.parse(parser)); -> categoryClass.cast(QueryNameQueryBuilder::fromXContent(parser));
     *
     * @param categoryClass 解析器的类别类
     * @param name          待解析对象的名称
     * @return <T>    待解析的对象
     * @throws IOException 异常信息
     */
    <T> T namedObject(Class<T> categoryClass, String name, Object context) throws IOException;

    /**
     * 子节点跳过
     *
     * @throws IOException 跳过过程异常
     */
    void skipChildren() throws IOException;


}
