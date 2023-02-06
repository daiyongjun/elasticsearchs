package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common;

import java.util.function.Function;

/**
 * 一个类似 {@link Function} 的接口，允许抛出已检查的异常。
 */
@FunctionalInterface
public interface CheckedFunction<T, R, E extends Exception> {
    R apply(T t) throws E;
}