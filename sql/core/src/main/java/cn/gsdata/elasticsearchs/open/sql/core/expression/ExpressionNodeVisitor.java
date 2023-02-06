package cn.gsdata.elasticsearchs.open.sql.core.expression;

import cn.gsdata.elasticsearchs.open.sql.core.expression.function.FunctionImplementation;

/**
 * Expression节点的抽象访问者
 *
 * @author daiyongjun
 */
public abstract class ExpressionNodeVisitor<T, C> {

    /**
     * 定义Literal  Expression节点访问方法
     *
     * @param node    LiteralExpression
     * @param context C
     * @return T
     */
    public T visitLiteral(LiteralExpression node, C context) {
        return visitNode(node, context);
    }

    /**
     * 定义抽象Expression节点访问方法，详细参考实现类
     *
     * @param node    Expression 自定义的Expression节点
     * @param context C  Visitor<T,C>泛型定义的类型，保留拓展字段
     * @return T Visitor<T,C> 泛型定义的类型，返回值T
     */
    public abstract T visitNode(Expression node, C context);

    /**
     * 访问当前Expression的子节点信息
     *
     * @param node    FunctionImplementation  当前Expression(实现了FunctionImplementation)节点信息
     * @param context C 待拓展字段
     * @return T  Expression的子节点信息
     */
    public T visitChildren(FunctionImplementation node, C context) {
        T result = defaultResult();

        for (Expression child : node.getArguments()) {
            T childResult = child.accept(this, context);
            result = aggregateResult(result, childResult);
        }
        return result;
    }

    /**
     * 获取默认结果值
     *
     * @return T  Visitor<T,C> 泛型定义的类型，返回值T
     */
    private T defaultResult() {
        return null;
    }

    /**
     * 汇总expression信息和child expression信息
     *
     * @param aggregate  expression信息
     * @param nextResult child expression信息
     * @return T
     */
    private T aggregateResult(T aggregate, T nextResult) {
        return nextResult;
    }
}
