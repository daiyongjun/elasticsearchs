package cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.ElasticsearchException;

/**
 * 自定义Sql异常信息
 *
 * @author daiyongjun
 */
public abstract class SqlException extends ElasticsearchException {
    public SqlException(String msg, Object... args) {
        super(msg, args);
    }
}
