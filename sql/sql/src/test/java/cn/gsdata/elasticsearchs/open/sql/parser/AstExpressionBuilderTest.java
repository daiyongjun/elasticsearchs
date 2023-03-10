package cn.gsdata.elasticsearchs.open.sql.parser;

import cn.gsdata.elasticsearchs.open.sql.common.antlr.CaseInsensitiveCharStream;
import cn.gsdata.elasticsearchs.open.sql.core.ast.Node;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.DataType;
import cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.*;
import com.google.common.collect.ImmutableList;
import gen.OpenDistroSQLLexer;
import gen.OpenDistroSQLParser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static cn.gsdata.elasticsearchs.open.sql.core.ast.dsl.AstDSL.*;
import static cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.NullOrder.NULL_LAST;
import static cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.SortOrder.ASC;
import static cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.SortOrder.DESC;
import static org.junit.Assert.assertEquals;

public class AstExpressionBuilderTest {
    private final AstExpressionBuilder astExprBuilder = new AstExpressionBuilder();

    @Test
    public void canBuildStringLiteral() {
        assertEquals(stringLiteral("hello"), buildExprAst("'hello'"));
        assertEquals(stringLiteral("hello"), buildExprAst("\"hello\""));
    }

    @Test
    public void canBuildIntegerLiteral() {
        assertEquals(
                intLiteral(123),
                buildExprAst("123")
        );
    }

    @Test
    public void canBuildNegativeRealLiteral() {
        assertEquals(
                doubleLiteral(-4.567),
                buildExprAst("-4.567")
        );
    }

    @Test
    public void canBuildBooleanLiteral() {
        assertEquals(
                booleanLiteral(true),
                buildExprAst("true")
        );
    }

    @Test
    public void canBuildDateLiteral() {
        assertEquals(
                dateLiteral("2020-07-07"),
                buildExprAst("DATE '2020-07-07'")
        );
    }

    @Test
    public void canBuildTimeLiteral() {
        assertEquals(
                timeLiteral("11:30:45"),
                buildExprAst("TIME '11:30:45'")
        );
    }

    @Test
    public void canBuildTimestampLiteral() {
        assertEquals(
                timestampLiteral("2020-07-07 11:30:45"),
                buildExprAst("TIMESTAMP '2020-07-07 11:30:45'")
        );
    }

    @Test
    public void canBuildIntervalLiteral() {
        assertEquals(
                intervalLiteral(1, DataType.INTEGER, "day"),
                buildExprAst("interval 1 day")
        );
    }

    @Test
    public void canBuildArithmeticExpression() {
        assertEquals(
                function("+", intLiteral(1), intLiteral(2)),
                buildExprAst("1 + 2")
        );
    }

    @Test
    public void canBuildFunctionWithoutArguments() {
        assertEquals(
                function("PI"),
                buildExprAst("PI()")
        );
    }

    @Test
    public void canBuildExpressionWithParentheses() {
        assertEquals(
                function("*",
                        function("+", doubleLiteral(-1.0), doubleLiteral(2.3)),
                        function("-", intLiteral(3), intLiteral(1))
                ),
                buildExprAst("(-1.0 + 2.3) * (3 - 1)")
        );
    }

    @Test
    public void canBuildFunctionCall() {
        assertEquals(
                function("abs", intLiteral(-1)),
                buildExprAst("abs(-1)")
        );
    }

    @Test
    public void canBuildNestedFunctionCall() {
        assertEquals(
                function("abs",
                        function("*",
                                function("abs", intLiteral(-5)),
                                intLiteral(-1)
                        )
                ),
                buildExprAst("abs(abs(-5) * -1)")
        );
    }

    @Test
    public void canBuildDateAndTimeFunctionCall() {
        assertEquals(
                function("dayofmonth", dateLiteral("2020-07-07")),
                buildExprAst("dayofmonth(DATE '2020-07-07')")
        );
    }

    @Test
    public void canBuildComparisonExpression() {
        assertEquals(
                function("!=", intLiteral(1), intLiteral(2)),
                buildExprAst("1 != 2")
        );
    }

    @Test
    public void canBuildNullTestExpression() {
        assertEquals(
                function("is null", intLiteral(1)),
                buildExprAst("1 is NULL")
        );

        assertEquals(
                function("is not null", intLiteral(1)),
                buildExprAst("1 IS NOT null")
        );
    }

    @Test
    public void canBuildNullTestExpressionWithNULLLiteral() {
        Assertions.assertEquals(
                function("is null", nullLiteral()),
                buildExprAst("NULL is NULL")
        );

        Assertions.assertEquals(
                function("is not null", nullLiteral()),
                buildExprAst("NULL IS NOT null")
        );
    }

    @Test
    public void canBuildLikeExpression() {
        assertEquals(
                function("like", stringLiteral("str"), stringLiteral("st%")),
                buildExprAst("'str' like 'st%'")
        );

        assertEquals(
                function("not like", stringLiteral("str"), stringLiteral("st%")),
                buildExprAst("'str' not like 'st%'")
        );
    }

    @Test
    public void canBuildRegexpExpression() {
        assertEquals(
                function("regexp", stringLiteral("str"), stringLiteral(".*")),
                buildExprAst("'str' regexp '.*'")
        );
    }

    @Test
    public void canBuildLogicalExpression() {
        assertEquals(
                and(booleanLiteral(true), booleanLiteral(false)),
                buildExprAst("true AND false")
        );

        assertEquals(
                or(booleanLiteral(true), booleanLiteral(false)),
                buildExprAst("true OR false")
        );

        assertEquals(
                not(booleanLiteral(false)),
                buildExprAst("NOT false")
        );
    }

    @Test
    public void canBuildWindowFunction() {
        Assertions.assertEquals(
                window(
                        function("RANK"),
                        ImmutableList.of(qualifiedName("state")),
                        ImmutableList.of(ImmutablePair.of(new SortOption(null, null), qualifiedName("age")))),
                buildExprAst("RANK() OVER (PARTITION BY state ORDER BY age)"));
    }

    @Test
    public void canBuildWindowFunctionWithNullOrderSpecified() {
        Assertions.assertEquals(
                window(
                        function("DENSE_RANK"),
                        ImmutableList.of(),
                        ImmutableList.of(ImmutablePair.of(
                                new SortOption(ASC, NULL_LAST), qualifiedName("age")))),
                buildExprAst("DENSE_RANK() OVER (ORDER BY age ASC NULLS LAST)"));
    }

    @Test
    public void canBuildWindowFunctionWithoutOrderBy() {
        Assertions.assertEquals(
                window(
                        function("RANK"),
                        ImmutableList.of(qualifiedName("state")),
                        ImmutableList.of()),
                buildExprAst("RANK() OVER (PARTITION BY state)"));
    }

    @Test
    public void canBuildWindowFunctionWithoutPartitionBy() {
        Assertions.assertEquals(
                window(
                        function("DENSE_RANK"),
                        ImmutableList.of(),
                        ImmutableList.of(ImmutablePair.of(new SortOption(DESC, null), qualifiedName("age")))),
                buildExprAst("DENSE_RANK() OVER (ORDER BY age DESC)"));
    }

    @Test
    public void canBuildAggregateWindowFunction() {
        Assertions.assertEquals(
                window(
                        aggregate("AVG", qualifiedName("age")),
                        ImmutableList.of(qualifiedName("state")),
                        ImmutableList.of(ImmutablePair.of(
                                new SortOption(null, null), qualifiedName("age")))),
                buildExprAst("AVG(age) OVER (PARTITION BY state ORDER BY age)"));
    }

    @Test
    public void canBuildCaseConditionStatement() {
        Assertions.assertEquals(
                caseWhen(
                        qualifiedName("age"),
                        stringLiteral("age2"),
                        when(intLiteral(30), stringLiteral("age1"))),
                buildExprAst("CASE age WHEN 30 THEN 'age1' ELSE 'age2' END")
        );
    }

    @Test
    public void canBuildKeywordsAsIdentifiers() {
        Assertions.assertEquals(
                qualifiedName("timestamp"),
                buildExprAst("timestamp")
        );
    }

    @Test
    public void canBuildKeywordsAsIdentInQualifiedName() {
        Assertions.assertEquals(
                qualifiedName("test", "timestamp"),
                buildExprAst("test.timestamp")
        );
    }

    @Test
    public void canCastFieldAsString() {
        Assertions.assertEquals(
                cast(qualifiedName("state"), stringLiteral("string")),
                buildExprAst("cast(state as string)")
        );
    }

    @Test
    public void canCastValueAsString() {
        Assertions.assertEquals(
                cast(intLiteral(1), stringLiteral("string")),
                buildExprAst("cast(1 as string)")
        );
    }

    @Test
    public void filteredAggregation() {
        Assertions.assertEquals(
                aggregate("avg", qualifiedName("age")),
                buildExprAst("avg(age)")
        );
    }

    private Node buildExprAst(String expr) {
        OpenDistroSQLLexer lexer = new OpenDistroSQLLexer(new CaseInsensitiveCharStream(expr));
        OpenDistroSQLParser parser = new OpenDistroSQLParser(new CommonTokenStream(lexer));
        return parser.expression().accept(astExprBuilder);
    }
}
