package cn.gsdata.elasticsearchs.open.sql.core.data.model;

import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;

/**
 * Expression Integer Value
 *
 * @author daiyongjun
 */
public class ExprIntegerValue extends AbstractExprNumberValue {
    public ExprIntegerValue(Number value) {
        super(value);
    }

    @Override
    public int compare(ExprValue other) {
        return Integer.compare(integerValue(), other.integerValue());
    }

    @Override
    public boolean equal(ExprValue other) {
        return integerValue().equals(other.integerValue());
    }

    @Override
    public Object value() {
        return integerValue();
    }

    @Override
    public ExprType type() {
        return ExprCoreType.INTEGER;
    }
}
