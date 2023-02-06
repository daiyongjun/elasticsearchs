package cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common;

/**
 * 服务SQl异常
 *
 * @author daiyongjun
 */
public class ServerSqlException extends SqlException {
    protected ServerSqlException(String message, Object... args) {
        super(message, args);
    }
}

