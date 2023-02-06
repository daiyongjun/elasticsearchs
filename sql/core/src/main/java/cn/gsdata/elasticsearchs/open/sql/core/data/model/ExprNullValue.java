package cn.gsdata.elasticsearchs.open.sql.core.data.model;

import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;

/**
 * Expression  Null Value
 *
 * @author daiyongjun
 */
public class ExprNullValue extends AbstractExprValue {
    private static final ExprNullValue EXPR_NULL_VALUE = new ExprNullValue();

    private ExprNullValue() {
    }

    /**
     * of()方法是1.9版本后对集合添加元素的优化，这里对ExprNullValue 添加一个new ExprNullValue()实例
     *
     * @return ExprNullValue
     */
    public static ExprNullValue of() {
        return EXPR_NULL_VALUE;
    }


    @Override
    public int compare(ExprValue other) {
        throw new IllegalStateException(
                "[BUG] Unreachable, Comparing with NULL is undefined");
    }


    /**
     * 当 this.isNull() || this.isMissing() 时 return other.isNull();
     *
     * @param other ExprValue
     * @return boolean
     */
    @Override
    public boolean equal(ExprValue other) {
        return other.isNull();
    }

    @Override
    public Object value() {
        return null;
    }

    @Override
    public ExprType type() {
        return ExprCoreType.UNDEFINED;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public String toString() {
        return "NULL";
    }
}
