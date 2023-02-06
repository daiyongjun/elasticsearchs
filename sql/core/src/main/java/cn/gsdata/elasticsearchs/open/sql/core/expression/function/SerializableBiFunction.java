package cn.gsdata.elasticsearchs.open.sql.core.expression.function;

import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 定义SerializableFunction，同时继承{@link BiFunction}和{@link Serializable}，支持序列化的函数接口
 *
 * @param <T> {@link BiFunction} 定义的function两个参数类型之一
 * @param <U> {@link BiFunction} 定义的function两个参数类型之一
 * @param <R> {@link Function} 定义的function唯一个返回值类型
 * @author daiyongjun
 */
public interface SerializableBiFunction<T, U, R> extends BiFunction<T, U, R>, Serializable {
}
