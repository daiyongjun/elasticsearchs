package cn.gsdata.elasticsearchs.open.sql.core.ast.expression;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * 构建搜索字段列表的参数
 *
 * @author daiyongjun
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class Argument extends UnresolvedExpression {
    private final String argName;
    private final Literal value;

    @Override
    public List<UnresolvedExpression> getChild() {
        return Collections.singletonList(value);
    }

}
