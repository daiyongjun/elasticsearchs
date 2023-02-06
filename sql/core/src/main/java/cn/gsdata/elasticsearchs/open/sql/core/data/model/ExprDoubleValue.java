package cn.gsdata.elasticsearchs.open.sql.core.data.model;

import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;

/**
 * Expression Double Value
 *
 * @author daiyongjun
 */
public class ExprDoubleValue extends AbstractExprNumberValue {
    public ExprDoubleValue(Number value) {
        super(value);
    }

    @Override
    public int compare(ExprValue other) {
        return Double.compare(doubleValue(), other.doubleValue());
    }

    @Override
    public boolean equal(ExprValue other) {
        return doubleValue().equals(other.doubleValue());
    }

    @Override
    public Object value() {
        return doubleValue();
    }

    @Override
    public ExprType type() {
        return ExprCoreType.DOUBLE;
    }
}
