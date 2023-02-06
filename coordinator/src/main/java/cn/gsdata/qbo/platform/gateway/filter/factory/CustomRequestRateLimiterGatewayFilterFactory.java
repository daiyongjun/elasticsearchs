package cn.gsdata.qbo.platform.gateway.filter.factory;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.HttpStatusHolder;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.setResponseStatus;

/**
 * 自定义限流的filter工厂方法
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/10/23 11:24
 */
@Component
@ConfigurationProperties("spring.cloud.gateway.filter.request-rate-limiter")
public class CustomRequestRateLimiterGatewayFilterFactory extends
        AbstractGatewayFilterFactory<CustomRequestRateLimiterGatewayFilterFactory.Config> {
    /**
     * Key-Resolver key.
     */
    private static final String EMPTY_KEY = "____EMPTY_KEY__";

    /**
     * Switch to deny requests if the Key Resolver returns an empty key, defaults to true.
     */
    private boolean denyEmptyKey = true;

    /**
     * HttpStatus to return when denyEmptyKey is true, defaults to FORBIDDEN.
     */
    private String emptyKeyStatusCode = HttpStatus.FORBIDDEN.name();

    public CustomRequestRateLimiterGatewayFilterFactory() {
        super(Config.class);
    }

    public boolean isDenyEmptyKey() {
        return denyEmptyKey;
    }

    public void setDenyEmptyKey(boolean denyEmptyKey) {
        this.denyEmptyKey = denyEmptyKey;
    }

    public String getEmptyKeyStatusCode() {
        return emptyKeyStatusCode;
    }

    public void setEmptyKeyStatusCode(String emptyKeyStatusCode) {
        this.emptyKeyStatusCode = emptyKeyStatusCode;
    }

    @SuppressWarnings("unchecked")
    @Override
    public GatewayFilter apply(Config config) {
        KeyResolver resolver = config.keyResolver;
        RateLimiter<Object> limiter = config.rateLimiter;
        boolean denyEmpty = getOrDefault(config.denyEmptyKey, this.denyEmptyKey);
        HttpStatusHolder emptyKeyStatus = HttpStatusHolder
                .parse(getOrDefault(config.emptyKeyStatus, this.emptyKeyStatusCode));

        return (exchange, chain) -> {
            Route route = exchange
                    .getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            return resolver.resolve(exchange).defaultIfEmpty(EMPTY_KEY).flatMap(key -> {
                if (EMPTY_KEY.equals(key)) {
                    if (denyEmpty) {
                        setResponseStatus(exchange, emptyKeyStatus);
                        return exchange.getResponse().setComplete();
                    }
                    return chain.filter(exchange);
                }
                assert route != null;
                return limiter.isAllowed(route.getId(), key).flatMap(response -> {

                    for (Map.Entry<String, String> header : response.getHeaders()
                            .entrySet()) {
                        exchange.getResponse().getHeaders().add(header.getKey(),
                                header.getValue());
                    }

                    if (response.isAllowed()) {
                        return chain.filter(exchange);
                    }
                    //超过了限流的response返回值
                    return setRateCheckResponse(exchange);
                });
            });
        };
    }

    private Mono<Void> setRateCheckResponse(ServerWebExchange exchange) {
        //超过了限流
        ServerHttpResponse response = exchange.getResponse();
        //设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, max-age=0");
        //设置body
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", HttpStatus.NOT_IMPLEMENTED.value());
        response.setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        jsonObject.put("msg", "亲！服务器太忙请稍后再访问");
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(jsonObject.toJSONString().getBytes());
        return response.writeWith(Mono.just(bodyDataBuffer));
    }

    private <T> T getOrDefault(T configValue, T defaultValue) {
        return (configValue != null) ? configValue : defaultValue;
    }

    public static class Config {

        private KeyResolver keyResolver;

        private RateLimiter rateLimiter;

        private HttpStatus statusCode = HttpStatus.TOO_MANY_REQUESTS;

        private Boolean denyEmptyKey;

        private String emptyKeyStatus;

        public KeyResolver getKeyResolver() {
            return keyResolver;
        }

        public Config setKeyResolver(KeyResolver keyResolver) {
            this.keyResolver = keyResolver;
            return this;
        }

        public RateLimiter getRateLimiter() {
            return rateLimiter;
        }

        public Config setRateLimiter(RateLimiter rateLimiter) {
            this.rateLimiter = rateLimiter;
            return this;
        }

        public HttpStatus getStatusCode() {
            return statusCode;
        }

        public Config setStatusCode(HttpStatus statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Boolean getDenyEmptyKey() {
            return denyEmptyKey;
        }

        public Config setDenyEmptyKey(Boolean denyEmptyKey) {
            this.denyEmptyKey = denyEmptyKey;
            return this;
        }

        public String getEmptyKeyStatus() {
            return emptyKeyStatus;
        }

        public Config setEmptyKeyStatus(String emptyKeyStatus) {
            this.emptyKeyStatus = emptyKeyStatus;
            return this;
        }
    }
}
