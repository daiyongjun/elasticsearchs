package cn.gsdata.elasticsearchs.open.sql.core.ast.expression;

import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 定义Literal的最小单位文字的类型（定义SQL中的数据类型），Ast对象的类型
 * 定义SQL语句的数据类型 UNKNOWN、NULL、INTEGER、LONG、DOUBLE、BOOLEAN、DATE、TIME、TIMESTAMP
 *
 * @author daiyongjun
 */
@RequiredArgsConstructor
public enum DataType {
    /**
     * 定义未匹配的数据类型
     */
    TYPE_ERROR(ExprCoreType.UNKNOWN),
    /**
     * 定义NULL数据类型
     */
    NULL(ExprCoreType.UNDEFINED),
    /**
     * 定义Number数据类型
     */
    INTEGER(ExprCoreType.INTEGER),
    LONG(ExprCoreType.LONG),
    DOUBLE(ExprCoreType.DOUBLE),
    /**
     * 定义字符串数据类型
     */
    STRING(ExprCoreType.STRING),
    /**
     * 定义逻辑类型
     */
    BOOLEAN(ExprCoreType.BOOLEAN),
    /**
     * 定义时间类型
     */
    DATE(ExprCoreType.DATE),
    TIME(ExprCoreType.TIME),
    TIMESTAMP(ExprCoreType.TIMESTAMP),

    /**
     * 定义间隔 如: interval 1 day
     */
    INTERVAL(ExprCoreType.INTERVAL);

    @Getter
    private final ExprCoreType coreType;
}
