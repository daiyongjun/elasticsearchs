package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.index.query;


import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.Strings;

import java.io.IOException;

/**
 * 基于term的query基类
 *
 * @author daiyongjun
 */
public abstract class BaseTermQueryBuilder<QB extends BaseTermQueryBuilder<QB>> extends AbstractQueryBuilder<QB> {
    /**
     * 字段名称
     */
    protected final String fieldName;

    /**
     * 字段所对应的值内容
     */
    protected final Object value;

    /**
     * 基于ParseField 定义字段对应值的所对应的名称
     */
    public static final ParseField VALUE_FIELD = new ParseField("value");

    /**
     * 构造方法 构建一个term查询基类
     *
     * @param fieldName 字段名称
     * @param value     字段值
     */
    public BaseTermQueryBuilder(String fieldName, Object value) {
        if (Strings.isEmpty(fieldName)) {
            throw new IllegalArgumentException("field name is null or empty");
        }
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        this.fieldName = fieldName;
        this.value = value;
    }

    /**
     * 返回此查询中使用的字段名称。
     *
     * @return String 字段名称
     */
    public String fieldName() {
        return this.fieldName;
    }

    /**
     * 返回此查询中使用的值。
     *
     * @return Object 字段名称
     */
    public Object value() {
        return this.value;
    }


    /**
     * Builder中写入BaseTermQuery(pojo)的数据，Builder.append(参数) 将pojo->Xcontent
     *
     * @param builder XContentBuilder
     * @throws IOException Builder中append(参数)异常
     */
    @Override
    protected void doXContent(XContentBuilder builder) throws IOException {
        builder.startObject(getName());
        builder.startObject(fieldName);
        builder.field(VALUE_FIELD.getPreferredName(), this.value);
        printBoostAndQueryName(builder);
        builder.endObject();
        builder.endObject();
    }
}
