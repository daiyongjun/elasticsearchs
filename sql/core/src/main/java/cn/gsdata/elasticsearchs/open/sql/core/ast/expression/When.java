package cn.gsdata.elasticsearchs.open.sql.core.ast.expression;

import cn.gsdata.elasticsearchs.open.sql.core.ast.Node;
import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 定义case 中的 When Express 节点 以及 Then Express 节点，内含 where Express 内容 以及 then Express 内容
 *
 * @author daiyongjun
 */
@EqualsAndHashCode(callSuper = false)
@Getter
@RequiredArgsConstructor
@ToString
public class When extends UnresolvedExpression {
    /**
     * where Express 内容
     */
    private final UnresolvedExpression condition;
    /**
     * then Express 内容 （result(类似于function中的return) ：case age when 30 then 40）
     */
    private final UnresolvedExpression result;

    @Override
    public List<? extends Node> getChild() {
        return ImmutableList.of(condition, result);
    }
}
