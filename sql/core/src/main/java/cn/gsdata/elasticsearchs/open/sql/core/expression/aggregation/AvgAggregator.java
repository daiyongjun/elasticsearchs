package cn.gsdata.elasticsearchs.open.sql.core.expression.aggregation;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;
import cn.gsdata.elasticsearchs.open.sql.core.expression.Expression;
import cn.gsdata.elasticsearchs.open.sql.core.expression.ExpressionNodeVisitor;
import cn.gsdata.elasticsearchs.open.sql.core.expression.env.Environment;
import cn.gsdata.elasticsearchs.open.sql.core.expression.function.FunctionName;

import java.util.List;

/**
 * Expression 和FunctionImplementation 实现类 均值聚合器
 *
 * @author daiyongjun
 */
public class AvgAggregator extends Aggregator<AvgAggregator.AvgState> {

    @Override
    public ExprValue valueOf(Environment<Expression, ExprValue> valueEnv) {
        return null;
    }

    @Override
    public ExprType type() {
        return null;
    }

    @Override
    public <T, C> T accept(ExpressionNodeVisitor<T, C> visitor, C context) {
        return null;
    }

    @Override
    public FunctionName getFunctionName() {
        return null;
    }

    @Override
    public List<Expression> getArguments() {
        return null;
    }

    /**
     * AggregationState 实现类 聚合状态(平均值)
     */
    protected static class AvgState implements AggregationState {
        @Override
        public ExprValue result() {
            return null;
        }
    }
}
