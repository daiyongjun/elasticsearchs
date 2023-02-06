package cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common;

/**
 * 语法非法参数异常
 *
 * @author daiyongjun
 */
public class SqlIllegalArgumentException extends ServerSqlException {
    public SqlIllegalArgumentException(String message, Object... args) {
        super(message, args);
    }
}
