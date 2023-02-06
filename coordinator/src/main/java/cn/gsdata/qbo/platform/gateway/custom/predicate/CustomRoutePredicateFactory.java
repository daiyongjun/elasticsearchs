package cn.gsdata.qbo.platform.gateway.custom.predicate;

import cn.gsdata.qbo.platform.gateway.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 自定义路由断路器
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2022/3/12 10:10
 */
@Component()
@Slf4j
public class CustomRoutePredicateFactory extends AbstractRoutePredicateFactory<CustomRoutePredicateFactory.Config> {
    /**
     * DateTime 1 key.
     */
    public static final String DATETIME1_KEY = "datetime1";

    /**
     * DateTime 2 key.
     */
    public static final String DATETIME2_KEY = "datetime2";

    public CustomRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(DATETIME1_KEY, DATETIME2_KEY);
    }


    @Override
    public Predicate<ServerWebExchange> apply(CustomRoutePredicateFactory.Config config) {
        return exchange -> {
            Map<String, String> params = exchange.getRequest().getQueryParams().toSingleValueMap();
            long param;
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
            try {
                param = df.parse(params.getOrDefault("newsPosthour", TimeUtil.getCurrentDateTime("yyyyMMddHH"))).getTime();
            } catch (ParseException e) {
                param = System.currentTimeMillis();
            }
            return match(config.getDatetime1(),config.getDatetime2(), param);
        };
    }


    /**
     * 逻辑判断模块
     *
     * @param time1     时间范围1
     * @param time2     时间范围2
     * @param time      待确认的时间 time
     * @return boolean
     */
    private boolean match(String time1,String time2, long time) {
        if (time2 == null) {
            // time1 >= time
            return TimeUtil.conversionStamp(time1) >= time;
        }
        // time1 >= time > time2
        return TimeUtil.conversionStamp(time1) >= time && time > TimeUtil.conversionStamp(time2);
    }

    /**
     * 设置自定义断言的参数，参数内容：1D 2M 1Y 这种格式
     */
    @Validated
    public static class Config {
        @NotNull
        private String datetime1;

        private String datetime2;

        public String getDatetime1() {
            return datetime1;
        }

        public Config setDatetime1(String datetime1) {
            this.datetime1 = datetime1;
            return this;
        }

        public String getDatetime2() {
            return datetime2;
        }

        public Config setDatetime2(String datetime2) {
            this.datetime2 = datetime2;
            return this;
        }
    }

}
