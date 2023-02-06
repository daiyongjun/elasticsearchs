package cn.gsdata.elasticsearchs.open.sql.core.expression.function;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 定义SerializableFunction，同时继承{@link Function}和{@link Serializable}，支持序列化的函数接口
 *
 * @param <T> {@link Function} 定义的function唯一个参数类型
 * @param <R> {@link Function} 定义的function唯一个返回值类型
 * @author daiyongjun
 */
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {
}
