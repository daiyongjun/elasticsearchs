package cn.gsdata.elasticsearchs.open.sql.core.planner.physical;

import cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.*;
import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import cn.gsdata.elasticsearchs.open.sql.core.expression.Expression;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;

/**
 * @author daiyongjun
 */
@ToString
@EqualsAndHashCode(callSuper = false)
public class SortOperator extends PhysicalPlan {
    @Getter
    private final PhysicalPlan input;

    @Getter
    private final List<Pair<SortOption, Expression>> sortList;

    public SortOperator(
            PhysicalPlan input, List<Pair<SortOption, Expression>> sortList) {
        this.input = input;
        this.sortList = sortList;
    }


    @Override
    public List<PhysicalPlan> getChild() {
        return Collections.singletonList(input);
    }

    @Override
    public <R, C> R accept(PhysicalPlanNodeVisitor<R, C> visitor, C context) {
        return visitor.visitSort(this, context);
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public ExprValue next() {
        return null;
    }
}
