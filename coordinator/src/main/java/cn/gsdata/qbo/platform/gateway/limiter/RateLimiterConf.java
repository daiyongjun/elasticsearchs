package cn.gsdata.qbo.platform.gateway.limiter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基础的ratelimiter的配置类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/10/21 9:30
 */
@Component
@ConfigurationProperties(prefix = "ratelimiter-conf")
public class RateLimiterConf {
    /**
     * 缺省情况的处理速度
     */
    private static final String DEFAULT_REPLENISHRATE = "default.replenishRate";
    /**
     * 缺省情况的容量
     */
    private static final String DEFAULT_BURSTCAPACITY = "default.burstCapacity";

    private Map<String, Integer> rateLimitMap = new ConcurrentHashMap<String, Integer>() {
        {
            put(DEFAULT_REPLENISHRATE, 100);
            put(DEFAULT_BURSTCAPACITY, 1000);
        }
    };

    public Map<String, Integer> getRateLimitMap() {
        return rateLimitMap;
    }

    public void setRateLimitMap(Map<String, Integer> rateLimitMap) {
        this.rateLimitMap = rateLimitMap;
    }
}