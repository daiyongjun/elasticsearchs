package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentLocation;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.ElasticsearchException;

/**
 *  数据解析异常
 * @author daiyongjun
 */
public class ParsingException extends ElasticsearchException {

    protected static final int UNKNOWN_POSITION = -1;
    private final int lineNumber;
    private final int columnNumber;

    public ParsingException(XContentLocation contentLocation, String msg, Object... args) {
        this(contentLocation, msg, null, args);
    }

    public ParsingException(XContentLocation contentLocation, String msg, Throwable cause, Object... args) {
        super(msg, cause, args);
        int lineNumber = UNKNOWN_POSITION;
        int columnNumber = UNKNOWN_POSITION;
        if (contentLocation != null) {
            lineNumber = contentLocation.lineNumber;
            columnNumber = contentLocation.columnNumber;
        }
        this.columnNumber = columnNumber;
        this.lineNumber = lineNumber;
    }

    /**
     * Line number of the location of the error
     *
     * @return the line number or -1 if unknown
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * 错误位置的列号
     * */
    public int getColumnNumber() {
        return columnNumber;
    }
}