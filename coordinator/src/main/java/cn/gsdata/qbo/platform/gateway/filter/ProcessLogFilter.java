package cn.gsdata.qbo.platform.gateway.filter;

import cn.gsdata.qbo.platform.gateway.utils.LoggerUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import pro.chenggang.plugin.springcloud.gateway.context.GatewayContext;
import pro.chenggang.plugin.springcloud.gateway.option.FilterOrderEnum;
import pro.chenggang.plugin.springcloud.gateway.util.GatewayUtils;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static cn.gsdata.qbo.platform.gateway.constant.GatewayConstant.*;

/**
 * 获取日志进行日志汇总
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/8/12 10:10
 */
@SuppressWarnings("unused")
@Configuration
@Slf4j
public class ProcessLogFilter implements GlobalFilter, Ordered {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI requestUrl = request.getURI();
        String scheme = requestUrl.getScheme();
        /*
         * not http or https scheme
         */
        if ((!HTTP_SCHEME.equalsIgnoreCase(scheme) && !HTTPS_SCHEME.equals(scheme))) {
            return chain.filter(exchange);
        }
        return chain.filter(exchange).then(Mono.fromRunnable(() -> processLog(exchange)));
    }

    @Override
    public int getOrder() {
        return FilterOrderEnum.REQUEST_LOG_FILTER.getOrder() - 2;
    }

    /**
     * log response exclude response body 输出日志格式 输入日志格式
     *
     * @param exchange 事件
     */
    private void processLog(ServerWebExchange exchange) {
        String tranceId = exchange.getAttribute(TRANCE_ID);
        //打印请求日志
        logRequest(tranceId, exchange);
        //打印响应日志
        logResponse(tranceId, exchange);
        //打印简版日志
        collectLog(tranceId, exchange);
    }

    /**
     * 打印的请求日志
     *
     * @param tranceId 请求id
     * @param exchange 请求信息
     */
    private void logRequest(String tranceId, ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();

        HttpHeaders headers = request.getHeaders();
        MediaType contentType = headers.getContentType();
        Long length = exchange.getAttribute(CONTENT_LENGTH);
        length = length == null ? -1L : length;
        log.debug(tranceId + " (Request)ContentType : {} , Content Length : {}", contentType, length);
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        //获取请求参数
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        //获取body的请内容
        String data = "{}";
        if (gatewayContext != null && gatewayContext.getReadResponseData() && length > 0 && null != contentType) {
            if (contentType.includes(MediaType.APPLICATION_JSON) || contentType.includes(MediaType.APPLICATION_JSON_UTF8)) {
                data = gatewayContext.getRequestBody();
            } else if (contentType.includes(MediaType.APPLICATION_FORM_URLENCODED)) {
                queryParams = gatewayContext.getFormData();
            }
        }
        log.info(tranceId + " (Request)ContentType : {} , Content Length : {} , (Request)Query Param : {} , (Request)Query Data : {}", contentType, length, queryParams, JSON.parse(data));
        exchange.getAttributes().put(SEARCH_COUNT, LoggerUtil.getSearchCount(queryParams, exchange.getAttribute(REQUEST_URL)));
    }

    /**
     * 打印的请求日志
     *
     * @param tranceId 请求id
     * @param exchange 请求信息
     */
    private void logResponse(String tranceId, ServerWebExchange exchange) {
        Long start = exchange.getAttribute(START_TIME);
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = response.getHeaders();
        MediaType contentType = headers.getContentType();
        long length = headers.getContentLength();
        log.debug(tranceId + " (custom)ContentType : {} , Content Length : {}", contentType, length);
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        Object body = "";
        if (gatewayContext != null && gatewayContext.getReadResponseData()) {
            body = gatewayContext.getResponseBody();
        }
        long executeTime = 0;
        if (start != null) {
            executeTime = System.currentTimeMillis() - start;
        }
        log.info(tranceId + " (custom)HttpStatus : {} , (custom)custom Body : {} , Cost : {}ms", response.getStatusCode(), body, executeTime);
    }

    /**
     * 请求的简版日志
     *
     * @param tranceId 请求id
     * @param exchange 请求信息
     */
    private void collectLog(String tranceId, ServerWebExchange exchange) {
        //trace
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = request.getHeaders();
        long length = headers.getContentLength();
        MediaType contentType = headers.getContentType();
        Long start = exchange.getAttribute(START_TIME);
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
       URI uri =  exchange.getAttribute(TODO_MESSAGE);
        String todoHost = "-";
        String todoPath = "-";
        if(uri != null){
            //获取基础的配置信息
            todoHost = uri.getHost();
            todoPath = uri.getPath();
        }
        long executeTime = 0;
        Date time;
        if (start != null) {
            executeTime = System.currentTimeMillis() - start;
            time = new Date(start);
        } else {
            time = new Date();
        }
        Integer size = 0;
        Integer took =0;
        if (gatewayContext != null && gatewayContext.getReadResponseData()) {
            try{
                Map<String,Object> result = LoggerUtil.parseRet(gatewayContext.getResponseBody(),new String[]{"size","took"});
                size = (Integer)result.get("size");
                took = (Integer)result.get("took");
                took = took == null ? 0 : took;
            }catch (Exception ignored) {
            }
        }
        //获取body的请内容
        String data = "{}";
        if (gatewayContext != null && gatewayContext.getReadResponseData() && length > 0 && null != contentType) {
            if (contentType.includes(MediaType.APPLICATION_JSON) || contentType.includes(MediaType.APPLICATION_JSON_UTF8)) {
                data = gatewayContext.getRequestBody();
            }
        }
        log.trace(sdf.format(time) + "\t" + tranceId + "\t" + GatewayUtils.getIpAddress(request) + "\t" + request.getPath() + "\t" + request.getURI() + "\t" + response.getStatusCode() + "\t" + executeTime + "\t" + exchange.getAttribute("application") + "\t" + size + "\t" + exchange.getAttribute(SEARCH_COUNT) + "\t" + JSON.parse(data) + "\t" + exchange.getAttribute(CONTENT_LENGTH)+"\t"+took+"\t"+todoHost+"\t"+todoPath);
    }
}
