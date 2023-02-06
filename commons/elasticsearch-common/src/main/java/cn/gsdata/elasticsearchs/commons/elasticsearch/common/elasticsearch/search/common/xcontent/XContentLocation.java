package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent;

/**
 * 表示某些 XContent 中某个位置的行号和列号的简单数据结构，例如JSON。
 * 位置通常用于将解析错误的位置传达给最终用户，因此具有从 1 开始的行号和列号。
 *
 * @author daiyongjun
 */
public class XContentLocation {
    public final int lineNumber;
    public final int columnNumber;

    public XContentLocation(int lineNumber, int columnNumber) {
        super();
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    @Override
    public String toString() {
        return lineNumber + ":" + columnNumber;
    }
}
