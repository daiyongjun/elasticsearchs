package cn.gsdata.elasticsearchs.open.sql.core.expression.function;

import java.io.Serializable;
import java.util.function.Supplier;
/**
 * SerializableNoArgFunction，同时继承{@link Supplier}和{@link Serializable}，支持序列化的函数接口
 *
 * @param <T> {@link Supplier} 定义的function唯一个返回值类型
 * @author daiyongjun
 */
public interface SerializableNoArgFunction<T> extends Supplier<T>, Serializable {
}
