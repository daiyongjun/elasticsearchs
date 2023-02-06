package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.common.unit;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.ToXContent;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentParser;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.ElasticsearchException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;

import java.io.IOException;
import java.util.Locale;


/**
 * 参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.1/common-options.html#fuzziness
 * 模糊性：官方文档定义的是可以编辑距离如：1、替换 一个字符到另一个字符 _f_ox -> _b_ox  2、插入 一个新字符: sic -> sick
 * 删除 3、一个字符:: b_l_ack -> back 4、换位 调整字符: _st_ar -> _ts_ar
 * <p>
 * 模糊性就是把 bieber 转换为 beaver 需要以下几步，
 * 1、用 v 替换掉 b: bie_b_er -> bie_v_er
 * 2、用 a 替换掉 i: b_i_ever -> b_a_ever
 * 3、换位 a 和 e : b_ae_ver -> b_ea_ver
 * 这里的3步就是编辑距离
 *
 * @author daiyongjun
 */
public class Fuzziness implements ToXContent {
    public static final String X_FIELD_NAME = "fuzziness";

    public static final ParseField FIELD = new ParseField(X_FIELD_NAME);
    /**
     * AUTO状态下的编辑距离
     */
    private static final int DEFAULT_LOW_DISTANCE = 3;
    private static final int DEFAULT_HIGH_DISTANCE = 6;

    private final String fuzziness;
    /**
     * 最低编辑距离
     */
    private int lowDistance = DEFAULT_LOW_DISTANCE;
    /**
     * 最高编辑距离
     */
    private int highDistance = DEFAULT_HIGH_DISTANCE;

    /**
     * 内置的几种编辑距离
     */
    public static final Fuzziness ZERO = new Fuzziness(0);
    public static final Fuzziness ONE = new Fuzziness(1);
    public static final Fuzziness TWO = new Fuzziness(2);
    public static final Fuzziness AUTO = new Fuzziness("AUTO");


    private Fuzziness(int fuzziness) {
        if (fuzziness != 0 && fuzziness != 1 && fuzziness != 2) {
            throw new IllegalArgumentException("Valid edit distances are [0, 1, 2] but was [" + fuzziness + "]");
        }
        this.fuzziness = Integer.toString(fuzziness);
    }

    private Fuzziness(String fuzziness) {
        if (fuzziness == null || fuzziness.isEmpty()) {
            throw new IllegalArgumentException("fuzziness can't be null!");
        }
        this.fuzziness = fuzziness.toUpperCase(Locale.ROOT);
    }

    private Fuzziness(String fuzziness, int lowDistance, int highDistance) {
        this(fuzziness);
        if (lowDistance < 0 || highDistance < 0 || lowDistance > highDistance) {
            throw new IllegalArgumentException("fuzziness wrongly configured, must be: lowDistance > 0, highDistance" +
                    " > 0 and lowDistance <= highDistance ");
        }
        this.lowDistance = lowDistance;
        this.highDistance = highDistance;
    }

    public static Fuzziness parse(XContentParser parser) throws IOException {
        XContentParser.Token token = parser.currentToken();
        switch (token) {
            case VALUE_STRING:
            case VALUE_NUMBER:
                final String fuzziness = parser.text();
                if (AUTO.asString().equalsIgnoreCase(fuzziness)) {
                    return AUTO;
                } else if (fuzziness.toUpperCase(Locale.ROOT).startsWith(AUTO.asString() + ":")) {
                    return parseCustomAuto(fuzziness);
                }
                try {
                    final int minimumSimilarity = Integer.parseInt(fuzziness);
                    switch (minimumSimilarity) {
                        case 0:
                            return ZERO;
                        case 1:
                            return ONE;
                        case 2:
                            return TWO;
                        default:
                            return build(fuzziness);
                    }
                } catch (NumberFormatException ex) {
                    return build(fuzziness);
                }

            default:
                throw new IllegalArgumentException("Can't parse fuzziness on token: [" + token + "]");
        }
    }

    public static Fuzziness build(Object fuzziness) {
        if (fuzziness instanceof Fuzziness) {
            return (Fuzziness) fuzziness;
        }
        String string = fuzziness.toString();
        if (AUTO.asString().equalsIgnoreCase(string)) {
            return AUTO;
        } else if (string.toUpperCase(Locale.ROOT).startsWith(AUTO.asString() + ":")) {
            return parseCustomAuto(string);
        }
        return new Fuzziness(string);
    }


    public String asString() {
        if (isAutoWithCustomValues()) {
            return fuzziness.toString() + ":" + lowDistance + "," + highDistance;
        }
        return fuzziness.toString();
    }

    private boolean isAutoWithCustomValues() {
        return fuzziness.startsWith("AUTO") && (lowDistance != DEFAULT_LOW_DISTANCE ||
                highDistance != DEFAULT_HIGH_DISTANCE);
    }

    private static Fuzziness parseCustomAuto(final String string) {
        assert string.toUpperCase(Locale.ROOT).startsWith(AUTO.asString() + ":");
        String[] fuzzinessLimit = string.substring(AUTO.asString().length() + 1).split(",");
        if (fuzzinessLimit.length == 2) {
            try {
                int lowerLimit = Integer.parseInt(fuzzinessLimit[0]);
                int highLimit = Integer.parseInt(fuzzinessLimit[1]);
                return new Fuzziness("AUTO", lowerLimit, highLimit);
            } catch (NumberFormatException e) {
                throw new ElasticsearchException("failed to parse [{}] as a \"auto:int,int\"", e,
                        string);
            }
        } else {
            throw new ElasticsearchException("failed to find low and high distance values");
        }
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        builder.field(X_FIELD_NAME, fuzziness);
        return builder;
    }
}
