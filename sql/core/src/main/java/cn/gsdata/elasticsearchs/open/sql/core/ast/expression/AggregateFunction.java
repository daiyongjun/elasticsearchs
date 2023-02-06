package cn.gsdata.elasticsearchs.open.sql.core.ast.expression;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * 聚合函数的表达式节点。
 * 参数包括聚合函数名(AVG, SUM, MAX等)和聚合字段。
 *
 * @author daiyongjun
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class AggregateFunction extends UnresolvedExpression {
    private final String funcName;
    private final UnresolvedExpression field;

    @Override
    public List<UnresolvedExpression> getChild() {
        return Collections.singletonList(field);
    }
}
