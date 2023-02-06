package cn.gsdata.qbo.platform.gateway.limiter;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Min;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * redis限流核心方法
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/10/23 11:24
 */
public class SystemRedisRateLimiter extends AbstractRateLimiter<SystemRedisRateLimiter.Config> implements ApplicationContextAware {
    private static final String REPLENISH_RATE_KEY = "replenishRate";
    private static final String BURST_CAPACITY_KEY = "burstCapacity";
    private static final String CONFIGURATION_PROPERTY_NAME = "sys-redis-rate-limiter";
    public static final String REDIS_SCRIPT_NAME = "redisRequestRateLimiterScript";
    private static final String REMAINING_HEADER = "X-RateLimit-Remaining";
    private static final String REPLENISH_RATE_HEADER = "X-RateLimit-Replenish-Rate";
    private static final String BURST_CAPACITY_HEADER = "X-RateLimit-Burst-Capacity";
    /**
     * 缺省情况的处理速度
     */
    private static final String DEFAULT_REPLENISHRATE = "default.replenishRate";
    /**
     * 缺省情况的容量
     */
    private static final String DEFAULT_BURSTCAPACITY = "default.burstCapacity";

    private ReactiveRedisTemplate<String, String> redisTemplate;
    private RedisScript<List<Long>> script;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public SystemRedisRateLimiter(ReactiveRedisTemplate<String, String> redisTemplate,
                                  RedisScript<List<Long>> script, Validator validator) {
        super(Config.class, CONFIGURATION_PROPERTY_NAME, validator);
        this.redisTemplate = redisTemplate;
        this.script = script;
        initialized.compareAndSet(false, true);
    }

    public SystemRedisRateLimiter(int defaultReplenishRate, int defaultBurstCapacity) {
        super(Config.class, CONFIGURATION_PROPERTY_NAME, null);
        @SuppressWarnings("unused")
        Config defaultConfig = new Config()
                .setReplenishRate(defaultReplenishRate)
                .setBurstCapacity(defaultBurstCapacity);

    }

    @Override
    public Mono<RateLimiter.Response> isAllowed(String routeId, String id) {
        if (!this.initialized.get()) {
            throw new IllegalStateException("RedisRateLimiter is not initialized");
        }
        if (ObjectUtils.isEmpty(rateLimiterConf)) {
            throw new IllegalArgumentException("No Configuration found for route " + routeId);
        }
        Map<String, Integer> rateLimitMap = rateLimiterConf.getRateLimitMap();
        //缓存的key
        String replenishRateKey = routeId + "." + id + "." + REPLENISH_RATE_KEY;

        String defaultReplenishRateKey = routeId + "." + DEFAULT_REPLENISHRATE;

        //容量key
        String burstCapacityKey = routeId + "." + id + "." + BURST_CAPACITY_KEY;

        String defaultBurstCapacityKey = routeId + "." + DEFAULT_BURSTCAPACITY;

        //缓存的key
        int replenishRate, burstCapacity;
        //查看key是否预设
        if (!ObjectUtils.isEmpty(rateLimitMap.get(replenishRateKey)) || !ObjectUtils.isEmpty(rateLimitMap.get(burstCapacityKey))) {
            replenishRate = rateLimitMap.get(replenishRateKey);
            burstCapacity = rateLimitMap.get(burstCapacityKey);
        } else {
            id = routeId + "-default";
            replenishRate = ObjectUtils.isEmpty(rateLimitMap.get(defaultReplenishRateKey)) ? rateLimitMap.get(DEFAULT_REPLENISHRATE) : rateLimitMap.get(defaultReplenishRateKey);
            burstCapacity = ObjectUtils.isEmpty(rateLimitMap.get(defaultBurstCapacityKey)) ? rateLimitMap.get(DEFAULT_BURSTCAPACITY) : rateLimitMap.get(defaultBurstCapacityKey);
        }
        try {
            List<String> keys = getKeys(id);
            List<String> scriptArgs = Arrays.asList(replenishRate + "", burstCapacity + "",
                    Instant.now().getEpochSecond() + "", "1");
            Flux<List<Long>> flux = this.redisTemplate.execute(this.script, keys, scriptArgs);
            return flux.onErrorResume(throwable -> Flux.just(Arrays.asList(1L, -1L)))
                    .reduce(new ArrayList<Long>(), (longs, l) -> {
                        longs.addAll(l);
                        return longs;
                    }).map(results -> {
                        boolean allowed = results.get(0) == 1L;
                        Long tokensLeft = results.get(1);
                        return new Response(allowed, getHeaders(replenishRate, burstCapacity, tokensLeft));
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Mono.just(new RateLimiter.Response(true, getHeaders(replenishRate, burstCapacity, -1L)));
    }

    private RateLimiterConf rateLimiterConf;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.rateLimiterConf = applicationContext.getBean(RateLimiterConf.class);
    }

    private HashMap<String, String> getHeaders(Integer replenishRate, Integer burstCapacity, Long tokensLeft) {
        HashMap<String, String> headers = new HashMap<>(10);
        headers.put(REMAINING_HEADER, tokensLeft.toString());
        headers.put(REPLENISH_RATE_HEADER, String.valueOf(replenishRate));
        headers.put(BURST_CAPACITY_HEADER, String.valueOf(burstCapacity));
        return headers;
    }

    private static List<String> getKeys(String id) {
        // Make a unique key per user.
        String prefix = "request_sys_rate_limiter.{" + id;

        // You need two Redis keys for Token Bucket.
        String tokenKey = prefix + "}.tokens";
        String timestampKey = prefix + "}.timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }


    @Validated
    @Data
    public static class Config {
        @Min(1)
        private int replenishRate;
        @Min(1)
        private int burstCapacity = 1;

        Config setReplenishRate(int replenishRate) {
            this.replenishRate = replenishRate;
            return this;
        }

        Config setBurstCapacity(int burstCapacity) {
            this.burstCapacity = burstCapacity;
            return this;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "replenishRate=" + replenishRate +
                    ", burstCapacity=" + burstCapacity +
                    '}';
        }
    }
}