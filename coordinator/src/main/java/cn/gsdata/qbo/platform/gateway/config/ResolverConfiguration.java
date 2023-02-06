package cn.gsdata.qbo.platform.gateway.config;

import cn.gsdata.qbo.platform.gateway.filter.factory.CustomRequestRateLimiterGatewayFilterFactory;
import cn.gsdata.qbo.platform.gateway.limiter.SystemRedisRateLimiter;
import cn.gsdata.qbo.platform.gateway.utils.PingYinUtil;
import cn.gsdata.qbo.platform.gateway.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import reactor.core.publisher.Mono;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * 设置获取请求规则
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/10/18 14:57
 */
@Configuration
public class ResolverConfiguration {

    @Bean
    public CustomRequestRateLimiterGatewayFilterFactory rateCheckGatewayFilterFactory() {
        return new CustomRequestRateLimiterGatewayFilterFactory();
    }

    @Bean
    public KeyResolver keyResolver() {
        //
        return exchange -> Mono.just(PingYinUtil.getPingYin(RequestUtil.parseApplication(exchange)));
    }

    @Bean
    @Primary
    @SuppressWarnings("all")
    SystemRedisRateLimiter systemRedisRateLimiter(
            ReactiveRedisTemplate<String, String> redisTemplate,
            @Qualifier(SystemRedisRateLimiter.REDIS_SCRIPT_NAME) RedisScript<List<Long>> script, Validator validator) {
        return new SystemRedisRateLimiter(redisTemplate, script, validator);
    }
}
