package cn.gsdata.elasticsearchs.open.sql.core.expression;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;
import cn.gsdata.elasticsearchs.open.sql.core.expression.env.Environment;

import java.io.Serializable;

/**
 * 定义expression节点(基于node)
 *
 * @author daiyongjun
 */
public interface Expression extends Serializable {

    /**
     * 从{@link Environment<Expression, ExprValue>}具体实现的Expression Value
     *
     * @param valueEnv Environment<Expression, ExprValue>
     * @return ExprValue
     */
    ExprValue valueOf(Environment<Expression, ExprValue> valueEnv);

    /**
     * Expression 类型
     *
     * @return ExprType
     */
    ExprType type();

    /**
     * 使用传入Expression访问类,返回当前Expression信息
     *
     * @param visitor ExpressionNodeVisitor<T, C>
     * @param context C
     * @return <T,C>
     */
    <T, C> T accept(ExpressionNodeVisitor<T, C> visitor, C context);
}
