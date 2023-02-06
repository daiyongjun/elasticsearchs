package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.geo;

import java.util.Locale;

/**
 * 表示查询之间关系的枚举类型包含相交、不相交、
 *
 * @author daiyongjun
 */
public enum ShapeRelation {

    /**
     * 相交
     */
    INTERSECTS("intersects"),
    /**
     * 不相交
     */
    DISJOINT("disjoint"),
    /**
     * 之内
     */
    WITHIN("within"),
    /**
     * 包含
     */
    CONTAINS("contains");


    /**
     * 关系的名称
     */
    private final String relationName;

    /**
     * ShapeRelation构造方法 指定Name创建ShapeRelation
     *
     * @param relationName 返回ShapeRelation对象
     */
    ShapeRelation(String relationName) {
        this.relationName = relationName;
    }

    /**
     * ShapeRelation静态获取类 指定Name返回shapeRelation对象
     *
     * @param name 指定关系的名称
     * @return ShapeRelation
     */
    public static ShapeRelation getRelationByName(String name) {
        name = name.toLowerCase(Locale.ENGLISH);
        for (ShapeRelation relation : ShapeRelation.values()) {
            if (relation.relationName.equals(name)) {
                return relation;
            }
        }
        return null;
    }

    /**
     * 获取关系的名称
     *
     * @return String
     */
    public String getRelationName() {
        return relationName;
    }
}
