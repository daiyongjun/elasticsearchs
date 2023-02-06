package cn.gsdata.elasticsearchs.open.sql.core.planner;

import java.util.List;

/**
 * 根据QuerySpecificationContext(超类tree)的原理，定义PlanNode
 *
 * @author daiyongjun
 */
public interface PlanNode<T extends PlanNode> {
    /**
     * 获取当前PlanNode节点下的所有子节点信息
     *
     * @return List<T>
     */
    List<T> getChild();
}
