package cn.gsdata.elasticsearchs.clients.elasticsearch.client;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Elastic-Search-Client的启动类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/3/06 13:30
 */
@EnableApolloConfig
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.gsdata.elasticsearchs.*"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
