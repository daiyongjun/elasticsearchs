package cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common.parser;

import cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common.util.Check;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * 自定义Visitor的逻辑
 *
 * @author daiyongjun
 */
public abstract class AbstractBuilder extends SqlBaseBaseVisitor<Object> {
    /**
     * 重写visit方法，访问所有节点内容
     *
     * @param tree 所有节点内容
     * @return Object 定义的范型节点信息
     */
    @Override
    public Object visit(ParseTree tree) {
        Object result = super.visit(tree);
        Check.notNull(result, "Don't know how to handle context [{}] with value [{}]", tree.getClass(), tree.getText());
        return result;
    }

}
