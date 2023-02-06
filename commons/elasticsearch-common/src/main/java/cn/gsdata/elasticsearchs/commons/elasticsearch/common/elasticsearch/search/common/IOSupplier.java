package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common;

import java.io.IOException;


/**
 * 重写java自带的Supplier<T>接口
 * 内置T get() throws IOException方法
 *
 * @author daiyongjun
 */
@FunctionalInterface
public interface IOSupplier<T> {
    T get() throws IOException;

}

