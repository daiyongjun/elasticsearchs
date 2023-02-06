package cn.gsdata.elasticsearchs.open.sql.core.storage.bindingtuple;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprMissingValue;
import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import cn.gsdata.elasticsearchs.open.sql.core.exception.ExpressionEvaluationException;
import cn.gsdata.elasticsearchs.open.sql.core.expression.Expression;
import cn.gsdata.elasticsearchs.open.sql.core.expression.ReferenceExpression;
import cn.gsdata.elasticsearchs.open.sql.core.expression.env.Environment;

/**
 * Environment<Expression, ExprValue> 实现类，使用resolve方法，输入Expression，返回ExprValue类型的结果
 *
 * @author daiyongjun
 */
public abstract class BindingTuple implements Environment<Expression, ExprValue> {
    public static BindingTuple EMPTY = new BindingTuple() {
        @Override
        public ExprValue resolve(ReferenceExpression ref) {
            return ExprMissingValue.of();
        }
    };

    @Override
    public ExprValue resolve(Expression var) {
        if (var instanceof ReferenceExpression) {
            return resolve(((ReferenceExpression) var));
        } else {
            throw new ExpressionEvaluationException(String.format("can resolve expression: %s", var));
        }
    }

    /**
     * resolve方法的具体逻辑实现
     *
     * @param ref ReferenceExpression
     * @return ExprValue
     */
    public abstract ExprValue resolve(ReferenceExpression ref);

}