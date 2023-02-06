package cn.gsdata.elasticsearchs.open.sql.core.planner.physical;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprTupleValue;
import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValueUtils;
import cn.gsdata.elasticsearchs.open.sql.core.expression.DSL;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValueUtils.LITERAL_MISSING;
import static cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValueUtils.stringValue;
import static cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType.INTEGER;
import static cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType.STRING;
import static cn.gsdata.elasticsearchs.open.sql.core.planner.physical.PhysicalPlanDSL.project;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectOperatorTest extends PhysicalPlanTestBase {
    @Mock
    private PhysicalPlan inputPlan;

    @Test
    public void project_one_field() {
        when(inputPlan.hasNext()).thenReturn(true, false);
        //ExprTupleValue 定义result结果集合action: GET ....... response: 200 .......
        when(inputPlan.next())
                .thenReturn(ExprValueUtils.tupleValue(ImmutableMap.of("action", "GET", "response", 200)));
        //ProjectOperator 定义字段action string
        PhysicalPlan plan = project(inputPlan, DSL.named("action", DSL.ref("action", STRING)));
        List<ExprValue> result = execute(plan);

        assertThat(
                result, allOf(
                        iterableWithSize(1),
                        hasItems(ExprValueUtils.tupleValue(ImmutableMap.of("action", "GET")))));
    }

    @Test
    public void project_two_field_follow_the_project_order() {
        when(inputPlan.hasNext()).thenReturn(true, false);
        //ExprTupleValue 定义result结果集合action: GET ....... response: 200 .......
        when(inputPlan.next())
                .thenReturn(ExprValueUtils.tupleValue(ImmutableMap.of("action", "GET", "response", 200)));
        //ProjectOperator 定义字段response integer, action string
        PhysicalPlan plan = project(inputPlan,
                DSL.named("response", DSL.ref("response", INTEGER)),
                DSL.named("action", DSL.ref("action", STRING)));
        List<ExprValue> result = execute(plan);

        assertThat(
                result,
                allOf(
                        iterableWithSize(1),
                        hasItems(
                                ExprValueUtils.tupleValue(ImmutableMap.of("response", 200, "action", "GET")))));

    }

    @Test
    public void project_keep_missing_value() {
        when(inputPlan.hasNext()).thenReturn(true, true, false);
        when(inputPlan.next())
                .thenReturn(ExprValueUtils.tupleValue(ImmutableMap.of("action", "GET", "response", 200)))
                .thenReturn(ExprValueUtils.tupleValue(ImmutableMap.of("action", "POST")));
        PhysicalPlan plan = project(inputPlan,
                DSL.named("response", DSL.ref("response", INTEGER)),
                DSL.named("action", DSL.ref("action", STRING)));
        List<ExprValue> result = execute(plan);

        assertThat(
                result,
                allOf(
                        iterableWithSize(2),
                        hasItems(
                                ExprValueUtils.tupleValue(ImmutableMap.of("response", 200, "action", "GET")),
                                ExprTupleValue.fromExprValueMap(ImmutableMap.of("response",
                                        LITERAL_MISSING,
                                        "action", stringValue("POST"))))));
    }

    @Test
    public void project_schema() {
        PhysicalPlan project = project(inputPlan,
                DSL.named("response", DSL.ref("response", INTEGER)),
                DSL.named("action", DSL.ref("action", STRING), "act"));
    }
}
