package cn.gsdata.elasticsearchs.open.sql.core.ast.expression;

import lombok.*;

/**
 * @author daiyongjun
 */
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@RequiredArgsConstructor
@ToString
public class Alias extends UnresolvedExpression {
    /**
     * 原始字段名称
     */
    private final String name;
    /**
     * 表达式别名
     */
    private final UnresolvedExpression delegated;
    /**
     * 字段别名
     */
    private String alias;
}
