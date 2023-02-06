package cn.gsdata.qbo.platform.gateway.handle;

import com.qingbo.bigdata.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 网关异常处理类提供网关的error.log
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/3/8 16:56
 */
@Slf4j
@SuppressWarnings("unused")
public class ExceptionHandler extends DefaultErrorWebExceptionHandler {

    ExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
                     ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    /**
     * 获取异常属性
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Throwable ex = super.getError(request);
        //自定义业务异常处理
        log.error(ex.getMessage() + "\t" + getTrace(ex));
        if (ex instanceof CustomException) {
            return new HashMap<String, Object>(1) {{
                this.put("msg", ex.getMessage() + ";异常详细信息请查看日志");
                this.put("code", ((CustomException) ex).getStatusCode());
            }};
        }
        return new HashMap<String, Object>(1) {{
            this.put("msg", "系统异常;异常详细信息请查看日志");
            this.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }};
    }

    private static String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    /**
     * 指定响应处理方法为JSON处理的方法
     *
     * @param errorAttributes 错误信息参数
     */
    @Override
    @SuppressWarnings("NullableProblems")
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * 根据code获取对应的HttpStatus
     *
     * @param errorAttributes 错误信息参数
     */
    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        int statusCode = (int) errorAttributes.get("code");
        return statusCode;
    }
}
