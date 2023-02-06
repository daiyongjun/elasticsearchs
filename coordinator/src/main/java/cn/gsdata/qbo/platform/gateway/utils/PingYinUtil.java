package cn.gsdata.qbo.platform.gateway.utils;

import com.alibaba.fastjson.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音工具类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/10/22 10:11
 */
public class PingYinUtil {
    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     *
     * @param inputString 中文字串
     * @return 中文转化为拼音的拼音
     */
    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();
        StringBuilder output = new StringBuilder();

        try {
            for (char c : input) {
                if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    output.append(temp[0]);
                } else {
                    output.append(c);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    /**
     * 获取汉字串拼音首字母，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音首字母
     */
    @SuppressWarnings("unused")
    public static String getFirstSpell(String chinese) {
        StringBuilder pybf = new StringBuilder();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char c : arr) {
            if (c > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat);
                    if (temp != null) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(c);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

    /**
     * 获取汉字串拼音，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音
     */
    @SuppressWarnings("unused")
    public static String getFullSpell(String chinese) {
        StringBuilder pybf = new StringBuilder();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char c : arr) {
            if (c > 128) {
                try {
                    pybf.append(PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(c);
            }
        }
        return pybf.toString();
    }

    public static void main(String[] args) {

        System.out.println(getPingYin("本地数仓心跳检测"));
//        String js = "{\"廊坊广电中心融媒体系统\":5,\"清博指数项目\":20,\"API\":8,\"指数移动版\":15,\"中科院文献情报中心\":3,\"济南新媒体中心\":3,\"重庆大屏项目\":6,\"清博开放平台\":10,\"字节跳动\":6,\"京东清博管家\":8,\"开普云项目专用\":3,\"讯飞有声\":10,\"数据迁移服务\":2,\"博晓通项目\":5,\"红麦微信分组文章推送\":2,\"精硕微信项目\":6}";
//        JSONObject obj = JSONObject.parseObject(js);
//        for (String str : obj.keySet()) {
//            System.out.println("ratelimiter-conf.rateLimitMap.solr-interface."+getPingYin(str)+".replenishRate = "+obj.getString(str));
//            System.out.println("ratelimiter-conf.rateLimitMap.solr-interface."+getPingYin(str)+".burstCapacity = "+obj.getString(str));
//        }
    }
}
