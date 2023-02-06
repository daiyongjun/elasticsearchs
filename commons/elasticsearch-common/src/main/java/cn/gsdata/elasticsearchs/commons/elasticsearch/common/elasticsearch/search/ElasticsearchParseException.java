package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search;

/**
 * 解析异常
 *
 * @author daiyongjun
 */
public class ElasticsearchParseException extends ElasticsearchException {
    public ElasticsearchParseException(String msg, Object... args) {
        super(msg, args);
    }
}
