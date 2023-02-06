package cn.gsdata.qbo.platform.gateway.refresh;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 限制规则刷新
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/10/22 16:37
 */
@Component
@Slf4j
public class LimiterRuleRefresh implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unused")
    @ApolloConfigChangeListener(value = "gateway-server", interestedKeyPrefixes = {"ratelimiter-conf.rateLimitMap."})
    private void onChange(ConfigChangeEvent changeEvent) {
        refreshLimiterRules(changeEvent.changedKeys());
    }


    private void refreshLimiterRules(Set<String> changedKeys) {
        log.info("Refreshing limiter levels");
        this.applicationContext.publishEvent(new EnvironmentChangeEvent(changedKeys));
        log.info("limiter levels refreshed");
    }
}
