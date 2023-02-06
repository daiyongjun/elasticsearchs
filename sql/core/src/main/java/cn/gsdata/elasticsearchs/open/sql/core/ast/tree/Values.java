package cn.gsdata.elasticsearchs.open.sql.core.ast.tree;


import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.Literal;
import cn.gsdata.elasticsearchs.open.sql.core.ast.Node;
import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 定义一个文字行节点
 *
 * @author daiyongjun
 */
@ToString
@Getter
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class Values extends UnresolvedPlan {

    private final List<List<Literal>> values;

    @Override
    public UnresolvedPlan attach(UnresolvedPlan child) {
        return null;
    }

    @Override
    public List<? extends Node> getChild() {
        return ImmutableList.of();
    }

}
