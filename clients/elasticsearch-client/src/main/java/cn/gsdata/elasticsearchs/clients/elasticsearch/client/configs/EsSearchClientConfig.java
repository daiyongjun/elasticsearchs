package cn.gsdata.elasticsearchs.clients.elasticsearch.client.configs;

import cn.gsdata.elasticsearchs.clients.elasticsearch.client.client.ContentRegistry;
import cn.gsdata.elasticsearchs.clients.elasticsearch.client.client.DynamicCustomRestClient;
import cn.gsdata.elasticsearchs.clients.elasticsearch.client.client.DynamicRestClient;
import cn.gsdata.elasticsearchs.commons.clients.common.container.ContainerHolder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.SearchModule;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.NamedXContentRegistry;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.function.Function;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory.Builder;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static java.util.stream.Collectors.toList;

/**
 * 配置加载类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/10/8 14:11
 */
@Configuration
@Slf4j
public class EsSearchClientConfig implements EnvironmentAware {
    /**
     * 存储配置文件中的索引信息
     */
    public static Map<String, Map<String, String>> indexes = new ConcurrentHashMap<>();
    private Environment environment;
    private final ScheduledExecutorService executor;
    private final ApplicationContext context;
    private Binder binder;

    @Autowired
    public EsSearchClientConfig(ApplicationContext context) {
        this.context = context;
        ThreadFactory threadFactory = (new Builder()).namingPattern("close-client-%d").build();
        this.executor = new ScheduledThreadPoolExecutor(1, threadFactory);
    }

    @Override
    @SuppressWarnings("all")
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        this.binder = Binder.get(environment);
    }

    @Bean(name = "client", destroyMethod = "destroy")
    public DynamicRestClient restClient() {
        DynamicRestClient dynamicRestClient = new DynamicRestClient();
        LinkedHashMap<Object, Object> restClients = new LinkedHashMap<>();
        List<String> customDataSourceNames = new ArrayList<>();
        this.initRestClient(restClients, customDataSourceNames);
        dynamicRestClient.setDefaultTargetContainer(restClients.get(customDataSourceNames.get(0)));
        dynamicRestClient.setTargetContainers(restClients);
        dynamicRestClient.afterPropertiesSet();
        return dynamicRestClient;
    }

    @Bean(name = "registry")
    public ContentRegistry registry() {
        SearchModule searchModule = new SearchModule();
        NamedXContentRegistry registry = new NamedXContentRegistry(Stream.of(
                searchModule.getNamedXContents().stream()
        ).flatMap(Function.identity()).collect(toList()));
        return new ContentRegistry(registry);
    }

    @ApolloConfigChangeListener(value = "elasticsearch-client", interestedKeyPrefixes = {"spring.custom.elasticsearch."})
    @SuppressWarnings("unused")
    public void onChange(ConfigChangeEvent changeEvent) {
        Set<String> changedKeys = changeEvent.changedKeys();
        //基于changedKeys更新配置
        this.context.publishEvent(new EnvironmentChangeEvent(changedKeys));
        //基于changedKeys，动态调整内存中配置
        handChanger(changedKeys);
    }

    /**
     * 用于手动触发重新构建client
     */
    public void handChanger(Set<String> changedKeys) {
        DynamicCustomRestClient dynamicCustomRestClient = this.context.getBean(DynamicCustomRestClient.class);
        Map<Object, Object> targetContainers = dynamicCustomRestClient.targetContainers;
        //changedKeys进行重新构建RestClient
        this.updateRestClient(targetContainers, changedKeys);

        dynamicRestClient.setDefaultTargetContainer(restClients.get(customDataSourceNames.get(0)));
        Map<Object, Object> containers = dynamicRestClient.setTargetContainers(restClients);
        dynamicRestClient.afterPropertiesSet();
        this.executor.execute(() -> {
            try {
                Thread.sleep(36000L);
            } catch (InterruptedException var3) {
                var3.printStackTrace();
            }
            dynamicRestClient.destroy(containers);
        });
    }


    /**
     * 基于Set<String> changedKeys动态更新Map<Object, Object>  当前的restClients
     *
     * @param restClients Map<Object, Object>  当前的restClients
     * @param changedKeys
     */
    private void updateRestClient(Map<Object, Object> restClients, Set<String> changedKeys) {

    }

    @SuppressWarnings("all")
    private void initRestClient(LinkedHashMap<Object, Object> restClients, List<String> customDataSourceNames) {
        String names = this.environment.getProperty("spring.custom.elasticsearch.names", "");
        String regex = ",";
        ContainerHolder.datasourceId.clear();
        if (!names.isEmpty()) {
            String[] var = names.split(regex);
            for (String name : var) {
                Map properties = this.binder.bind("spring.custom.elasticsearch." + name, Map.class).get();
                RestClient restClient = this.buildRestClient(name, properties);
                customDataSourceNames.add(name);
                ContainerHolder.datasourceId.add(name);
                restClients.put(name, restClient);
                log.info("Data source initialization 【" + name + "】 successfully ...");
            }
        }
    }

    /**
     * 构建客户端
     *
     * @param name       构建资源名称
     * @param properties 构建资源的属性信息
     * @return RestClient 返回客户端信息
     */
    private RestClient buildRestClient(String name, Map<String, Object> properties) {
        String username = properties.get("username").toString();
        String password = properties.get("password").toString();
        String hosts = properties.get("hosts").toString();
        //初始化索引信息
        Map<String, String> index = (Map<String, String>) properties.getOrDefault("index", new LinkedHashMap<>(2));
        String auth = index.getOrDefault("auth", "r");
        String format = index.getOrDefault("format", "yyyyMM");
        log.warn("初始化索引配置 : " + name + "\tindex.auth : " + auth + "\tindex.format : " + format);
        index.put("auth", auth);
        index.put("format", format);
        indexes.put(name, index);
        //httpclient 链接池基础配置
        Map<String, String> pool = (Map<String, String>) properties.getOrDefault("pool", new LinkedHashMap<>(6));
        int connectTimeout = Integer.parseInt(pool.getOrDefault("connect-timeout", "1000"));
        int socketTimeout = Integer.parseInt(pool.getOrDefault("socket-timeout", "30000"));
        int requestTimeout = Integer.parseInt(pool.getOrDefault("request-timeout", "30000"));
        int ioThreadCount = Integer.parseInt(pool.getOrDefault("io-thread-count", String.valueOf(Runtime.getRuntime().availableProcessors())));
        int maxConnPerRoute = Integer.parseInt(pool.getOrDefault("max-conn-per-route", "10"));
        int maxConnTotal = Integer.parseInt(pool.getOrDefault("max-conn-total", "20"));
        log.warn("初始化链接池的基本信息，初始化资源名称 : " + name + "\tpool.connect-timeout : " + connectTimeout + "\tpool.socket-timeout : " + socketTimeout + "\tpool.request-timeout : " + requestTimeout + "\tpool.io-thread-count : " + ioThreadCount + "\tpool.max-conn-per-route : " + maxConnPerRoute + "\tpool.max-conn-total : " + maxConnTotal);
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        return RestClient.builder(this.setHttpHosts(hosts))
                .setRequestConfigCallback(
                        requestConfigBuilder -> requestConfigBuilder
                                //指从连接池获取连接的timeout
                                .setConnectTimeout(connectTimeout)
                                //指客户端和服务器建立连接后，客户端从服务器读取数据的timeout
                                .setSocketTimeout(socketTimeout)
                                //客户端获取数据到获取结果超时时间，超过客户端拒绝请求
                                .setConnectionRequestTimeout(requestTimeout))
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    //设置选择器的处理线程，默认是按当前机器的cpu核数
                    httpClientBuilder.setDefaultIOReactorConfig(
                            IOReactorConfig.custom()
                                    .setIoThreadCount(ioThreadCount)
                                    .build());
                    httpClientBuilder.setMaxConnPerRoute(maxConnPerRoute);
                    httpClientBuilder.setMaxConnTotal(maxConnTotal);
                    return httpClientBuilder
                            .setDefaultCredentialsProvider(credentialsProvider);
                }).build();
    }

    private HttpHost[] setHttpHosts(String host) {
        HttpHost[] httpHosts = null;
        if (host != null) {
            String[] hosts = host.split(",");
            int size = hosts.length;
            httpHosts = new HttpHost[size];
            for (int i = 0; i < size; ++i) {
                String[] tmp = hosts[i].split(":");
                HttpHost newHttpHost = new HttpHost(tmp[0], Integer.parseInt(tmp[1]), "http");
                httpHosts[i] = newHttpHost;
            }
        }

        return httpHosts;
    }
}