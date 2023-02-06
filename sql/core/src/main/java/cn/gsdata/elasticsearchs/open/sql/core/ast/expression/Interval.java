package cn.gsdata.elasticsearchs.open.sql.core.ast.expression;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

/**
 * 间隔类型的表达式节点 ，基于ES的聚合 特性定义的类型
 *
 * @author daiyongjun
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class Interval extends UnresolvedExpression {
    private final UnresolvedExpression value;
    /**
     * 定义基础单位
     */
    private final IntervalUnit unit;

    public Interval(UnresolvedExpression value, String unit) {
        this.value = value;
        this.unit = IntervalUnit.of(unit);
    }

    @Override
    public List<UnresolvedExpression> getChild() {
        return Collections.singletonList(value);
    }

}
