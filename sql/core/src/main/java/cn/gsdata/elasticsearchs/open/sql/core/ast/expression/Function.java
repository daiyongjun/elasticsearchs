package cn.gsdata.elasticsearchs.open.sql.core.ast.expression;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 函数的表达式节点。参数包括函数名 (@funcName) 和函数参数 (@funcArgs)
 *
 * @author daiyongjun
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class Function extends UnresolvedExpression {
    private final String funcName;
    private final List<UnresolvedExpression> funcArgs;

    @Override
    public List<UnresolvedExpression> getChild() {
        return Collections.unmodifiableList(funcArgs);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", funcName,
                funcArgs.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")));
    }
}
