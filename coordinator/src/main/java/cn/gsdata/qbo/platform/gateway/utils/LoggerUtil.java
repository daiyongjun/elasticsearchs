package cn.gsdata.qbo.platform.gateway.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 日志处理工具类
 *
 * @author daiyongjun
 * @version 1.0
 * @date 2018/12/10 11:18
 */
public class LoggerUtil {
    /**
     * 生成系统uuid作为唯一标识
     *
     * @return String
     */
    public static String getUuid() {
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }

    public static long getDataSize(Object ret) {
        long size = 0;
        try {
            ret = JSON.toJSON(ret);
            if (ret != null) {
                if (ret instanceof String) {
                    ret = JSON.parse((String) ret);
                }
                if (ret instanceof JSONObject) {
                    JSONObject retOb = (JSONObject) ret;
                    if (!retOb.isEmpty()) {
                        size = 1;
                    }
                    //获取关键的keys
                    String[] keys = new String[]{"hits", "response", "docs", "data"};
                    for (String key : keys) {
                        if (retOb.containsKey(key)) {
                            size = getDataSize(retOb.get(key));
                            break;
                        }
                    }
                } else if (ret instanceof JSONArray) {
                    JSONArray rets = (JSONArray) ret;
                    size = rets.size();
                }
            }
        } catch (Exception e) {
            size = 0;
        }
        return size;
    }


    /**
     * 计数统计
     *
     * @param params     请求的参数
     * @param requestUrl 请求的url
     * @return int
     */
    public static int getSearchCount(MultiValueMap<String, String> params, String requestUrl) {
        int count = 1;
        try {
            String filterUrl = "msearch";
            if (requestUrl.contains(filterUrl)) {
                String[] repos = params.get("repos").toString().split(",");
                count = repos.length;
            }
        } catch (NullPointerException ignored) {
        }
        return count;
    }

    /**
     * 针对输入字段集合对结果集合进行解析
     * @param ret 结果集合
     * @param fileds 待处理的字段集合
     * @return Map<String,Long>
     */
    public static Map<String,Object> parseRet(Object ret, String[] fileds) {
        Map<String,Object> result = new HashMap<>(10);
        Object size = 0;
        for(String filed :fileds) {
            if (!"size".equals(filed)) {
                result.put(filed, null);
            }else {
                result.put("size",size);
            }
        }
        try {
            ret = JSON.toJSON(ret);
            if (ret != null) {
                if (ret instanceof String) {
                    ret = JSON.parse((String) ret);
                }
                if (ret instanceof JSONObject) {
                    JSONObject retOb = (JSONObject) ret;
                    //遍历获取参数
                    for(String filed :fileds){
                        if(!"size".equals(filed)){
                            result.put(filed, retOb.getOrDefault(filed, null));
                        }else{
                            if (!retOb.isEmpty()) {
                                size = 1;
                            }
                            //获取关键的keys
                            String[] keys = new String[]{"hits", "response", "docs", "data"};
                            for (String key : keys) {
                                if (retOb.containsKey(key)) {
                                    size = parseRet(retOb.get(key),new String[]{"size"}).get("size");
                                    break;
                                }
                            }
                            result.put("size",size);
                        }
                    }
                } else if (ret instanceof JSONArray) {
                    JSONArray rets = (JSONArray) ret;
                    for(String filed :fileds) {
                        if ("size".equals(filed)) {
                            result.put("size",rets.size());
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return result;
    }
}
