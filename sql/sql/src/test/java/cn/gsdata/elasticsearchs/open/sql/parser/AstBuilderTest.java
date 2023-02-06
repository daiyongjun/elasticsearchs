package cn.gsdata.elasticsearchs.open.sql.parser;

import cn.gsdata.elasticsearchs.open.sql.antlr.SQLSyntaxParser;
import cn.gsdata.elasticsearchs.open.sql.common.antlr.SyntaxCheckException;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.AllFields;
import cn.gsdata.elasticsearchs.open.sql.core.ast.tree.UnresolvedPlan;
import com.google.common.collect.ImmutableList;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static cn.gsdata.elasticsearchs.open.sql.core.ast.dsl.AstDSL.*;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AstBuilderTest {

    private final SQLSyntaxParser parser = new SQLSyntaxParser();

    @Test
    public void can_build_select_literals() {
        assertEquals(project(
                        values(emptyList()),
                        alias("123", intLiteral(123)),
                        alias("'hello'", stringLiteral("hello")),
                        alias("\"world\"", stringLiteral("world")),
                        alias("false", booleanLiteral(false)),
                        alias("-4.567", doubleLiteral(-4.567))
                ),
                buildAST("select 123, 'hello', \"world\", false, -4.567"));
    }

    @Test
    public void can_build_select_function_call_with_alias() {
        Assertions.assertEquals(
                project(
                        relation("test"),
                        alias(
                                "ABS(age)",
                                function("ABS", qualifiedName("age")),
                                "a"
                        )
                ),
                buildAST("SELECT ABS(age) AS a FROM test")
        );
    }

    @Test
    public void can_build_select_all_from_index() {
        Assertions.assertEquals(
                project(
                        relation("test"),
                        AllFields.of()
                ),
                buildAST("SELECT * FROM test")
        );
        assertThrows(SyntaxCheckException.class, () -> buildAST("SELECT *"));
    }

    @Test
    public void can_build_select_all_and_fields_from_index() {
        Assertions.assertEquals(
                project(
                        relation("test"),
                        AllFields.of(),
                        alias("age", qualifiedName("age")),
                        alias("age", qualifiedName("age"), "a")
                ),
                buildAST("SELECT *, age, age as a FROM test")
        );
    }

    @Test
    public void can_build_select_fields_from_index() {
        Assertions.assertEquals(
                project(
                        relation("test"),
                        alias("age", qualifiedName("age"))
                ),
                buildAST("SELECT age FROM test")
        );
    }

    @Test
    public void can_build_select_fields_with_alias() {
        Assertions.assertEquals(
                project(
                        relation("test"),
                        alias("age", qualifiedName("age"), "a")
                ),
                buildAST("SELECT age AS a FROM test")
        );
    }

    @Test
    public void can_build_select_fields_with_alias_quoted() {
        Assertions.assertEquals(
                project(
                        relation("test"),
                        alias(
                                "(age + 10)",
                                function("+", qualifiedName("age"), intLiteral(10)),
                                "Age_Expr"
                        )
                ),
                buildAST("SELECT"
                        + " (age + 10) AS `Age_Expr` "
                        + "FROM test"
                )
        );
    }

    @Test
    public void can_build_from_index_with_alias() {
        Assertions.assertEquals(
                project(
                        filter(
                                relation("test", "tt"),
                                function("=", qualifiedName("tt", "age"), intLiteral(30))),
                        alias("tt.name", qualifiedName("tt", "name"))
                ),
                buildAST("SELECT tt.name FROM test AS tt WHERE tt.age = 30")
        );
    }

    @Test
    public void can_build_from_index_with_alias_quoted() {
        Assertions.assertEquals(
                project(
                        filter(
                                relation("test", "t"),
                                function("=", qualifiedName("t", "age"), intLiteral(30))),
                        alias("`t`.name", qualifiedName("t", "name"))
                ),
                buildAST("SELECT `t`.name FROM test `t` WHERE `t`.age = 30")
        );
    }

    @Test
    public void can_build_where_clause() {
        Assertions.assertEquals(
                project(
                        filter(
                                relation("test"),
                                function(
                                        "=",
                                        qualifiedName("name"),
                                        stringLiteral("John"))
                        ),
                        alias("name", qualifiedName("name"))
                ),
                buildAST("SELECT name FROM test WHERE name = 'John'")
        );
    }

    @Test
    public void can_build_count_literal() {
        Assertions.assertEquals(
                project(
                        agg(
                                relation("test"),
                                ImmutableList.of(alias("COUNT(1)", aggregate("COUNT", intLiteral(1)))),
                                emptyList(),
                                emptyList(),
                                emptyList()
                        ),
                        alias("COUNT(1)", aggregate("COUNT", intLiteral(1)))),
                buildAST("SELECT COUNT(1) FROM test"));
    }

    @Test
    public void can_build_count_star() {
        Assertions.assertEquals(
                project(
                        agg(
                                relation("test"),
                                ImmutableList.of(
                                        alias("COUNT(*)", aggregate("COUNT", AllFields.of()))),
                                emptyList(),
                                emptyList(),
                                emptyList()),
                        alias("COUNT(*)", aggregate("COUNT", AllFields.of()))),
                buildAST("SELECT COUNT(*) FROM test"));
    }

    @Test
    public void can_build_group_by_field_name() {
        Assertions.assertEquals(
                project(
                        agg(
                                relation("test"),
                                ImmutableList.of(alias("AVG(age)", aggregate("AVG", qualifiedName("age")))),
                                emptyList(),
                                ImmutableList.of(alias("name", qualifiedName("name"))),
                                emptyList()),
                        alias("name", qualifiedName("name")),
                        alias("AVG(age)", aggregate("AVG", qualifiedName("age")))),
                buildAST("SELECT name, AVG(age) FROM test GROUP BY name"));
    }

    @Test
    public void can_build_group_by_function() {
        Assertions.assertEquals(
                project(
                        agg(
                                relation("test"),
                                ImmutableList.of(alias("AVG(age)", aggregate("AVG", qualifiedName("age")))),
                                emptyList(),
                                ImmutableList.of(alias("abs(name)", function("abs", qualifiedName("name")))),
                                emptyList()),
                        alias("abs(name)", function("abs", qualifiedName("name"))),
                        alias("AVG(age)", aggregate("AVG", qualifiedName("age")))),
                buildAST("SELECT abs(name), AVG(age) FROM test GROUP BY abs(name)"));
    }

    @Test
    public void can_build_group_by_uppercase_function() {
        Assertions.assertEquals(
                project(
                        agg(
                                relation("test"),
                                ImmutableList.of(alias("AVG(age)", aggregate("AVG", qualifiedName("age")))),
                                emptyList(),
                                ImmutableList.of(alias("ABS(name)", function("ABS", qualifiedName("name")))),
                                emptyList()),
                        alias("ABS(name)", function("ABS", qualifiedName("name"))),
                        alias("AVG(age)", aggregate("AVG", qualifiedName("age")))),
                buildAST("SELECT ABS(name), AVG(age) FROM test GROUP BY 1"));
    }


    @Test
    public void can_build_group_by_alias() {
        Assertions.assertEquals(
                project(
                        agg(
                                relation("test"),
                                ImmutableList.of(alias("AVG(age)", aggregate("AVG", qualifiedName("age")))),
                                emptyList(),
                                ImmutableList.of(alias("abs(name)", function("abs", qualifiedName("name")))),
                                emptyList()),
                        alias("abs(name)", function("abs", qualifiedName("name")), "n"),
                        alias("AVG(age)", aggregate("AVG", qualifiedName("age")))),
                buildAST("SELECT abs(name) as n, AVG(age) FROM test GROUP BY n"));
    }

    @Test
    public void can_build_group_by_ordinal() {
        Assertions.assertEquals(
                project(
                        agg(
                                relation("test"),
                                ImmutableList.of(alias("AVG(age)", aggregate("AVG", qualifiedName("age")))),
                                emptyList(),
                                ImmutableList.of(alias("abs(name)", function("abs", qualifiedName("name")))),
                                emptyList()),
                        alias("abs(name)", function("abs", qualifiedName("name")), "n"),
                        alias("AVG(age)", aggregate("AVG", qualifiedName("age")))),
                buildAST("SELECT abs(name) as n, AVG(age) FROM test GROUP BY 1"));
    }

    @Test
    public void can_build_implicit_group_by_clause() {
        Assertions.assertEquals(
                project(
                        agg(
                                relation("test"),
                                ImmutableList.of(alias("AVG(age)", aggregate("AVG", qualifiedName("age")))),
                                emptyList(),
                                emptyList(),
                                emptyList()),
                        alias("AVG(age)", aggregate("AVG", qualifiedName("age")))),
                buildAST("SELECT AVG(age) FROM test"));
    }

    @Test
    public void can_build_having_clause() {
        Assertions.assertEquals(
                project(
                        filter(
                                agg(
                                        relation("test"),
                                        ImmutableList.of(
                                                alias("AVG(age)", aggregate("AVG", qualifiedName("age"))),
                                                alias("MIN(balance)", aggregate("MIN", qualifiedName("balance")))),
                                        emptyList(),
                                        ImmutableList.of(alias("name", qualifiedName("name"))),
                                        emptyList()),
                                function(">",
                                        aggregate("MIN", qualifiedName("balance")),
                                        intLiteral(1000))),
                        alias("name", qualifiedName("name")),
                        alias("AVG(age)", aggregate("AVG", qualifiedName("age")))),
                buildAST("SELECT name, AVG(age) FROM test GROUP BY name HAVING MIN(balance) > 1000"));
    }

    @Test
    public void can_build_having_condition_using_alias() {
        Assertions.assertEquals(
                project(
                        filter(
                                agg(
                                        relation("test"),
                                        ImmutableList.of(
                                                alias("AVG(age)", aggregate("AVG", qualifiedName("age")))),
                                        emptyList(),
                                        ImmutableList.of(alias("name", qualifiedName("name"))),
                                        emptyList()),
                                function(">",
                                        aggregate("AVG", qualifiedName("age")),
                                        intLiteral(1000))),
                        alias("name", qualifiedName("name")),
                        alias("AVG(age)", aggregate("AVG", qualifiedName("age")), "a")),
                buildAST("SELECT name, AVG(age) AS a FROM test GROUP BY name HAVING a > 1000"));
    }

    @Test
    public void can_build_order_by_field_name() {
        Assertions.assertEquals(
                project(
                        sort(
                                relation("test"),
                                field("name", argument("asc", booleanLiteral(true)))),
                        alias("name", qualifiedName("name"))),
                buildAST("SELECT name FROM test ORDER BY name"));
    }

    @Test
    public void can_build_order_by_function() {
        Assertions.assertEquals(
                project(
                        sort(
                                relation("test"),
                                field(
                                        function("ABS", qualifiedName("name")),
                                        argument("asc", booleanLiteral(true)))),
                        alias("name", qualifiedName("name"))),
                buildAST("SELECT name FROM test ORDER BY ABS(name)"));
    }

    @Test
    public void can_build_order_by_alias() {
        Assertions.assertEquals(
                project(
                        sort(
                                relation("test"),
                                field("name", argument("asc", booleanLiteral(true)))),
                        alias("name", qualifiedName("name"), "n")),
                buildAST("SELECT name AS n FROM test ORDER BY n ASC"));
    }

    @Test
    public void can_build_order_by_ordinal() {
        Assertions.assertEquals(
                project(
                        sort(
                                relation("test"),
                                field("name", argument("asc", booleanLiteral(false)))),
                        alias("name", qualifiedName("name"))),
                buildAST("SELECT name FROM test ORDER BY 1 DESC"));
    }

    @Test
    public void can_build_order_by_multiple_field_names() {
        Assertions.assertEquals(
                project(
                        sort(
                                relation("test"),
                                field("name", argument("asc", booleanLiteral(true))),
                                field("age", argument("asc", booleanLiteral(false)))),
                        alias("name", qualifiedName("name")),
                        alias("age", qualifiedName("age"))),
                buildAST("SELECT name, age FROM test ORDER BY name, age DESC"));
    }

    @Test
    public void can_build_select_distinct_clause() {
        Assertions.assertEquals(
                project(
                        agg(
                                relation("test"),
                                emptyList(),
                                emptyList(),
                                ImmutableList.of(
                                        alias("name", qualifiedName("name")),
                                        alias("age", qualifiedName("age"))),
                                emptyList()),
                        alias("name", qualifiedName("name")),
                        alias("age", qualifiedName("age"))),
                buildAST("SELECT DISTINCT name, age FROM test"));
    }

    @Test
    public void can_build_select_distinct_clause_with_function() {
        Assertions.assertEquals(
                project(
                        agg(
                                relation("test"),
                                emptyList(),
                                emptyList(),
                                ImmutableList.of(
                                        alias("SUBSTRING(name, 1, 2)",
                                                function(
                                                        "SUBSTRING",
                                                        qualifiedName("name"),
                                                        intLiteral(1), intLiteral(2)))),
                                emptyList()),
                        alias("SUBSTRING(name, 1, 2)",
                                function(
                                        "SUBSTRING",
                                        qualifiedName("name"),
                                        intLiteral(1), intLiteral(2)))),
                buildAST("SELECT DISTINCT SUBSTRING(name, 1, 2) FROM test"));
    }

    @Test
    public void can_build_select_all_clause() {
        Assertions.assertEquals(
                buildAST("SELECT name, age FROM test"),
                buildAST("SELECT ALL name, age FROM test")
        );
    }

    @Test
    public void can_build_order_by_null_option() {
        Assertions.assertEquals(
                project(
                        sort(
                                relation("test"),
                                field("name",
                                        argument("asc", booleanLiteral(true)),
                                        argument("nullFirst", booleanLiteral(false)))),
                        alias("name", qualifiedName("name"))),
                buildAST("SELECT name FROM test ORDER BY name NULLS LAST"));
    }

    @Test
    public void can_build_order_by_sort_order_keyword_insensitive() {
        Assertions.assertEquals(
                project(
                        sort(
                                relation("test"),
                                field("age",
                                        argument("asc", booleanLiteral(true)))),
                        alias("age", qualifiedName("age"))),
                buildAST("SELECT age FROM test ORDER BY age ASC")
        );

        Assertions.assertEquals(
                project(
                        sort(
                                relation("test"),
                                field("age",
                                        argument("asc", booleanLiteral(true)))),
                        alias("age", qualifiedName("age"))),
                buildAST("SELECT age FROM test ORDER BY age asc")
        );
    }

    @Test
    public void can_build_from_subQuery() {
        Assertions.assertEquals(
                project(
                        filter(
                                relationSubQuery(
                                        project(
                                                relation("test"),
                                                alias("firstname", qualifiedName("firstname"), "firstName"),
                                                alias("lastname", qualifiedName("lastname"), "lastName")
                                        ),
                                        "a"
                                ),
                                function(">", qualifiedName("age"), intLiteral(20))
                        ),
                        alias("a.firstName", qualifiedName("a", "firstName")),
                        alias("lastName", qualifiedName("lastName"))),
                buildAST(
                        "SELECT a.firstName, lastName FROM ("
                                + "SELECT firstname AS firstName, lastname AS lastName FROM test"
                                + ") AS a where age > 20"
                )
        );
    }

    /**
     * root :  sqlStatement? SEMI? EOF
     * sqlStatement :  dmlStatement | adminStatement
     * dmlStatement : selectStatement
     * selectStatement : querySpecification
     */
    private UnresolvedPlan buildAST(String query) {
        ParseTree parseTree = parser.parse(query);
        //Builder为Visitor
        return parseTree.accept(new AstBuilder(query));
    }
}
