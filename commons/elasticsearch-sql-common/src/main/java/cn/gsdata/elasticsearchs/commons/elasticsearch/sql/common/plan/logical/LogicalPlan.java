package cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common.plan.logical;

import cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common.plan.QueryPlan;
import cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common.tree.Location;

import java.util.List;

/**
 * LogicalPlan 表述为LogicalPlan 是用户告诉我们他们想做的<b>what<b>（不是“如何”）。
 * 简述： LogicalPlan为我想做啥，PhysicalPlan我该如何去做。
 *
 * @author daiyongjun
 */
public abstract class LogicalPlan extends QueryPlan<LogicalPlan> {
    public LogicalPlan(Location location, List<LogicalPlan> children) {
        super(location, children);
    }
}
