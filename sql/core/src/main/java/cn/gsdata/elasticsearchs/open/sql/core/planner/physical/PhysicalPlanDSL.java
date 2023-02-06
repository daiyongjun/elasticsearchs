package cn.gsdata.elasticsearchs.open.sql.core.planner.physical;

import cn.gsdata.elasticsearchs.open.sql.core.expression.NamedExpression;

import java.util.Arrays;

/**
 * PhysicalPlan 具体实现类
 *
 * @author daiyongjun
 */
public class PhysicalPlanDSL {

    public static ProjectOperator project(PhysicalPlan input, NamedExpression... fields) {
        return new ProjectOperator(input, Arrays.asList(fields));
    }
}
