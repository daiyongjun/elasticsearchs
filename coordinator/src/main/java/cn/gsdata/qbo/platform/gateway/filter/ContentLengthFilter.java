package cn.gsdata.qbo.platform.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import pro.chenggang.plugin.springcloud.gateway.option.FilterOrderEnum;
import reactor.core.publisher.Mono;

import static cn.gsdata.qbo.platform.gateway.constant.GatewayConstant.*;

/**
 * 正文长度过滤器
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2021/7/15 10:10
 */
@Configuration
public class ContentLengthFilter implements GlobalFilter, Ordered {
    private final static Long MAX_CONTENT_LENGTH = 835504L;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        Long length = headers.getContentLength();
        exchange.getAttributes().put(CONTENT_LENGTH, length);
        // 拦截信息
        if (MAX_CONTENT_LENGTH > length) {
            return chain.filter(exchange);
        } else {
            return setForbiddenResponse(exchange);
        }
    }


    private Mono<Void> setForbiddenResponse(ServerWebExchange exchange) {
        //消息太大
        ServerHttpResponse response = exchange.getResponse();
        //设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, max-age=0");
        //设置body
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", HttpStatus.FORBIDDEN.value());
        response.setStatusCode(HttpStatus.FORBIDDEN);
        jsonObject.put("msg", "亲！消息体太大了");
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(jsonObject.toJSONString().getBytes());
        return response.writeWith(Mono.just(bodyDataBuffer));
    }

    @Override
    public int getOrder() {
        //其次执行，验证大小
        return FilterOrderEnum.REQUEST_LOG_FILTER.getOrder() +2;
    }
}
