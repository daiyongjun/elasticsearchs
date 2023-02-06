package cn.gsdata.elasticsearchs.open.sql.core.data.model;

import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;

import java.util.Objects;

/**
 * Expression Missing Value
 *
 * @author daiyongjun
 */
public class ExprMissingValue extends AbstractExprValue {
    private static final ExprMissingValue EXPR_MISSING_VALUE = new ExprMissingValue();

    /**
     * of()方法是1.9版本后对集合添加元素的优化，这里对ExprMissingValue 添加一个new ExprMissingValue()实例
     *
     * @return ExprMissingValue
     */
    public static ExprMissingValue of() {
        return EXPR_MISSING_VALUE;
    }


    private ExprMissingValue() {
    }

    @Override
    public int compare(ExprValue other) {
        throw new IllegalStateException("[BUG] Unreachable, Comparing with MISSING is "
                + "undefined");
    }

    /**
     * 当 this.isNull() || this.isMissing() 时 return other.isMissing();
     *
     * @param other ExprValue
     * @return boolean
     */
    @Override
    public boolean equal(ExprValue other) {
        return other.isMissing();
    }

    @Override
    public boolean isMissing() {
        return true;
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
    public int hashCode() {
        return Objects.hashCode("MISSING");
    }

    @Override
    public String toString() {
        return "MISSING";
    }
}
