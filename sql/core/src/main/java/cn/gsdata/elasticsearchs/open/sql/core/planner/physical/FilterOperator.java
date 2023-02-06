package cn.gsdata.elasticsearchs.open.sql.core.planner.physical;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import cn.gsdata.elasticsearchs.open.sql.core.expression.Expression;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

/**
 * Filter操作符使用条件来计算输入BindingTuple(input.next())
 *
 * @author daiyongjun
 */
@ToString
@RequiredArgsConstructor
public class FilterOperator extends PhysicalPlan {
    /**
     * 输出结果集
     */
    @Getter
    private final PhysicalPlan input;
    /**
     * Filter过滤表达式
     */
    @Getter
    private final Expression conditions;
    /**
     * next的临时存储
     */
    @ToString.Exclude
    private ExprValue next = null;


    @Override
    public List<PhysicalPlan> getChild() {
        return Collections.singletonList(input);
    }

    @Override
    public <R, C> R accept(PhysicalPlanNodeVisitor<R, C> visitor, C context) {
        return null;
    }

    @Override
    public boolean hasNext() {
        while (input.hasNext()) {
            ExprValue inputValue = input.next();
            ExprValue exprValue = conditions.valueOf(inputValue.bindingTuples());
            boolean condition = !(exprValue.isNull() || exprValue.isMissing()) && (exprValue.booleanValue());
            if (condition) {
                next = inputValue;
                return true;
            }
        }
        return false;
    }

    @Override
    public ExprValue next() {
        return next;
    }
}
