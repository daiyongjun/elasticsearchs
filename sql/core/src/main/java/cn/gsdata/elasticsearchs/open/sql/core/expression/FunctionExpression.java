package cn.gsdata.elasticsearchs.open.sql.core.expression;

import cn.gsdata.elasticsearchs.open.sql.core.expression.function.FunctionImplementation;
import cn.gsdata.elasticsearchs.open.sql.core.expression.function.FunctionName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Function Expression
 *
 * @author daiyongjun
 */
@RequiredArgsConstructor
@ToString
public abstract class FunctionExpression implements Expression, FunctionImplementation {
    @Getter
    private final FunctionName functionName;

    @Getter
    private final List<Expression> arguments;


    @Override
    public <T, C> T accept(ExpressionNodeVisitor<T, C> visitor, C context) {
        return null;
    }
}
