package cn.gsdata.elasticsearchs.open.sql.core.ast.tree;

import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.UnresolvedExpression;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 定义关系节点（关系节点为搜索源）
 *
 * @author daiyongjun
 */
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class Relation extends UnresolvedPlan {
    /**
     * 搜索源的表名的表达式
     */
    private final UnresolvedExpression tableName;
    /**
     * 关系的别名
     */
    private String alias;

    /**
     * 获取表名 搜索源的表名表达式的名称
     *
     * @return String
     */
    public String getTableName() {
        return tableName.toString();
    }

    /**
     * 获取原始表名或别名(如果在别名中存在)
     *
     * @return 表名或别名
     */
    public String getTableNameOrAlias() {
        return (alias == null) ? getTableName() : alias;
    }

    @Override
    public UnresolvedPlan attach(UnresolvedPlan child) {
        return this;
    }
}
