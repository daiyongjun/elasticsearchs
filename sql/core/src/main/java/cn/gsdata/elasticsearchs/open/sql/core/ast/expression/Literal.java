package cn.gsdata.elasticsearchs.open.sql.core.ast.expression;

import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 文字类型的表达式节点，Literal为最小单元组成
 * 内置Literal的值以及类型
 *
 * @author daiyongjun
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class Literal extends UnresolvedExpression {
    private final Object value;
    private final DataType type;

    @Override
    public List<UnresolvedExpression> getChild() {
        return ImmutableList.of();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
