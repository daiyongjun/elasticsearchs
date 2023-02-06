package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent;

import java.io.IOException;

/**
 * 使用指定XContent解析器，将XContentParser转换为指定类型<Context> Pojo类
 * 备注：@FunctionalInterface 注解表示为只有一个实现方法的接口，可以使用Lambda表达式
 *
 * @author daiyongjun
 */
@FunctionalInterface
public interface ContextParser<Context, Value> {
    /**
     * 使用指定XContent解析器，将XContentParser转换为指定类型<Context, Value> Pojo类
     *
     * @param parser XContent解析器
     * @param context 设置的预留字段
     * @return Value
     * @throws IOException 使用指定XContent解析器，将XContentParser转换为指定类型<Value> Pojo类异常
     */
    Value parse(XContentParser parser, Context context) throws IOException;
}
