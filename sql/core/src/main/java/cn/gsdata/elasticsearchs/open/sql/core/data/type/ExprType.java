package cn.gsdata.elasticsearchs.open.sql.core.data.type;

import java.util.Collections;
import java.util.List;

import static cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType.UNKNOWN;

/**
 * Expression(value)类型
 *
 * @author daiyongjun
 */
public interface ExprType {
    /**
     * 获取类型名称
     *
     * @return String
     */
    String typeName();


    /**
     * Expression类型是否兼容
     *
     * @param other Expression类型
     * @return boolean
     */
    default boolean isCompatible(ExprType other) {
        if (this.equals(other)) {
            return true;
        } else {
            if (other.equals(UNKNOWN)) {
                return false;
            }
            for (ExprType parentTypeOfOther : other.getParent()) {
                if (isCompatible(parentTypeOfOther)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 获取父类型
     *
     * @return List<ExprType>
     */
    default List<ExprType> getParent() {
        return Collections.singletonList(UNKNOWN);
    }
}
