package cn.gsdata.elasticsearchs.open.sql.core.ast.expression;


import cn.gsdata.elasticsearchs.open.sql.core.ast.Node;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

/**
 * 定义使用所有字段
 *
 * @author daiyongjun
 */
@ToString
@EqualsAndHashCode(callSuper = false)
public class AllFields extends UnresolvedExpression {
    /**
     * 定义常量AllFields (INSTANCE)
     */
    public static final AllFields INSTANCE = new AllFields();

    public static AllFields of() {
        return INSTANCE;
    }

    @Override
    public List<? extends Node> getChild() {
        return Collections.emptyList();
    }

}
