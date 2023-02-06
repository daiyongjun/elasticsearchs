package cn.gsdata.elasticsearchs.open.sql.core.data.model;

import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;

/**
 * Expression Float Value
 *
 * @author daiyongjun
 */
public class ExprFloatValue extends AbstractExprNumberValue {
    public ExprFloatValue(Number value) {
        super(value);
    }

    @Override
    public int compare(ExprValue other) {
        return Float.compare(floatValue(), other.floatValue());
    }

    @Override
    public boolean equal(ExprValue other) {
        return floatValue().equals(other.floatValue());
    }

    @Override
    public Object value() {
        return floatValue();
    }

    @Override
    public ExprType type() {
        return ExprCoreType.FLOAT;
    }
}
