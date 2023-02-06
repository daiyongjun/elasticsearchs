package cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common.plan;

import cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common.tree.Location;
import cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common.tree.Node;

import java.util.List;

/**
 * QueryPlan分为两种情况LogicalPlan、PhysicalPlan
 *
 * @author daiyongjun
 */
public abstract class QueryPlan<PlanType extends QueryPlan<PlanType>> extends Node<PlanType> {

    public QueryPlan(Location location, List<PlanType> children) {
        super(location, children);
    }
}
