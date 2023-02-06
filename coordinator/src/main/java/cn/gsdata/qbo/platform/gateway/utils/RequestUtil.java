package cn.gsdata.qbo.platform.gateway.utils;

import com.qingbo.bigdata.authcommon.constants.AuthCommonConstant;
import com.qingbo.bigdata.authcommon.utils.JwtTokenUtil;
import com.qingbo.bigdata.common.constant.BaseQboConstant;
import com.qingbo.bigdata.common.exception.CustomException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import static cn.gsdata.qbo.platform.gateway.constant.GatewayConstant.REQUEST_APPLICATION;

/**
 * 针对请求解析请求内容
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/10/21 15:58
 */
public class RequestUtil {
    public static String parseApplication(ServerWebExchange exchange) throws CustomException {
        ServerHttpRequest request = exchange.getRequest();
        String token = request.getHeaders().getFirst(AuthCommonConstant.TOKEN_HEADER);
        if (token == null || token.isEmpty()) {
            throw new CustomException(BaseQboConstant.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED.value());
        }
        String application;
        try {
            token = token.replace(AuthCommonConstant.TOKEN_PREFIX, "");
            application = JwtTokenUtil.getUsername(token);
            exchange.getAttributes().put(REQUEST_APPLICATION, application);
        } catch (Exception e) {
            throw new CustomException(BaseQboConstant.UNAUTHORIZED_MESSAGE, HttpStatus.UNAUTHORIZED.value());
        }
        return application;
    }
}
