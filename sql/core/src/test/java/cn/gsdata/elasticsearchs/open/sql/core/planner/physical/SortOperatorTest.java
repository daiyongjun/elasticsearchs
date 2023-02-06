package cn.gsdata.elasticsearchs.open.sql.core.planner.physical;

import cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.*;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValueUtils.tupleValue;
import static cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType.INTEGER;
import static cn.gsdata.elasticsearchs.open.sql.core.expression.DSL.ref;
import static cn.gsdata.elasticsearchs.open.sql.core.planner.physical.PhysicalPlanDSL.sort;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SortOperatorTest extends PhysicalPlanTestBase {
    @Mock
    private PhysicalPlan inputPlan;

    public void sort_one_field_asc() {
        when(inputPlan.hasNext()).thenReturn(true, true, true, false);
        when(inputPlan.next())
                .thenReturn(tupleValue(ImmutableMap.of("size", 499, "response", 404)))
                .thenReturn(tupleValue(ImmutableMap.of("size", 320, "response", 200)))
                .thenReturn(tupleValue(ImmutableMap.of("size", 399, "response", 503)));
        execute(sort(inputPlan, Pair.of(SortOption.DEFAULT_ASC, ref("response", INTEGER))));
    }
}
