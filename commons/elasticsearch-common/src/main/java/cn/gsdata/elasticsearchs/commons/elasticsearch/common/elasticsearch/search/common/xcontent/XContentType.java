package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.json.JsonXContent;

/**
 * @author daiyongjun
 */
public enum XContentType {
    /**
     * 枚举类型JSON（JSON_X_CONTENT）
     */
    JSON() {
        @Override
        public XContent xContent() {
            return JsonXContent.JSON_X_CONTENT;
        }

        @Override
        public String mediaType() {
            return "application/json; charset=UTF-8";
        }
    };


    public abstract XContent xContent();

    public abstract String mediaType();

}
