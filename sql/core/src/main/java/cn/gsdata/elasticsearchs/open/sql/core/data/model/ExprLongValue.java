package cn.gsdata.elasticsearchs.open.sql.core.data.model;

import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;

/**
 * Expression Long Value
 *
 * @author daiyongjun
 */
public class ExprLongValue extends AbstractExprNumberValue {
    public ExprLongValue(Number value) {
        super(value);
    }

    @Override
    public int compare(ExprValue other) {
        return Long.compare(longValue(), other.longValue());
    }

    @Override
    public boolean equal(ExprValue other) {
        return longValue().equals(other.longValue());
    }

    @Override
    public Object value() {
        return longValue();
    }

    @Override
    public ExprType type() {
        return ExprCoreType.LONG;
    }
}
