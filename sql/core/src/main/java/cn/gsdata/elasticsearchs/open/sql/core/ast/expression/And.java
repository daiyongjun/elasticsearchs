package cn.gsdata.elasticsearchs.open.sql.core.ast.expression;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

/**
 * 逻辑与的表达式节点
 *
 * @author daiyongjun
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class And extends UnresolvedExpression {
    private final UnresolvedExpression left;
    private final UnresolvedExpression right;

    @Override
    public List<UnresolvedExpression> getChild() {
        return Arrays.asList(left, right);
    }
}
