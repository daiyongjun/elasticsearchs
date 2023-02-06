package cn.gsdata.qbo.platform.gateway.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间处理公共类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/5/28 10:52
 */
public class TimeUtil {
    public static String DATE_TIME_FORMAT = "yyyy-MM-ddHH";

    private static Pattern pattern = Pattern.compile("now-([0-9]+)([dhms]{1})");

    /**
     * 指定格式获取当前日期时间
     *
     * @return String
     */
    public static String getCurrentDateTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 将 now-nd,now-nh,now-nm,now-ns 转换成时间戳
     *
     * @param time now-nd,now-nh,now-nm,now-ns
     * @return long 时间戳
     */
    public static Long conversionStamp(String time) {
        // 现在创建 matcher 对象
        Matcher m = pattern.matcher(time);
        int num = 0;
        String type = "";
        if (m.find()) {
            type = m.group(2);
            num = Integer.parseInt(m.group(1));
        }
        switch (type) {
            case "d":
                return System.currentTimeMillis() - num * 3600 * 24 * 1000;
            case "h":
                return System.currentTimeMillis() - num * 3600 * 1000;
            case "m":
                return System.currentTimeMillis() - num * 60 * 1000;
            case "s":
                return System.currentTimeMillis() - num * 1000;
            default:
                return System.currentTimeMillis();
        }

    }
    /**
     * 时间特殊处理类-针对精确到秒的时间特殊处理
     *
     * @param param 时间入参|时间范围
     * @return String
     */
    public static String timeHandle(String param) {
        return param.replaceAll("[ ]+", "T");
    }
}
