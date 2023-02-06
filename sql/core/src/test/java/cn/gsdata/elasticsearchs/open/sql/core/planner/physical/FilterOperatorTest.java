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

import java.util.LinkedHashMap;
import java.util.List;

import static cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValueUtils.LITERAL_MISSING;
import static cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValueUtils.LITERAL_NULL;
import static cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType.INTEGER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FilterOperatorTest extends PhysicalPlanTestBase {
    @Mock
    private PhysicalPlan inputPlan;

    @Test
    public void filterTest() {
        FilterOperator plan = new FilterOperator(new TestScan(),
                dsl.equal(DSL.ref("response", INTEGER), DSL.literal(404)));
        List<ExprValue> result = execute(plan);
        assertEquals(1, result.size());
        assertThat(result, containsInAnyOrder(ExprValueUtils
                .tupleValue(ImmutableMap
                        .of("ip", "209.160.24.63", "action", "GET", "response", 404, "referer",
                                "www.amazon.com"))));
    }

    @Test
    public void nullValueShouldBeenIgnored() {
        LinkedHashMap<String, ExprValue> value = new LinkedHashMap<>();
        value.put("response", LITERAL_NULL);
        when(inputPlan.hasNext()).thenReturn(true, false);
        when(inputPlan.next()).thenReturn(new ExprTupleValue(value));

        FilterOperator plan = new FilterOperator(inputPlan,
                dsl.equal(DSL.ref("response", INTEGER), DSL.literal(404)));
        List<ExprValue> result = execute(plan);
        assertEquals(0, result.size());
    }

    @Test
    public void missingValueShouldBeenIgnored() {
        LinkedHashMap<String, ExprValue> value = new LinkedHashMap<>();
        value.put("response", LITERAL_MISSING);
        when(inputPlan.hasNext()).thenReturn(true, false);
        when(inputPlan.next()).thenReturn(new ExprTupleValue(value));

        FilterOperator plan = new FilterOperator(inputPlan,
                dsl.equal(DSL.ref("response", INTEGER), DSL.literal(404)));
        List<ExprValue> result = execute(plan);
        assertEquals(0, result.size());
    }
}