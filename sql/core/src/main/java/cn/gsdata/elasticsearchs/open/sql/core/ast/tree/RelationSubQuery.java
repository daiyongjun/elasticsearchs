package cn.gsdata.elasticsearchs.open.sql.core.ast.tree;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 定义子关系节点（关系节点为搜索源）
 *
 * @author daiyongjun
 */
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@ToString
public class RelationSubQuery extends UnresolvedPlan {
    /**
     * 子关系节点
     */
    private UnresolvedPlan query;

    /**
     * 子关系节点的别名
     */
    private String alias;

    /**
     * 将子查询别名作为表名
     *
     * @return String
     */
    public String getAliasAsTableName() {
        return alias;
    }

    @Override
    public List<UnresolvedPlan> getChild() {
        return ImmutableList.of(query);
    }

    @Override
    public UnresolvedPlan attach(UnresolvedPlan child) {
        return this;
    }
}
