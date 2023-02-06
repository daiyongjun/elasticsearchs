package cn.gsdata.elasticsearchs.open.sql.core.ast.tree;

import cn.gsdata.elasticsearchs.open.sql.core.ast.Node;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.UnresolvedExpression;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Filter的plan节点
 *
 * @author daiyongjun
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@Getter
public class Filter extends UnresolvedPlan {
    /**
     * condition Expression表达式
     */
    private UnresolvedExpression condition;
    /**
     * 子plan节点
     */
    private UnresolvedPlan child;

    public Filter(UnresolvedExpression condition) {
        this.condition = condition;
    }

    @Override
    public UnresolvedPlan attach(UnresolvedPlan child) {
        this.child = child;
        return this;
    }

    public List<? extends Node> getChild() {
        return null;
    }
}
