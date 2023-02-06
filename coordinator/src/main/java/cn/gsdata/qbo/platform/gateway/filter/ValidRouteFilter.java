package cn.gsdata.qbo.platform.gateway.filter;

import com.qingbo.bigdata.common.exception.CustomException;
import com.qingbo.bigdata.common.utils.WebUtil;
import cn.gsdata.qbo.platform.gateway.utils.LoggerUtil;
import cn.gsdata.qbo.platform.gateway.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import pro.chenggang.plugin.springcloud.gateway.option.FilterOrderEnum;
import reactor.core.publisher.Mono;

import static cn.gsdata.qbo.platform.gateway.constant.GatewayConstant.*;

/**
 * 全局过滤器AUTH验证
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/3/6 11:47
 */
@Configuration
@Slf4j
public class ValidRouteFilter implements GlobalFilter,Ordered {

    @Value("${qbo.gateway.custom.param.url-ignore:/auth/login}")
    String ignore;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) throws CustomException {
        ServerHttpRequest request = exchange.getRequest();
        String requestUrl = request.getPath().pathWithinApplication().value();
        ServerHttpRequest.Builder mutate = request.mutate();
        //在请求上下文设置tranceId
        String uuid = LoggerUtil.getUuid();
        exchange.getAttributes().put(TRANCE_ID, uuid);
        exchange.getAttributes().put(REQUEST_URL, requestUrl);
        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        // 不进行拦截的地址
        if (WebUtil.isStartWith(requestUrl, ignore)) {
            ServerHttpRequest build = mutate.build();
            return chain.filter(exchange.mutate().request(build).build());
        }
        String application = exchange.getAttribute(REQUEST_APPLICATION);
        if (StringUtils.isEmpty(application)) {
            RequestUtil.parseApplication(exchange);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        //最先执行，所有请求必先验证权限
        return FilterOrderEnum.REQUEST_LOG_FILTER.getOrder() + 3;
    }
}
