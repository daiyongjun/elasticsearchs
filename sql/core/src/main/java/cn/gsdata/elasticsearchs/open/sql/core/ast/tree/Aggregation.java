package cn.gsdata.elasticsearchs.open.sql.core.ast.tree;

import cn.gsdata.elasticsearchs.open.sql.core.ast.AbstractNodeVisitor;
import cn.gsdata.elasticsearchs.open.sql.core.ast.Node;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.Argument;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.UnresolvedExpression;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.Collections;
import java.util.List;

/**
 * Aggregation的逻辑规划节点
 *
 * @author daiyongjun
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class Aggregation extends UnresolvedPlan {

    /**
     * 聚合类方法 内含 aggregateFunction : AVG | COUNT | SUM | MIN | MAX | COUNT LR_BRACKET STAR RR_BRACKET)
     * aggregateFunctionCall : aggregateFunction
     * aggregateFunction : functionName=aggregationFunctionName LR_BRACKET functionArg RR_BRACKET
     * | COUNT LR_BRACKET STAR RR_BRACKET
     * aggregationFunctionName  : AVG | COUNT | SUM | MIN | MAX
     */
    private List<UnresolvedExpression> aggExprList;

    /**
     * orderByClause : ORDER BY orderByElement (COMMA orderByElement)*
     * orderByElement : expression order=(ASC | DESC)? (NULLS (FIRST | LAST))?
     */
    private List<UnresolvedExpression> sortExprList;

    /**
     * groupByClause :  GROUP BY groupByElements 有序列表
     * GROUP BY : 'GROUP' 'BY'
     * groupByElements : groupByElement (COMMA groupByElement)*
     * COMMA : ','
     * groupByElement : expression
     */
    private List<UnresolvedExpression> groupExprList;

    /**
     *
     */
    private List<Argument> argExprList;

    /**
     * 子plan节点
     */
    private UnresolvedPlan child;

    public Aggregation(List<UnresolvedExpression> aggExprList,
                       List<UnresolvedExpression> sortExprList,
                       List<UnresolvedExpression> groupExprList) {
        this(aggExprList, sortExprList, groupExprList, Collections.emptyList());
    }

    public Aggregation(List<UnresolvedExpression> aggExprList,
                       List<UnresolvedExpression> sortExprList,
                       List<UnresolvedExpression> groupExprList,
                       List<Argument> argExprList) {
        this.aggExprList = aggExprList;
        this.sortExprList = sortExprList;
        this.groupExprList = groupExprList;
        this.argExprList = argExprList;
    }

    @Override
    public UnresolvedPlan attach(UnresolvedPlan child) {
        this.child = child;
        return this;
    }

    public List<? extends Node> getChild() {
        return ImmutableList.of(this.child);
    }

    @Override
    public <T, C> T accept(AbstractNodeVisitor<T, C> nodeVisitor, C context) {
        return nodeVisitor.visitAggregation(this, context);
    }
}
