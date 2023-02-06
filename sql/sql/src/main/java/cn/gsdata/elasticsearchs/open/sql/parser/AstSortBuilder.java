package cn.gsdata.elasticsearchs.open.sql.parser;

import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.Argument;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.Field;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.UnresolvedExpression;
import cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort;
import cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.*;
import cn.gsdata.elasticsearchs.open.sql.core.ast.tree.UnresolvedPlan;
import cn.gsdata.elasticsearchs.open.sql.parser.context.QuerySpecification;
import com.google.common.collect.ImmutableList;
import gen.OpenDistroSQLParser.*;
import gen.OpenDistroSQLParserBaseVisitor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static cn.gsdata.elasticsearchs.open.sql.core.ast.dsl.AstDSL.booleanLiteral;
import static cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.NullOrder.NULL_FIRST;
import static cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.SortOrder.DESC;

/**
 * Ast(定义的基础类型)排序构建类，Ast 定义为node,其详细实现有expression和tree(sort|)
 *
 * @author daiyongjun
 */
@RequiredArgsConstructor
public class AstSortBuilder extends OpenDistroSQLParserBaseVisitor<UnresolvedPlan> {
    /*
        orderByClause : ORDER BY orderByElement (COMMA orderByElement)*
     */

    /**
     * orderByClause : ORDER BY orderByElement (COMMA orderByElement)*
     * ORDER : 'ORDER'
     * BY : 'BY'
     * orderByElement : expression order=(ASC | DESC)? (NULLS (FIRST | LAST))?
     * expression :  notExpression | andExpression  | orExpression  | predicateExpression
     * order=(ASC | DESC)? (NULLS (FIRST | LAST))?
     * ASC : 'ASC'
     * DESC : 'DESC'
     * NULLS : 'NULLS'
     * FIRST : 'FIRST'
     * LAST : 'LAST'
     * (COMMA orderByElement)*
     * COMMA : ','
     * orderByElement : expression order=(ASC | DESC)? (NULLS (FIRST | LAST))?
     */
    private final QuerySpecification querySpec;

    @Override
    public UnresolvedPlan visitOrderByClause(OrderByClauseContext ctx) {
        return new Sort(
                createSortFields()
        );
    }

    private List<Field> createSortFields() {
        List<Field> fields = new ArrayList<>();
        List<UnresolvedExpression> items = querySpec.getOrderByItems();
        List<SortOption> options = querySpec.getOrderByOptions();
        for (int i = 0; i < items.size(); i++) {
            fields.add(
                    new Field(
                            querySpec.replaceIfAliasOrOrdinal(items.get(i)),
                            createSortArguments(options.get(i))));
        }
        return fields;
    }

    private List<Argument> createSortArguments(SortOption option) {
        SortOrder sortOrder = option.getSortOrder();
        NullOrder nullOrder = option.getNullOrder();
        ImmutableList.Builder<Argument> args = ImmutableList.builder();
        args.add(new Argument("asc", booleanLiteral(sortOrder != DESC)));

        if (nullOrder != null) {
            args.add(new Argument("nullFirst", booleanLiteral(nullOrder == NULL_FIRST)));
        }
        return args.build();
    }
}

