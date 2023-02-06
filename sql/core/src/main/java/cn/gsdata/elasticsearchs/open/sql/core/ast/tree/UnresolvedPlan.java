package cn.gsdata.elasticsearchs.open.sql.core.ast.tree;


import cn.gsdata.elasticsearchs.open.sql.core.ast.Node;

/**
 * Abstract unresolved plan
 *
 * @author daiyongjun
 */
public abstract class UnresolvedPlan extends Node {
    /**
     * 设置child tree信息
     *
     * @param child UnresolvedPlan
     * @return UnresolvedPlan
     */
    public abstract UnresolvedPlan attach(UnresolvedPlan child);
}
