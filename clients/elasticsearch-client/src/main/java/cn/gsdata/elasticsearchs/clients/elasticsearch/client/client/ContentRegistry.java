package cn.gsdata.elasticsearchs.clients.elasticsearch.client.client;


import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.NamedXContentRegistry;
import lombok.Data;

import javax.annotation.PreDestroy;

/**
 * 基于elastic-search-common 的NamedXContentRegistry
 *
 * @author daiyongjun
 */
@Data
public class ContentRegistry {
    private final NamedXContentRegistry ContentRegistry;


    @PreDestroy
    public void destroy() {
        //内容销毁
    }
}
