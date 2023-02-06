package cn.gsdata.elasticsearchs.open.sql.core.planner.physical;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValueUtils;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LimitOperatorTest extends PhysicalPlanTestBase {
    @Test
    public void limit() {
        PhysicalPlan plan = new LimitOperator(new TestScan(), 1, 0);
        List<ExprValue> result = execute(plan);
        assertEquals(1, result.size());
        assertThat(result, containsInAnyOrder(
                ExprValueUtils.tupleValue(ImmutableMap.of(
                        "ip", "209.160.24.63", "action", "GET", "response", 200, "referer", "www.amazon.com"))
        ));
    }

    @Test
    public void limit_and_offset() {
        PhysicalPlan plan = new LimitOperator(new TestScan(), 1, 1);
        List<ExprValue> result = execute(plan);
        assertEquals(1, result.size());
        assertThat(result, containsInAnyOrder(
                ExprValueUtils.tupleValue(ImmutableMap.of(
                        "ip", "209.160.24.63", "action", "GET", "response", 404, "referer", "www.amazon.com"))
        ));
    }

    @Test
    public void offset_exceeds_row_number() {
        PhysicalPlan plan = new LimitOperator(new TestScan(), 1, 6);
        List<ExprValue> result = execute(plan);
        assertEquals(0, result.size());
    }
}