package cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common.plan.physical;

import cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common.plan.QueryPlan;
import cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common.session.Executable;
import cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common.tree.Location;

import java.util.List;

/**
 * PhysicalPlan 是“如何”将 LogicalPlan（“什么”）实际转换为一个或多个查询。
 * 比如：LogicalPlan = 我想从公司到地铁口
 * PhysicalPlan = 实际乘坐电梯从公司到摆渡车口，然后乘坐摆渡车到地铁口
 *
 * @author daiyongjun
 */
public class PhysicalPlan extends QueryPlan<PhysicalPlan> implements Executable {
    public PhysicalPlan(Location location, List<PhysicalPlan> children) {
        super(location, children);
    }
}
