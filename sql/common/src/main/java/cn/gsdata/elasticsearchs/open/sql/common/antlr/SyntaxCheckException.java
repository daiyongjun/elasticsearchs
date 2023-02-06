package cn.gsdata.elasticsearchs.open.sql.common.antlr;

/**
 * 语法检测异常
 *
 * @author daiyongjun
 */
public class SyntaxCheckException extends RuntimeException {
    public SyntaxCheckException(String message) {
        super(message);
    }
}
