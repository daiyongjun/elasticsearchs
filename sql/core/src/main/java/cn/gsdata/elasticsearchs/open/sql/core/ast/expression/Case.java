package cn.gsdata.elasticsearchs.open.sql.core.ast.expression;

import cn.gsdata.elasticsearchs.open.sql.core.ast.Node;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * 特殊函数 case 节点函数 ,函数内置参数
 * case 语句有点类似与switch 语句  case  1 ; case 2  ;case  3 ;case 4 ....; default n;
 * CASE age WHEN 30 THEN 'age1' ELSE 'age2' END
 *
 * @author daiyongjun
 */
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@ToString
public class Case extends UnresolvedExpression {
    /**
     * case  expression
     * when 语句比较的值。如: 上述function 中的 case age 的 age  待判断的expression
     */
    private final UnresolvedExpression caseValue;

    /**
     * 表示WHEN语句的表达式列表。 如：上述function中 when 30 then  'age1'  为啥是个list  呢 case 语句类似于switch中的case
     */
    private final List<When> whenClauses;

    /**
     * else elseArg ?
     * else 语句的表达式 。 如：上述function中的  else  'age'  else 下的表达式 , else 类似case语句中的default  n;
     */
    private final UnresolvedExpression elseClause;

    @Override
    public List<? extends Node> getChild() {
        ImmutableList.Builder<Node> children = ImmutableList.builder();
        if (caseValue != null) {
            children.add(caseValue);
        }
        children.addAll(whenClauses);

        if (elseClause != null) {
            children.add(elseClause);
        }
        return children.build();
    }
}
