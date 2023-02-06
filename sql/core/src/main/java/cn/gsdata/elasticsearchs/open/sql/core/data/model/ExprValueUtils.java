package cn.gsdata.elasticsearchs.open.sql.core.data.model;

import cn.gsdata.elasticsearchs.open.sql.core.exception.ExpressionEvaluationException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Expression Value工具类
 *
 * @author daiyongjun
 */
public class ExprValueUtils {
    public static final ExprValue LITERAL_TRUE = ExprBooleanValue.of(true);
    public static final ExprValue LITERAL_FALSE = ExprBooleanValue.of(false);

    public static final ExprValue LITERAL_NULL = ExprNullValue.of();
    public static final ExprValue LITERAL_MISSING = ExprMissingValue.of();

    public static ExprValue fromObjectValue(Object o) {
        if (null == o) {
            return LITERAL_NULL;
        }
        if (o instanceof Map) {
            return tupleValue((Map) o);
        } else if (o instanceof List) {
            return collectionValue(((List) o));
        } else if (o instanceof Byte) {
            return byteValue((Byte) o);
        } else if (o instanceof Short) {
            return shortValue((Short) o);
        } else if (o instanceof Integer) {
            return integerValue((Integer) o);
        } else if (o instanceof Long) {
            return longValue(((Long) o));
        } else if (o instanceof Float) {
            return floatValue((Float) o);
        } else if (o instanceof Double) {
            return doubleValue((Double) o);
        } else if (o instanceof String) {
            return stringValue((String) o);
        } else if (o instanceof Boolean) {
            return booleanValue((Boolean) o);
        } else {
            throw new ExpressionEvaluationException("unsupported object " + o.getClass());
        }
    }

    /**
     * Expression Tuple Value对象创建方法，{@link ExprTupleValue} constructor。
     *
     * @param map Map<String, Object>
     * @return ExprValue
     */
    public static ExprValue tupleValue(Map<String, Object> map) {
        LinkedHashMap<String, ExprValue> valueMap = new LinkedHashMap<>();
        map.forEach((k, v) -> valueMap.put(k, fromObjectValue(v)));
        return new ExprTupleValue(valueMap);
    }

    /**
     * Expression Collection Value对象创建方法
     *
     * @param list List<Object>
     * @return ExprValue
     */
    public static ExprValue collectionValue(List<Object> list) {
        List<ExprValue> valueList = new ArrayList<>();
        list.forEach(o -> valueList.add(fromObjectValue(o)));
        return new ExprCollectionValue(valueList);
    }

    /**
     * Expression Byte Value对象创建方法
     *
     * @param value Byte
     * @return ExprValue
     */
    public static ExprValue byteValue(Byte value) {
        return new ExprByteValue(value);
    }

    /**
     * Expression Short Value对象创建方法
     *
     * @param value Short
     * @return ExprValue
     */
    public static ExprValue shortValue(Short value) {
        return new ExprShortValue(value);
    }

    /**
     * Expression Integer Value对象创建方法
     *
     * @param value Integer
     * @return ExprValue
     */
    public static ExprValue integerValue(Integer value) {
        return new ExprIntegerValue(value);
    }

    /**
     * Expression Long Value对象创建方法
     *
     * @param value Long
     * @return ExprValue
     */
    public static ExprValue longValue(Long value) {
        return new ExprLongValue(value);
    }

    /**
     * Expression Float Value对象创建方法
     *
     * @param value Float
     * @return ExprValue
     */
    public static ExprValue floatValue(Float value) {
        return new ExprFloatValue(value);
    }

    /**
     * Expression Double Value对象创建方法
     *
     * @param value Double
     * @return ExprValue
     */
    public static ExprValue doubleValue(Double value) {
        return new ExprDoubleValue(value);
    }

    /**
     * Expression String Value对象创建方法
     *
     * @param value String
     * @return ExprValue
     */
    public static ExprValue stringValue(String value) {
        return new ExprStringValue(value);
    }

    /**
     * Expression Boolean Value对象创建方法
     *
     * @param value Boolean
     * @return ExprValue
     */
    public static ExprValue booleanValue(Boolean value) {
        return value ? LITERAL_TRUE : LITERAL_FALSE;
    }

    /**
     * Expression MissingValue 对象创建方法
     *
     * @return ExprValue
     */
    public static ExprValue missingValue() {
        return ExprMissingValue.of();
    }

    /**
     * Expression Null Value 对象创建方法
     *
     * @return ExprValue
     */
    public static ExprValue nullValue() {
        return ExprNullValue.of();
    }


}
