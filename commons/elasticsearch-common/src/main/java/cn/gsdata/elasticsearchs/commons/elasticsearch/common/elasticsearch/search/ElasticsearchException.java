package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.logging.LoggerMessageFormat;

/**
 *  构建自定义异常类
 *
 * @author daiyongjun
 */
public class ElasticsearchException  extends RuntimeException{

    /**
     * 自定义异常：ElasticsearchException 构造方法
     * @param msg   异常信息
     * @param args  异常信息待处理参数
     */
    public ElasticsearchException(String msg, Object... args) {
        super(LoggerMessageFormat.format(msg, args));
    }

    /**
     *  自定义异常：ElasticsearchException 构造方法
     * @param msg   异常信息
     * @param cause 接受异常类
     * @param args  异常信息待处理参数
     */
    public ElasticsearchException(String msg, Throwable cause, Object... args) {
        super(LoggerMessageFormat.format(msg, args), cause);
    }

}
