package cn.gsdata.elasticsearchs.open.sql.core.data.type;

import lombok.experimental.UtilityClass;

import static cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType.UNKNOWN;

/**
 * 表达式值的扩大类型规则的定义,详细如下，int类型其扩大类型是long,float,double
 * Expression Type      Widens to data types(扩大的数据类型列表)
 * INTEGER                 LONG, FLOAT, DOUBLE
 * LONG                       FLOAT, DOUBLE
 * FLOAT                     FLOAT
 * DOUBLE                  DOUBLE
 * STRING                  STRING
 * BOOLEAN               BOOLEAN
 * ARRAY                     ARRAY
 * STRUCT                   STRUCT
 *
 * @author daiyongjun
 */
@UtilityClass
public class WideningTypeRule {
    /**
     * 类型不关联其中一个TYPE为UNKNOWN
     */
    public static final int IMPOSSIBLE_WIDENING = Integer.MAX_VALUE;
    /**
     * 类型相关联
     */
    public static final int TYPE_EQUAL = 0;

    /**
     * 从一种类型扩大到另一种类型时所扩大的距离计算。例如，距离(INTEGER, FLOAT) = 2，但距离(FLOAT, INTEGER) =不可能扩大
     *
     * @param type1 ExprType
     * @param type2 ExprType
     * @return int  从一种类型扩大到另一种类型时所扩大的距离。
     */
    public static int distance(ExprType type1, ExprType type2) {
        return distance(type1, type2, TYPE_EQUAL);
    }

    /**
     * 从一种类型扩大到另一种类型时所扩大的距离计算核心类。例如，距离(INTEGER, FLOAT) = 2，但距离(FLOAT, INTEGER) =不可能扩大
     * 类型扩大距离计算如，INTEGER 扩大类型为LONG, FLOAT, DOUBLE  distance(INTEGER, FLOAT,TYPE_EQUAL + 1)
     *
     * @param type1    ExprType
     * @param type2    ExprType
     * @param distance int
     * @return int  从一种类型扩大到另一种类型时所扩大的距离。
     */
    private static int distance(ExprType type1, ExprType type2, int distance) {
        if (type1 == type2) {
            return distance;
        } else if (type1 == UNKNOWN) {
            return IMPOSSIBLE_WIDENING;
        } else {
            //如:distance(LONG, DOUBLE) = 3
            //INTEGER 扩大类型为LONG, FLOAT, DOUBLE
            // INTEGER -> LONG   distance(LONG, DOUBLE,TYPE_EQUAL + 1)
            // LONG -> FLOAT   distance(FLOAT, DOUBLE, TYPE_EQUAL + 1 + 1)
            // LONG -> DOUBLE   distance(DOUBLE, DOUBLE, TYPE_EQUAL + 1 + 1 + 1)
            return type1.getParent().stream().map(parentOfType1 -> distance(parentOfType1, type2, distance + 1)).reduce(Math::min).get();
        }
    }
}
