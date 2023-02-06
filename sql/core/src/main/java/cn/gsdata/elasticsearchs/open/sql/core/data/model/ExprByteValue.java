package cn.gsdata.elasticsearchs.open.sql.core.data.model;

import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;


/**
 * Expression Byte Value
 *
 * @author daiyongjun
 */
public class ExprByteValue extends AbstractExprNumberValue {
    public ExprByteValue(Number value) {
        super(value);
    }

    @Override
    public int compare(ExprValue other) {
        return Byte.compare(byteValue(), other.byteValue());
    }

    @Override
    public boolean equal(ExprValue other) {
        return byteValue().equals(other.byteValue());
    }

    @Override
    public Object value() {
        return byteValue();
    }

    @Override
    public ExprType type() {
        return ExprCoreType.BYTE;
    }

    @Override
    public String toString() {
        return value().toString();
    }
}
