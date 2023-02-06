package cn.gsdata.elasticsearchs.open.sql.parser;

import cn.gsdata.elasticsearchs.open.sql.common.antlr.SyntaxCheckException;
import cn.gsdata.elasticsearchs.open.sql.common.utils.StringUtils;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.Alias;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.AllFields;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.UnresolvedExpression;
import cn.gsdata.elasticsearchs.open.sql.core.ast.tree.*;
import cn.gsdata.elasticsearchs.open.sql.parser.context.ParsingContext;
import com.google.common.collect.ImmutableList;
import gen.OpenDistroSQLParser.*;
import gen.OpenDistroSQLParserBaseVisitor;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Optional;

import static java.util.Collections.emptyList;

/**
 * Ast(定义的基础类型)构建类，Ast 定义为node,其详细实现有expression和tree
 *
 * @author daiyongjun
 */
@RequiredArgsConstructor
public class AstBuilder extends OpenDistroSQLParserBaseVisitor<UnresolvedPlan> {
    /**
     * SQL解析上下文，内建QuerySpecification队列
     */
    private final ParsingContext context = new ParsingContext();


    private final AstExpressionBuilder expressionBuilder = new AstExpressionBuilder();

    /**
     * 输入的SQL语句
     */
    private final String query;

    /**
     * context解析开始的地方
     * querySpecification : selectClause fromClause? limitClause?
     */
    @Override
    public UnresolvedPlan visitQuerySpecification(QuerySpecificationContext queryContext) {
        context.push();
        //QuerySpecificationContext(selectClause fromClause? limitClause?)解析为QuerySpecification
        context.peek().collect(queryContext, query);

        //QuerySpecificationContext(selectClause)解析为Project
        Project project = (Project) visit(queryContext.selectClause());

        //没有from tables的情况
        if (queryContext.fromClause() == null) {
            Optional<UnresolvedExpression> allFields = project.getProjectList().stream().filter(node -> node instanceof AllFields).findFirst();
            if (allFields.isPresent()) {
                throw new SyntaxCheckException("No FROM clause found for select all");
            }
            //附加一个Values操作符，其中只包含一个空行，这样Project操作符就有机会求值，尽管求值不依赖于Values中的内容。
            //emptyValue表示为没有from tables
            Values emptyValue = new Values(ImmutableList.of(emptyList()));
            return project.attach(emptyValue);
        }
        //QuerySpecificationContext(fromClause)解析为Project
        UnresolvedPlan from = visit(queryContext.fromClause());

        //QuerySpecificationContext(fromClause)解析为Project
        if (queryContext.limitClause() != null) {
            from = visit(queryContext.limitClause()).attach(from);
        }
        UnresolvedPlan result = project.attach(from);
        context.pop();
        return result;
    }

    /**
     * selectClause : SELECT selectSpec? selectElements
     * selectSpec :  (ALL | DISTINCT)
     * selectElements :  (star=STAR | selectElement) (COMMA selectElement)*
     * selectElement : expression (AS? alias)?
     */
    @Override
    public UnresolvedPlan visitSelectClause(SelectClauseContext ctx) {
        ImmutableList.Builder<UnresolvedExpression> builder =
                new ImmutableList.Builder<>();
        // STAR:'*';
        if (ctx.selectElements().star != null) {
            builder.add(AllFields.of());
        }
        ctx.selectElements().selectElement().forEach(field -> builder.add(visitSelectItem(field)));
        return new Project(builder.build());
    }

    /**
     * selectElement : expression (AS? alias)?
     */
    private UnresolvedExpression visitSelectItem(SelectElementContext ctx) {
        String name = StringUtils.unquoteIdentifier(ParserUtils.getTextInQuery(ctx.expression(), query));
        UnresolvedExpression expr = visitAstExpression(ctx.expression());
        if (ctx.alias() == null) {
            return new Alias(name, expr);
        } else {
            String alias = StringUtils.unquoteIdentifier(ctx.alias().getText());
            return new Alias(name, expr, alias);
        }
    }

    /**
     * 基于Tree获取节点信息
     *
     * @param tree tree
     * @return UnresolvedExpression
     */
    private UnresolvedExpression visitAstExpression(ParseTree tree) {
        return expressionBuilder.visit(tree);
    }

    /**
     * fromClause : FROM relation (whereClause)? (groupByClause)? (havingClause)? (orderByClause)?
     * relation :   tableAsRelation | subqueryAsRelation
     * whereClause :    WHERE expression
     * groupByClause :  GROUP BY groupByElements
     * havingClause :   HAVING expression
     * orderByClause :  ORDER BY orderByElement (COMMA orderByElement)*
     */
    @Override
    public UnresolvedPlan visitFromClause(FromClauseContext ctx) {
        //relation : tableAsRelation | subQueryAsRelation
        UnresolvedPlan result = visit(ctx.relation());
        //whereClause : WHERE expression
        if (ctx.whereClause() != null) {
            result = visit(ctx.whereClause()).attach(result);
        }
        // groupByClause : GROUP BY groupByElements
        AstAggregationBuilder aggBuilder = new AstAggregationBuilder(context.peek());
        UnresolvedPlan aggregation = aggBuilder.visit(ctx.groupByClause());
        if (aggregation != null) {
            result = aggregation.attach(result);
        }

        if (ctx.havingClause() != null) {
            result = visit(ctx.havingClause()).attach(result);
        }

        if (ctx.orderByClause() != null) {
            AstSortBuilder sortBuilder = new AstSortBuilder(context.peek());
            result = sortBuilder.visit(ctx.orderByClause()).attach(result);
        }
        return result;
    }

    /**
     * tableAsRelation : tableName (AS? alias)?
     * tableName : qualifiedName
     * AS : 'AS'
     * alias : ident
     * ident : DOT? ID | BACKTICK_QUOTE_ID | keywordsCanBeId
     * DOT : '.'
     * ID : ID_LITERAL
     * ID_LITERAL : [@*A-Z]+?[*A-Z_\-0-9]*
     * BACKTICK_QUOTE_ID : BQUOTA_STRING
     * BQUOTA_STRING : '`' ( '\\'. | '``' | ~('`'|'\\'))* '`'
     * keywordsCanBeId : (FULL : 'FULL')｜(FIELD : 'FIELD')｜(D : 'D')｜(T : 'T')｜(TS : 'TS') // OD SQL and ODBC special | (COUNT : 'COUNT') | (SUM : 'SUM') | (AVG : 'AVG') | (MAX : 'MAX') | (MIN : 'MIN') | (TIMESTAMP : 'TIMESTAMP') | (DATE : 'DATE')| (TIME : 'TIME') | (DAYOFWEEK : 'DAYOFWEEK') | (FIRST : 'FIRST') | (LAST : 'LAST')
     */
    @Override
    public UnresolvedPlan visitTableAsRelation(TableAsRelationContext ctx) {
        String tableAlias = (ctx.alias() == null) ? null
                : StringUtils.unquoteIdentifier(ctx.alias().getText());
        return new Relation(visitAstExpression(ctx.tableName()), tableAlias);
    }

    /**
     * subqueryAsRelation : LR_BRACKET subquery=querySpecification RR_BRACKET AS? alias
     * LR_BRACKET : '('
     * subquery : querySpecification
     * querySpecification : selectClause fromClause? limitClause?
     * RR_BRACKET : ')'
     * AS : 'AS'
     * alias : ident
     */
    @Override
    public UnresolvedPlan visitSubQueryAsRelation(SubqueryAsRelationContext ctx) {
        return new RelationSubQuery(visit(ctx.subquery), ctx.alias().getText());
    }

    /**
     * whereClause : WHERE expression
     * WHERE : 'WHERE'
     * expression :  notExpression | andExpression  | orExpression  | predicateExpression
     */
    @Override
    public UnresolvedPlan visitWhereClause(WhereClauseContext ctx) {
        return new Filter(visitAstExpression(ctx.expression()));
    }

    /**
     * havingClause : HAVING expression
     * HAVING : 'HAVING'
     * expression :  notExpression | andExpression  | orExpression  | predicateExpression
     */
    @Override
    public UnresolvedPlan visitHavingClause(HavingClauseContext ctx) {
        AstHavingFilterBuilder builder = new AstHavingFilterBuilder(context.peek());
        return new Filter(builder.visit(ctx.expression()));
    }

    @Override
    public UnresolvedPlan visitLimitClause(LimitClauseContext ctx) {
        return null;
    }

    /**
     * 获取VisitChildren
     * T childResult = c.accept(this);
     * result = this.aggregateResult(result, childResult);
     */
    @Override
    protected UnresolvedPlan aggregateResult(UnresolvedPlan aggregate, UnresolvedPlan nextResult) {
        return nextResult != null ? nextResult : aggregate;
    }
}
