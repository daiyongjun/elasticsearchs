package cn.gsdata.qbo.platform.gateway;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import pro.chenggang.plugin.springcloud.gateway.annotation.EnableGatewayPlugin;
/**
 * 网关启动模块
 *
 * @author daiyongjun
 * @version 1.0
 * @date 2019/1/2 15:39
 */
@EnableApolloConfig
@SpringBootApplication
@EnableDiscoveryClient
@EnableGatewayPlugin
@EnableHystrix
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}