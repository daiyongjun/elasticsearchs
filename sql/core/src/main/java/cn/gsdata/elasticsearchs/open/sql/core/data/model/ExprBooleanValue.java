package cn.gsdata.elasticsearchs.open.sql.core.data.model;

import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;

/**
 * Expression Boolean Value
 *
 * @author daiyongjun
 */
public class ExprBooleanValue extends AbstractExprValue {
    private static final ExprBooleanValue TRUE = new ExprBooleanValue(true);
    private static final ExprBooleanValue FALSE = new ExprBooleanValue(false);
    private final Boolean value;

    private ExprBooleanValue(Boolean value) {
        this.value = value;
    }

    /**
     * of()方法是1.9版本后对集合添加元素的优化，这里对ExprBooleanValue 添加一个new ExprBooleanValue()实例
     *
     * @param value Boolean
     * @return ExprBooleanValue
     */
    public static ExprBooleanValue of(Boolean value) {
        return value ? TRUE : FALSE;
    }

    @Override
    public Boolean booleanValue() {
        return value;
    }

    @Override
    public int compare(ExprValue other) {
        return Boolean.compare(value, other.booleanValue());
    }

    @Override
    public boolean equal(ExprValue other) {
        return value.equals(other.booleanValue());
    }

    @Override
    public Object value() {
        return value;
    }

    @Override
    public ExprType type() {
        return ExprCoreType.BOOLEAN;
    }
}
