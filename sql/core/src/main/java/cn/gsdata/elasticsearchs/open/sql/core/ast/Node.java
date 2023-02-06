package cn.gsdata.elasticsearchs.open.sql.core.ast;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * 根据QuerySpecificationContext(超类tree)的原理，定义node,ast 的node超类
 *
 * @author daiyongjun
 */
@EqualsAndHashCode
@ToString
public class Node {
    /**
     * 使用传入Node访问类,返回当前Node信息
     *
     * @param visitor AbstractNodeVisitor<R, C> 节点访问抽象类
     * @param context C 带拓展字段
     * @param <R>     AbstractNodeVisitor<R,C>定义的当前节点的返回类型
     * @param <C>     AbstractNodeVisitor<R,C>定义的当前对象待拓展类型
     * @return <R, C> 定义的泛形类型
     */
    public <R, C> R accept(AbstractNodeVisitor<R, C> visitor, C context) {
        return visitor.visitChildren(this, context);
    }

    /**
     * 获取当前Node节点下的所有子节点信息
     *
     * @return List<? extends Node>
     */
    public List<? extends Node> getChild() {
        return null;
    }
}
