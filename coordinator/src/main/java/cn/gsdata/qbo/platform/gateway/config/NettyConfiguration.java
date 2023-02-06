package cn.gsdata.qbo.platform.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * 配置gateway中限制get请求过小的问题
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/6/27 9:45
 */
@Component
public class NettyConfiguration implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {
   @Value("${server.max-initial-line-length:65536}")
    private int maxInitialLingLength;

    @Override
    public void customize(NettyReactiveWebServerFactory container) {
        container.addServerCustomizers(
                httpServer -> httpServer.httpRequestDecoder(
                        httpRequestDecoderSpec -> httpRequestDecoderSpec.maxInitialLineLength(maxInitialLingLength)
                )
        );
    }
}
