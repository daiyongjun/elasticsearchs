package cn.gsdata.elasticsearchs.open.sql.core.data.model;

import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;
import com.google.common.base.Objects;
import lombok.RequiredArgsConstructor;

/**
 * Expression Number value (Expression Byte Value、Expression Short Value、Expression Int Value、Expression Long Value、Expression Float Value、Expression Double Value)
 *
 * @author daiyongjun
 */
@RequiredArgsConstructor
public abstract class AbstractExprNumberValue extends AbstractExprValue {
    private final Number value;

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public Byte byteValue() {
        return value.byteValue();
    }

    @Override
    public Short shortValue() {
        return value.shortValue();
    }

    @Override
    public Integer integerValue() {
        return value.intValue();
    }

    @Override
    public Long longValue() {
        return value.longValue();
    }

    @Override
    public Float floatValue() {
        return value.floatValue();
    }

    @Override
    public Double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
