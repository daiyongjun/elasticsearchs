package cn.gsdata.elasticsearchs.open.sql.core.data.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 定义expression的类型
 *
 * @author daiyongjun
 */
public enum ExprCoreType implements ExprType {
    /**
     * 由于不受支持的数据类型，未知。
     */
    UNKNOWN,
    /**
     * 特殊文字的未定义类型，例如 NULL。作为数据类型树的根，它与任何其他类型兼容。
     * BYTE(UNDEFINED)、BOOLEAN(UNDEFINED)、  STRING(UNDEFINED).....
     */
    UNDEFINED,
    /**
     * Numbers类型
     */
    BYTE(UNDEFINED),
    SHORT(BYTE),
    INTEGER(SHORT),
    LONG(INTEGER),
    FLOAT(LONG),
    DOUBLE(FLOAT),
    /**
     * Boolean.
     */
    BOOLEAN(UNDEFINED),
    /**
     * String.
     */
    STRING(UNDEFINED),
    /**
     * Date.
     */
    TIMESTAMP(UNDEFINED),
    DATE(UNDEFINED),
    TIME(UNDEFINED),
    DATETIME(UNDEFINED),
    INTERVAL(UNDEFINED),
    /**
     * Struct. Tuple
     */
    STRUCT(UNDEFINED),
    /**
     * Array.
     */
    ARRAY(UNDEFINED);

    /**
     * 当前基本类型的父级（更广泛兼容的类型）。
     */
    private final List<ExprType> parents = new ArrayList<>();

    /**
     * 构造方法创建父类
     *
     * @param compatibleTypes 兼容类型
     */
    ExprCoreType(ExprCoreType... compatibleTypes) {
        for (ExprCoreType subType : compatibleTypes) {
            subType.parents.add(this);
        }
    }

    @Override
    public List<ExprType> getParent() {
        return parents.isEmpty() ? ExprType.super.getParent() : parents;
    }

    @Override
    public String typeName() {
        return this.name();
    }

    /**
     * 返回所有有效的ExprCoreType。
     *
     * @return List<ExprCoreType>
     */
    public static List<ExprCoreType> coreTypes() {
        return Arrays.stream(ExprCoreType.values())
                .filter(type -> type != UNKNOWN)
                .filter(type -> type != UNDEFINED)
                .collect(Collectors.toList());
    }

}
