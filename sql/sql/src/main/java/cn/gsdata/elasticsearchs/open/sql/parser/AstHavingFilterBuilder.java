package cn.gsdata.elasticsearchs.open.sql.parser;

import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.UnresolvedExpression;
import cn.gsdata.elasticsearchs.open.sql.parser.context.QuerySpecification;
import gen.OpenDistroSQLParser.*;
import lombok.RequiredArgsConstructor;

/**
 * AST具有构建Having子句条件表达式（AstExpressionBuilder的子类）
 * AST具有构建Having子句条件表达式的筛选器，并将别名替换为SELECT子句中的原始表达式。因为UnresolvedExpression是不可变的，所以很难在之后替换它。
 * havingClause :  HAVING expression
 *
 * @author daiyongjun
 */
@RequiredArgsConstructor
public class AstHavingFilterBuilder extends AstExpressionBuilder {
    private final QuerySpecification querySpec;


    @Override
    public UnresolvedExpression visitQualifiedName(QualifiedNameContext ctx) {
        return replaceAlias(super.visitQualifiedName(ctx));
    }

    private UnresolvedExpression replaceAlias(UnresolvedExpression expr) {
        if (querySpec.isSelectAlias(expr)) {
            return querySpec.getSelectItemByAlias(expr);
        }
        return expr;
    }
}
