package cn.gsdata.elasticsearchs.open.sql.core.expression;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;
import cn.gsdata.elasticsearchs.open.sql.core.expression.env.Environment;
import lombok.RequiredArgsConstructor;

/**
 * 文字表达式(Literal  Expression）和Literal定义差不多
 *
 * @author daiyongjun
 */
@RequiredArgsConstructor
public class LiteralExpression implements Expression {
    private final ExprValue exprValue;

    @Override
    public ExprValue valueOf(Environment<Expression, ExprValue> valueEnv) {
        return exprValue;
    }

    @Override
    public ExprType type() {
        return exprValue.type();
    }

    @Override
    public <T, C> T accept(ExpressionNodeVisitor<T, C> visitor, C context) {
        return visitor.visitLiteral(this, context);
    }
}
