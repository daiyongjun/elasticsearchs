package cn.gsdata.elasticsearchs.open.sql.core.data.model;

import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;
import cn.gsdata.elasticsearchs.open.sql.core.exception.ExpressionEvaluationException;
import cn.gsdata.elasticsearchs.open.sql.core.storage.bindingtuple.BindingTuple;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Map;

/**
 * 定义Expression的Value的接口,用于定义ExprValue的类型接口，具体实现有String,Inter等等
 *
 * @author daiyongjun
 */
public interface ExprValue extends Serializable, Comparable<ExprValue> {
    /**
     * 获取Expression Value值
     *
     * @return Object   Value具体值
     */
    Object value();

    /**
     * 获取Expression Value值对应的类型
     *
     * @return ExprType Value值对应的类型
     */
    ExprType type();

    /**
     * 当前value为空值
     *
     * @return boolean true: 为空值 false: 为非空
     */
    default boolean isNull() {
        return false;
    }

    /**
     * 当前Expression Value是缺失
     *
     * @return boolean  true: 缺失 false: 非缺失
     */
    default boolean isMissing() {
        return false;
    }

    /**
     * 当前Expression Value是数字值
     *
     * @return boolean true: 是数字 false : 非数字
     */
    default boolean isNumber() {
        return false;
    }

    /**
     * ExprValue与之相关的绑定信息
     *
     * @return BindingTuple
     */
    default BindingTuple bindingTuples() {
        return BindingTuple.EMPTY;
    }

    /**
     * 获取Expression Value 类型为Byte 异常，详细基于具体实现类
     *
     * @return Byte
     */
    default Byte byteValue() {
        throw new ExpressionEvaluationException(
                "invalid to get byteValue from value of type " + type());
    }

    /**
     * 获取Expression Value 类型为Short异常，详细基于具体实现类
     *
     * @return Short
     */
    default Short shortValue() {
        throw new ExpressionEvaluationException(
                "invalid to get shortValue from value of type " + type());
    }

    /**
     * 获取Expression Value 类型为Integer异常，详细基于具体实现类
     *
     * @return Integer
     */
    default Integer integerValue() {
        throw new ExpressionEvaluationException(
                "invalid to get integerValue from value of type " + type());
    }

    /**
     * 获取Expression Value 类型为Long异常，详细基于具体实现类
     *
     * @return Long
     */
    default Long longValue() {
        throw new ExpressionEvaluationException(
                "invalid to get longValue from value of type " + type());
    }

    /**
     * 获取Expression Value 类型为Float异常，详细基于具体实现类
     *
     * @return Float
     */
    default Float floatValue() {
        throw new ExpressionEvaluationException(
                "invalid to get floatValue from value of type " + type());
    }

    /**
     * 获取Expression Value 类型为Double异常，详细基于具体实现类
     *
     * @return Double
     */
    default Double doubleValue() {
        throw new ExpressionEvaluationException(
                "invalid to get doubleValue from value of type " + type());
    }

    /**
     * 获取Expression Value 类型为String异常，详细基于具体实现类
     *
     * @return String
     */
    default String stringValue() {
        throw new ExpressionEvaluationException(
                "invalid to get stringValue from value of type " + type());
    }

    /**
     * 获取Expression Value 类型为Boolean异常，详细基于具体实现类
     *
     * @return Boolean
     */
    default Boolean booleanValue() {
        throw new ExpressionEvaluationException(
                "invalid to get booleanValue from value of type " + type());
    }

    /**
     * 获取Expression Value 类型为Instant异常，详细基于具体实现类
     * Instant时间戳是包含日期和时间，实上Instant就是类似JDK8以前的Date
     *
     * @return Instant 相当于Date类型
     */
    default Instant timestampValue() {
        throw new ExpressionEvaluationException(
                "invalid to get timestampValue from value of type " + type());
    }

    /**
     * 获取Expression Value 类型为LocalTime异常，详细基于具体实现类
     * LocalTime 是Java8中新增的时间类，主要包含小时.分钟.秒.纳秒等信息如: 05:40:22.111
     *
     * @return LocalTime
     */
    default LocalTime timeValue() {
        throw new ExpressionEvaluationException(
                "invalid to get timeValue from value of type " + type());
    }

    /**
     * 获取Expression Value 类型为LocalDate异常，详细基于具体实现类
     * LocalDate 是在Java 8中引入的LocalDate表示一个格式为yyyy-MM-dd的日期入: 2021-06-13
     *
     * @return LocalDate
     */
    default LocalDate dateValue() {
        throw new ExpressionEvaluationException(
                "invalid to get dateValue from value of type " + type());
    }

    /**
     * 获取Expression Value 类型为LocalDate异常，详细基于具体实现类
     * LocalDateTime是在Java 8中新特性中添加的时间处理类
     *
     * @return LocalDateTime
     */
    default LocalDateTime datetimeValue() {
        throw new ExpressionEvaluationException(
                "invalid to get datetimeValue from value of type " + type());
    }

    /**
     * 获取Expression Value 类型为TemporalAmount异常，详细基于具体实现类
     * TemporalAmount 是Java.time包下的一个接口，表示一段具体的时间（如：5天、1小时、3分钟等）
     *
     * @return TemporalAmount
     */
    default TemporalAmount intervalValue() {
        throw new ExpressionEvaluationException(
                "invalid to get intervalValue from value of type " + type());
    }

    /**
     * 获取Expression Value 类型为Tuple类型(集合类型)Map<String, ExprValue>异常，详细基于具体实现类
     *
     * @return Map<String, ExprValue>
     */
    default Map<String, ExprValue> tupleValue() {
        throw new ExpressionEvaluationException(
                "invalid to get tupleValue from value of type " + type());
    }

    /**
     * 获取Expression Value 类型为collection类型(数组类型)List<ExprValue>异常，详细基于具体实现类
     *
     * @return List<ExprValue>
     */
    default List<ExprValue> collectionValue() {
        throw new ExpressionEvaluationException(
                "invalid to get collectionValue from value of type " + type());
    }

    /**
     * 基于KEY获取Expression Value 类型为ExprValue类型，详细基于具体实现类
     *
     * @param key String 基于key值获取Value值
     * @return ExprValue
     */
    default ExprValue keyValue(String key) {
        return ExprMissingValue.of();
    }

}
