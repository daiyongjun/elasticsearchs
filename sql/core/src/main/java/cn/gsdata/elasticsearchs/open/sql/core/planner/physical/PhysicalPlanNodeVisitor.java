package cn.gsdata.elasticsearchs.open.sql.core.planner.physical;

/**
 * PhysicalPlanNode节点的抽象访问者
 *
 * @author daiyongjun
 */
public abstract class PhysicalPlanNodeVisitor<R, C> {

    protected R visitNode(PhysicalPlan node, C context) {
        return null;
    }

    public R visitProject(ProjectOperator node, C context) {
        return visitNode(node, context);
    }

    public R visitSort(SortOperator sortOperator, C context) {
        return null;
    }
}
