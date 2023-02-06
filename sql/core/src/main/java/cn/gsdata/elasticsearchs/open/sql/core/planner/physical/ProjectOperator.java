package cn.gsdata.elasticsearchs.open.sql.core.planner.physical;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprTupleValue;
import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import cn.gsdata.elasticsearchs.open.sql.core.expression.NamedExpression;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * Project Operator 的PhysicalPlan
 *
 * @author daiyongjun
 */
@RequiredArgsConstructor
public class ProjectOperator extends PhysicalPlan {
    @Getter
    private final PhysicalPlan input;

    @Getter
    private final List<NamedExpression> projectList;

    @Override
    public List<PhysicalPlan> getChild() {
        return Collections.singletonList(input);
    }

    @Override
    public <R, C> R accept(PhysicalPlanNodeVisitor<R, C> visitor, C context) {
        return visitor.visitProject(this, context);
    }

    /**
     * Iterable实现类，具体方法(hasNext)，询问有没有下一个元素
     *
     * @return boolean
     */
    @Override
    public boolean hasNext() {
        return input.hasNext();
    }

    /**
     * Iterable实现类，具体方法(next)，返回迭代的下一个元素(ExprValue)
     *
     * @return ExprValue
     */
    @Override
    public ExprValue next() {
        ExprValue inputValue = input.next();
        ImmutableMap.Builder<String, ExprValue> mapBuilder = new ImmutableMap.Builder<>();
        for (NamedExpression expr : projectList) {
            ExprValue exprValue = expr.valueOf(inputValue.bindingTuples());
            mapBuilder.put(expr.getNameOrAlias(), exprValue);
        }
        return ExprTupleValue.fromExprValueMap(mapBuilder.build());
    }
}
