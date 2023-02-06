package cn.gsdata.qbo.platform.gateway.refresh;

import com.ctrip.framework.apollo.enums.PropertyChangeType;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 路由加载类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/3/6 11:47
 */
@Component
@Slf4j
public class GatewayRoutRefresh implements ApplicationContextAware, ApplicationEventPublisherAware {


    private static final String ID_PATTERN = "spring.cloud.gateway.routes[d+].id";

    private static final String DEFAULT_FILTER_PATTERN = "spring.cloud.gateway.default-filters[d+].name";

    private ApplicationContext applicationContext;

    private ApplicationEventPublisher publisher;

    private final GatewayProperties gatewayProperties;

    @Autowired
    public GatewayRoutRefresh(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Override
    public void setApplicationEventPublisher(@Nullable ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @SuppressWarnings("unused")
    @ApolloConfigChangeListener(value = "gateway-server", interestedKeyPrefixes = {"spring.cloud.gateway.","hystrix.command."})
    public void onChange(ConfigChangeEvent changeEvent) {
        refreshGatewayProperties(changeEvent);
    }

    /***
     * 刷新org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator中定义的routes
     *
     * @param changeEvent 监听事件
     * @author daiyongjun
     * @date 2019/5/21 2:13 PM
     */
    private void refreshGatewayProperties(ConfigChangeEvent changeEvent) {
        log.info("Refreshing GatewayProperties!");
        preDestroyGatewayProperties(changeEvent);
        this.applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
        refreshGatewayRouteDefinition();
        log.info("GatewayProperties refreshed!");
    }

    /***
     * 清空路由
     * GatewayProperties没有@PreDestroy和destroy方法
     * org.springframework.cloud.context.properties.ConfigurationPropertiesRebinder#rebind(java.lang.String)中destroyBean时不会销毁当前对象
     * 如果把spring.cloud.gateway.前缀的配置项全部删除（例如需要动态删除最后一个路由的场景），initializeBean时也无法创建新的bean，则return当前bean
     * 若仍保留有spring.cloud.gateway.routes[n]或spring.cloud.gateway.default-filters[n]等配置，initializeBean时会注入新的属性替换已有的bean
     * 这个方法提供了类似@PreDestroy的操作，根据配置文件的实际情况把org.springframework.cloud.gateway.config.GatewayProperties#routes
     * 和org.springframework.cloud.gateway.config.GatewayProperties#defaultFilters两个集合清空
     *
     * @param changeEvent 监听事件
     * @author daiyongjun
     * @date 2019/5/21 2:13 PM
     */
    private synchronized void preDestroyGatewayProperties(ConfigChangeEvent changeEvent) {
        log.info("Pre Destroy GatewayProperties!");
        final boolean needClearRoutes = this.checkNeedClear(changeEvent, ID_PATTERN, this.gatewayProperties.getRoutes().size());
        if (needClearRoutes) {
            this.gatewayProperties.setRoutes(new ArrayList<>());
        }
        final boolean needClearDefaultFilters = this.checkNeedClear(changeEvent, DEFAULT_FILTER_PATTERN, this.gatewayProperties.getDefaultFilters().size());
        if (needClearDefaultFilters) {
            this.gatewayProperties.setRoutes(new ArrayList<>());
        }
        log.info("Pre Destroy GatewayProperties finished!");
    }

    private void refreshGatewayRouteDefinition() {
        log.info("Refreshing Gateway RouteDefinition!");
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        log.info("Gateway RouteDefinition refreshed!");
    }

    /***
     * 根据changeEvent和定义的pattern匹配key，如果所有对应PropertyChangeType为DELETED则需要清空GatewayProperties里相关集合
     *
     * @param changeEvent 监听事件
     * @param pattern 路由匹配规则
     * @param existSize 待清理的路由地址
     * @return boolean
     * @author daiyongjun
     * @date 2019/5/23 2:18 PM
     */
    private boolean checkNeedClear(ConfigChangeEvent changeEvent, String pattern, int existSize) {

        return changeEvent.changedKeys().stream().filter(key -> key.matches(pattern))
                .filter(key -> {
                    ConfigChange change = changeEvent.getChange(key);
                    return PropertyChangeType.DELETED.equals(change.getChangeType());
                }).count() == existSize;
    }

}