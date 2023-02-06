package cn.gsdata.elasticsearchs.open.sql.core.ast.expression;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

/**
 * 逻辑非的表达式节点
 *
 * @author daiyongjun
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class Not extends UnresolvedExpression {
    private final UnresolvedExpression expression;

    @Override
    public List<UnresolvedExpression> getChild() {
        return Collections.singletonList(expression);
    }
}
