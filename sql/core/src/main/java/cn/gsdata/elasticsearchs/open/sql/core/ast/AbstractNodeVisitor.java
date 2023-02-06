package cn.gsdata.elasticsearchs.open.sql.core.ast;

import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.WindowFunction;
import cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Aggregation;

/**
 * AST 节点访问抽象类(详细看具体实现),基于Antlr4的tree实现原理自定义node节点
 *
 * @author daiyongjun
 */
public abstract class AbstractNodeVisitor<T, C> {
    /**
     * 访问当前 node的具体值,详细具体看具体实现的类的实现
     *
     * @param node    Node  当前node节点信息
     * @param context C 待拓展字段
     * @return T 基于node节点信息返回node详细信息
     */
    public abstract T visit(Node node, C context);

    /**
     * 访问当前node的子节点信息
     *
     * @param node    Node  当前node节点信息
     * @param context C 待拓展字段
     * @return T  基于node节点信息返回node详细信息
     */
    public T visitChildren(Node node, C context) {
        T result = defaultResult();

        for (Node child : node.getChild()) {
            T childResult = child.accept(this, context);
            result = aggregateResult(result, childResult);
        }
        return result;
    }

    /**
     * 获取当前节点的默认值
     *
     * @return T 默认返回值
     */
    private T defaultResult() {
        return null;
    }

    /**
     * 基于defaultResult();childResult()返回子节点childResult结果
     *
     * @param aggregate  default    defaultResult()
     * @param nextResult child  childResult()
     * @return T childResult()
     */
    private T aggregateResult(T aggregate, T nextResult) {
        return nextResult;
    }

    /**
     * 访问Aggregation(node)节点的详细信息
     *
     * @param node    Aggregation   聚合节点
     * @param context C 待拓展字段
     * @return T visitChildren(node, context)
     */
    public T visitAggregation(Aggregation node, C context) {
        return visitChildren(node, context);
    }

    /**
     * 访问windowFunction(node)节点的详细信息
     *
     * @param node    WindowFunction
     * @param context C
     * @return T visitChildren(node, context)
     */
    public T visitWindowFunction(WindowFunction node, C context) {
        return visitChildren(node, context);
    }
}
