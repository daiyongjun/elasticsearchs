package cn.gsdata.elasticsearchs.open.sql.core.expression;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class NamedExpressionTest extends ExpressionTestBase {
    @Test
    void name_an_expression() {
        LiteralExpression delegated = DSL.literal(10);
        NamedExpression namedExpression = DSL.named("10", delegated);

        assertEquals("10", namedExpression.getNameOrAlias());
        assertEquals(delegated.type(), namedExpression.type());
        assertEquals(delegated.valueOf(valueEnv()), namedExpression.valueOf(valueEnv()));
    }

    @Test
    void name_an_expression_with_alias() {
        LiteralExpression delegated = DSL.literal(10);
        NamedExpression namedExpression = DSL.named("10", delegated, "ten");
        assertEquals("ten", namedExpression.getNameOrAlias());
    }

    @Test
    void name_an_named_expression() {
        LiteralExpression delegated = DSL.literal(10);
        Expression expression = DSL.named("10", delegated, "ten");
        NamedExpression namedExpression = DSL.named(expression);
        assertEquals("ten", namedExpression.getNameOrAlias());
    }
}
