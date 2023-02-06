package cn.gsdata.elasticsearchs.open.sql.core.data.model;

import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;
import lombok.RequiredArgsConstructor;

/**
 * Expression String Value
 *
 * @author daiyongjun
 */
@RequiredArgsConstructor
public class ExprStringValue extends AbstractExprValue {
    private final String value;

    @Override
    public String stringValue() {
        return value;
    }

    @Override
    public int compare(ExprValue other) {
        return value.compareTo(other.stringValue());
    }

    @Override
    public boolean equal(ExprValue other) {
        return value.equals(other.stringValue());
    }

    @Override
    public Object value() {
        return value;
    }

    @Override
    public ExprType type() {
        return ExprCoreType.STRING;
    }
}
