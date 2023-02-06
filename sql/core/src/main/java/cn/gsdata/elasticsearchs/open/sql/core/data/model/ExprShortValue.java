package cn.gsdata.elasticsearchs.open.sql.core.data.model;

import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;

/**
 * Expression Short Value
 *
 * @author daiyongjun
 */
public class ExprShortValue extends AbstractExprNumberValue {
    public ExprShortValue(Number value) {
        super(value);
    }

    @Override
    public int compare(ExprValue other) {
        return Short.compare(shortValue(), other.shortValue());
    }

    @Override
    public boolean equal(ExprValue other) {
        return shortValue().equals(other.shortValue());
    }

    @Override
    public Object value() {
        return shortValue();
    }

    @Override
    public ExprType type() {
        return ExprCoreType.SHORT;
    }
}
