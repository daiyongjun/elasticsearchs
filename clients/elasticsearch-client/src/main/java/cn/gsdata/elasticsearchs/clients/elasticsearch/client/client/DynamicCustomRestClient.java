package cn.gsdata.elasticsearchs.clients.elasticsearch.client.client;


import java.io.IOException;
import java.util.Map;
import javax.annotation.PreDestroy;

import cn.gsdata.elasticsearchs.commons.clients.common.container.AbstractRoutingContainer;
import cn.gsdata.elasticsearchs.commons.clients.common.container.ContainerHolder;
import org.elasticsearch.client.RestClient;

/**
 * 动态获取容器类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/10/8 14:11
 */
public class DynamicCustomRestClient extends AbstractRoutingContainer<CustomRestClient> {

    public String determineCurrentLookupKey() {
        return ContainerHolder.getDataSourceType();
    }

    @PreDestroy
    public void destroy() {
        Map<String, CustomRestClient> containers = this.targetContainers;
        assert containers != null;
        this.destroy(containers);
    }

    public void destroy(Map<String, CustomRestClient> containers) {
        containers.forEach((key, value) -> {
            RestClient restClient = value.getRestClient();
            if (restClient != null) {
                try {
                    restClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
