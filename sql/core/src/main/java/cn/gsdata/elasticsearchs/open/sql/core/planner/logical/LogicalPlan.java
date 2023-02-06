package cn.gsdata.elasticsearchs.open.sql.core.planner.logical;

import cn.gsdata.elasticsearchs.open.sql.core.planner.PlanNode;

/**
 * @author daiyongjun
 */
public abstract class LogicalPlan implements PlanNode<LogicalPlan> {

    public abstract <R, C> R accept(LogicalPlanNodeVisitor<R, C> visitor, C context);
}
