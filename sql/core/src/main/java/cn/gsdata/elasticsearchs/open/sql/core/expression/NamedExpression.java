package cn.gsdata.elasticsearchs.open.sql.core.expression;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;
import cn.gsdata.elasticsearchs.open.sql.core.expression.env.Environment;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 命名表达式(Named Expression) 表示带有名称的表达式
 *
 * @author daiyongjun
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
public class NamedExpression implements Expression {
    /**
     * 被命名表达式的名称
     */
    private final String name;

    /**
     * 被命名的表达式
     */
    private final Expression delegated;

    /**
     * 被命名的表达式的别名
     */
    private String alias;

    @Override
    public ExprValue valueOf(Environment<Expression, ExprValue> valueEnv) {
        return delegated.valueOf(valueEnv);
    }

    @Override
    public ExprType type() {
        return delegated.type();
    }

    /**
     * 获取被命名的表达式的别名
     *
     * @return String
     */
    public String getNameOrAlias() {
        return Strings.isNullOrEmpty(alias) ? name : alias;
    }

    @Override
    public <T, C> T accept(ExpressionNodeVisitor<T, C> visitor, C context) {
        return null;
    }

    @Override
    public String toString() {
        return getNameOrAlias();
    }
}
