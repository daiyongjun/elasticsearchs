package cn.gsdata.elasticsearchs.open.sql.parser.context;

import cn.gsdata.elasticsearchs.open.sql.common.utils.StringUtils;
import cn.gsdata.elasticsearchs.open.sql.core.ast.dsl.AstDSL;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.DataType;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.Literal;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.QualifiedName;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.UnresolvedExpression;
import cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.*;
import cn.gsdata.elasticsearchs.open.sql.core.exception.SemanticCheckException;
import cn.gsdata.elasticsearchs.open.sql.parser.AstExpressionBuilder;
import gen.OpenDistroSQLParser.*;
import gen.OpenDistroSQLParserBaseVisitor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.*;

import static cn.gsdata.elasticsearchs.open.sql.parser.ParserUtils.createSortOption;
import static cn.gsdata.elasticsearchs.open.sql.parser.ParserUtils.getTextInQuery;

/**
 * 定义querySpecification Context类
 * querySpecification : selectClaus fromClause? limitClause?
 *
 * @author daiyongjun
 */
@Setter
@Getter
@ToString
public class QuerySpecification {
    /**
     * selectClaus : SELECT selectSpec? selectElements
     * SELECT : 'SELECT'
     * selectSpec : (ALL | DISTINCT)
     * selectElements : (star=STAR | selectElement) (COMMA selectElement)*
     * selectElement : expression (AS? alias)?
     * selectItems  用于存储 expression  详细见 visitSelectElement 方法
     */
    private final List<UnresolvedExpression> selectItems = new ArrayList<>();

    /**
     * selectItems 对应的别名信息
     */
    private final Map<String, UnresolvedExpression> selectItemsByAlias = new HashMap<>();

    /**
     * aggregateFunctionCall : aggregateFunction
     * aggregateFunction : functionName=aggregationFunctionName LR_BRACKET functionArg RR_BRACKET
     * | COUNT LR_BRACKET STAR RR_BRACKET
     * aggregationFunctionName  : AVG | COUNT | SUM | MIN | MAX
     */
    private final Set<UnresolvedExpression> aggregators = new LinkedHashSet<>();

    /**
     * groupByClause :  GROUP BY groupByElements 有序列表
     * GROUP BY : 'GROUP' 'BY'
     * groupByElements : groupByElement (COMMA groupByElement)*
     * COMMA : ','
     * groupByElement : expression
     */
    private final List<UnresolvedExpression> groupByItems = new ArrayList<>();

    /**
     * orderByClause : ORDER BY orderByElement (COMMA orderByElement)*
     * orderByElement : expression order=(ASC | DESC)? (NULLS (FIRST | LAST))?
     */
    private final List<UnresolvedExpression> orderByItems = new ArrayList<>();

    /**
     * orderByClause : ORDER BY orderByElement (COMMA orderByElement)*
     * orderByElement : expression order=(ASC | DESC)? (NULLS (FIRST | LAST))?
     * order 中参数信息如(ASC | DESC)? (NULLS (FIRST | LAST))?
     */
    private final List<SortOption> orderByOptions = new ArrayList<>();

    /**
     * 收集解析树中除子查询外的所有查询信息
     * 功能类似于  ParseTree parseTree = parser.parse(query);  return parseTree.accept(new AstBuilder(query));
     * 解析QuerySpecification的内容
     */
    public void collect(QuerySpecificationContext query, String queryString) {
        query.accept(new QuerySpecificationBuilder(queryString));
    }

    /**
     * 替换未解析的表达式，如果它是表示的别名或序数SELECT列表中的实际表达式 group by alias 或者 group by id
     *
     * @param expr 更换项目
     * @return 选择给定expr表示的项
     */
    public UnresolvedExpression replaceIfAliasOrOrdinal(UnresolvedExpression expr) {
        // 未解析的表达式是 id
        if (isIntegerLiteral(expr)) {
            return getSelectItemByOrdinal(expr);
            // 未解析的表达式是 alias
        } else if (isSelectAlias(expr)) {
            return getSelectItemByAlias(expr);
        } else {
            return expr;
        }
    }

    /**
     * 判断表达式是否是Literal(Integer)类型
     *
     * @param expr UnresolvedExpression 待判断的表达式
     * @return boolean
     */
    private boolean isIntegerLiteral(UnresolvedExpression expr) {
        if (!(expr instanceof Literal)) {
            return false;
        }
        if (((Literal) expr).getType() != DataType.INTEGER) {
            throw new SemanticCheckException(StringUtils.format(
                    "Non-integer constant [%s] found in ordinal", expr));
        }
        return true;
    }

    /**
     * 替换未解析的表达式，未解析的表达式为ID，替换SELECT列表中的实际表达式
     *
     * @param expr UnresolvedExpression 未解析的表达式
     * @return UnresolvedExpression    SELECT列表中的实际表达式
     */
    private UnresolvedExpression getSelectItemByOrdinal(UnresolvedExpression expr) {
        int ordinal = (Integer) ((Literal) expr).getValue();
        if (ordinal <= 0 || ordinal > selectItems.size()) {
            throw new SemanticCheckException(StringUtils.format(
                    "Ordinal [%d] is out of bound of select item list", ordinal));
        }
        return selectItems.get(ordinal - 1);
    }

    /**
     * 检查未解析的表达式是否为别名
     *
     * @param expr UnresolvedExpression 未解析的表达式
     * @return boolean 是否为别名
     */
    public boolean isSelectAlias(UnresolvedExpression expr) {
        return (expr instanceof QualifiedName)
                && (selectItemsByAlias.containsKey(expr.toString()));
    }

    /**
     * 替换未解析的表达式，未解析的表达式为Alias，替换SELECT列表中的实际表达式
     *
     * @param expr alias 未解析的表达式
     * @return expression 替换SELECT列表中的实际表达式
     */
    public UnresolvedExpression getSelectItemByAlias(UnresolvedExpression expr) {
        return selectItemsByAlias.get(expr.toString());
    }

    /**
     * QuerySpecification构建器，大部分node, 内含tree和expression只会收集tree，expression创建所需要的信息内容
     *
     * @author daiyongjun
     */
    public class QuerySpecificationBuilder extends OpenDistroSQLParserBaseVisitor<Void> {

        private final AstExpressionBuilder expressionBuilder = new AstExpressionBuilder();

        private final String queryString;

        public QuerySpecificationBuilder(String queryString) {
            this.queryString = queryString;
        }

        @Override
        public Void visitSubQueryAsRelation(SubqueryAsRelationContext ctx) {
            return null;
        }

        @Override
        public Void visitWindowFunctionClause(WindowFunctionClauseContext ctx) {
            return null;
        }

        /**
         * selectSpec : (ALL | DISTINCT)
         * DISTINCT : groupByItems
         */
        @Override
        public Void visitSelectClause(SelectClauseContext ctx) {
            super.visitSelectClause(ctx);
            // SELECT DISTINCT 是GROUP BY的等价和特殊形式
            if (isDistinct(ctx.selectSpec())) {
                groupByItems.addAll(selectItems);
            }
            return null;
        }

        /**
         * selectElement : expression (AS? alias)?
         * expression :  selectItems
         * (AS? alias)? :  selectItemsByAlias
         */
        @Override
        public Void visitSelectElement(SelectElementContext ctx) {
            UnresolvedExpression expr = visitAstExpression(ctx.expression());
            selectItems.add(expr);
            if (ctx.alias() != null) {
                String alias = StringUtils.unquoteIdentifier(ctx.alias().getText());
                selectItemsByAlias.put(alias, expr);
            }
            return super.visitSelectElement(ctx);
        }

        /**
         * groupByClause :  GROUP BY groupByElements
         * GROUP BY : 'GROUP' 'BY'
         * groupByElements : groupByElement (COMMA groupByElement)*
         * COMMA : ','
         * groupByElement : expression
         */
        @Override
        public Void visitGroupByElement(GroupByElementContext ctx) {
            groupByItems.add(visitAstExpression(ctx));
            return super.visitGroupByElement(ctx);
        }

        /**
         * orderByClause : ORDER BY orderByElement (COMMA orderByElement)*
         * orderByElement : expression order=(ASC | DESC)? (NULLS (FIRST | LAST))?
         */
        @Override
        public Void visitOrderByElement(OrderByElementContext ctx) {
            orderByItems.add(visitAstExpression(ctx.expression()));
            orderByOptions.add(createSortOption(ctx));
            return super.visitOrderByElement(ctx);
        }

        /**
         * aggregateFunctionCall : aggregateFunction
         * aggregateFunction : functionName=aggregationFunctionName LR_BRACKET functionArg RR_BRACKET
         * | COUNT LR_BRACKET STAR RR_BRACKET
         * aggregationFunctionName  : AVG | COUNT | SUM | MIN | MAX
         */
        @Override
        public Void visitAggregateFunctionCall(AggregateFunctionCallContext ctx) {
            aggregators.add(AstDSL.alias(getTextInQuery(ctx, queryString), visitAstExpression(ctx)));
            return super.visitAggregateFunctionCall(ctx);
        }

        ///**
        // *
        // */
        //@Override
        //public Void visitFilteredAggregationFunctionCall(FilteredAggregationFunctionCallContext ctx) {
        //    UnresolvedExpression aggregateFunction = visitAstExpression(ctx);
        //    aggregators.add(
        //            AstDSL.alias(getTextInQuery(ctx, queryString), aggregateFunction));
        //    return super.visitFilteredAggregationFunctionCall(ctx);
        //}

        /**
         * 定义自定义方法访问Ast表达式
         *
         * @param tree ParseTree node节点之tree
         * @return UnresolvedExpression
         */
        private UnresolvedExpression visitAstExpression(ParseTree tree) {
            return expressionBuilder.visit(tree);
        }

        /**
         * 判断当前ctx 是否为Distinct
         *
         * @param ctx SelectSpecContext
         * @return boolean
         */
        private boolean isDistinct(SelectSpecContext ctx) {
            return (ctx != null) && (ctx.DISTINCT() != null);
        }
    }
}
