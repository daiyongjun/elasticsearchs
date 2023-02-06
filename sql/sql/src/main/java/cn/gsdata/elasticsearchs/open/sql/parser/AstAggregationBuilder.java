package cn.gsdata.elasticsearchs.open.sql.parser;

import cn.gsdata.elasticsearchs.open.sql.common.utils.StringUtils;
import cn.gsdata.elasticsearchs.open.sql.core.ast.Node;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.AggregateFunction;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.Alias;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.QualifiedName;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.UnresolvedExpression;
import cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Aggregation;
import cn.gsdata.elasticsearchs.open.sql.core.ast.tree.UnresolvedPlan;
import cn.gsdata.elasticsearchs.open.sql.core.exception.SemanticCheckException;
import cn.gsdata.elasticsearchs.open.sql.parser.context.QuerySpecification;
import gen.OpenDistroSQLParserBaseVisitor;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * AST聚合构建器，为以下场景构建AST聚合节点
 * 聚合的情形：
 * 1、 Explicit(显式的)GROUP BY 内容
 * 1.1、按列名或标量表达式分组(比如: SELECT DISTINCT等效、ABS(age)、fieldName)
 * SELECT ABS(age) FROM test GROUP BY ABS(age)
 * 1.2、SELECT AS子句中按别名分组
 * SELECT state AS s FROM test GROUP BY s
 * 1.3、SELECT  list 中list对应的value值进行分组
 * SELECT state FROM test GROUP BY 1
 * 2、Implicit(隐式的) GROUP BY 内容
 * 2.1、全聚合函数
 * SELECT AVG(age), SUM(balance) FROM test
 * 2.2、若干聚合函数
 * SELECT state, AVG(age) FROM test
 *
 * @author daiyongjun
 */
@RequiredArgsConstructor
public class AstAggregationBuilder extends OpenDistroSQLParserBaseVisitor<UnresolvedPlan> {

    private final QuerySpecification querySpec;

    /**
     * groupByClause :  GROUP BY groupByElements
     * GROUP  BY :  'GROUP' 'BY'
     * groupByElements : groupByElement (COMMA groupByElement)*
     * groupByElement : expression
     */
    @Override
    public UnresolvedPlan visit(ParseTree groupByClause) {
        if (querySpec.getGroupByItems().isEmpty()) {
            if (isAggregatorNotFoundAnywhere()) {
                //没有发现任何聚合信息
                return null;
            }
            //Implicit(隐式的) GROUP BY 内容 其中分为全聚合函数、若干聚合函数
            return buildImplicitAggregation();
        }
        return buildExplicitAggregation();
    }

    private boolean isAggregatorNotFoundAnywhere() {
        return querySpec.getAggregators().isEmpty();
    }

    /**
     * Implicit(隐式的) GROUP BY 内容 其中分为全聚合函数、若干聚合函数
     */
    private UnresolvedPlan buildImplicitAggregation() {
        //获取待验证的selectItem,由于group by 不能存在非non-aggregated column
        Optional<UnresolvedExpression> invalidSelectItem = findNonAggregatedItemInSelect();

        if (invalidSelectItem.isPresent()) {
            // Report semantic error to avoid fall back to old engine again
            throw new SemanticCheckException(StringUtils.format(
                    "Explicit GROUP BY clause is required because expression [%s] "
                            + "contains non-aggregated column", invalidSelectItem.get()));
        }
        // aggExprList ->  new ArrayList<>(querySpec.getAggregators())
        // emptyList() ->   sortExprList
        // groupExprList -> querySpec.getGroupByItems()
        return new Aggregation(
                new ArrayList<>(querySpec.getAggregators()),
                emptyList(),
                querySpec.getGroupByItems());
    }

    private UnresolvedPlan buildExplicitAggregation() {
        List<UnresolvedExpression> groupByItems = replaceGroupByItemIfAliasOrOrdinal();
        return new Aggregation(
                new ArrayList<>(querySpec.getAggregators()),
                emptyList(),
                groupByItems);
    }

    /**
     * 在SELECT selectSpec? selectElements中查找非聚合项。注意，literal是特殊的，聚合函数不需要应用它。
     */
    private Optional<UnresolvedExpression> findNonAggregatedItemInSelect() {
        return querySpec.getSelectItems().stream()
                .filter(this::isNonAggregateOrLiteralExpression)
                .findFirst();
    }

    /**
     * 判断select item(expr) 是否为AggregateFunction
     */
    private boolean isNonAggregateOrLiteralExpression(UnresolvedExpression expr) {
        if (expr instanceof AggregateFunction) {
            return false;
        }

        if (expr instanceof QualifiedName) {
            return true;
        }

        List<? extends Node> children = expr.getChild();
        return children.stream().anyMatch(child ->
                isNonAggregateOrLiteralExpression((UnresolvedExpression) child));
    }

    /**
     * 替换group by item 为 Alias实例
     */
    private List<UnresolvedExpression> replaceGroupByItemIfAliasOrOrdinal() {
        return querySpec.getGroupByItems()
                .stream()
                //替换group by id 或者alias
                .map(querySpec::replaceIfAliasOrOrdinal)
                //将expression替换成Alias对象
                .map(expr -> new Alias(expr.toString(), expr))
                .collect(Collectors.toList());
    }
}
