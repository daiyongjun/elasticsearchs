package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常信息处理类
 *
 * @author daiyongjun
 */
public final class ExceptionsHelper {
    public static String stackTrace(Throwable e) {
        StringWriter stackTraceStringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stackTraceStringWriter);
        e.printStackTrace(printWriter);
        return stackTraceStringWriter.toString();
    }
}
