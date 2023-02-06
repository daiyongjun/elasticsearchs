package cn.gsdata.elasticsearchs.open.sql.core.planner.physical;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import cn.gsdata.elasticsearchs.open.sql.core.planner.PlanNode;

import java.util.Iterator;

/**
 * @author daiyongjun
 */
public abstract class PhysicalPlan implements PlanNode<PhysicalPlan>, Iterator<ExprValue>, AutoCloseable {

    /**
     * 使用传入PhysicalPlanNode访问类,返回当前PhysicalPlanNode信息
     *
     * @param visitor PhysicalPlanNodeVisitor<R, C>
     * @param context C
     * @return <R,C>
     */
    public abstract <R, C> R accept(PhysicalPlanNodeVisitor<R, C> visitor, C context);

    /**
     * open/close
     * PhysicalPlan::open  physicalPlan.open();
     */
    public void open() {
        getChild().forEach(PhysicalPlan::open);
    }

    /**
     * AutoCloseable具体实现方法,  使用try-catch声明资源,jvm会自动执行close方法
     */
    @Override
    public void close() {
        getChild().forEach(PhysicalPlan::close);
    }
}
