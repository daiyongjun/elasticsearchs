package cn.gsdata.elasticsearchs.open.sql.core.expression;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValueUtils;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;
import cn.gsdata.elasticsearchs.open.sql.core.expression.function.BuiltinFunctionName;
import cn.gsdata.elasticsearchs.open.sql.core.expression.function.BuiltinFunctionRepository;
import cn.gsdata.elasticsearchs.open.sql.core.expression.function.FunctionResolver;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * Expression 具体实现类
 *
 * @author daiyongjun
 */
@RequiredArgsConstructor
public class DSL {
    /**
     * 内置函数库
     */
    private final BuiltinFunctionRepository repository;

    /**
     * 初始化 文字表达式 Literal  Expression
     *
     * @param value Integer
     * @return LiteralExpression
     */
    public static LiteralExpression literal(Integer value) {
        return new LiteralExpression(ExprValueUtils.integerValue(value));
    }

    /**
     * 初始化 命名表达式 Named Expression
     *
     * @param name       String
     * @param expression Expression
     * @return NamedExpression
     */
    public static NamedExpression named(String name, Expression expression) {
        return new NamedExpression(name, expression);
    }

    /**
     * 初始化 命名表达式 Named Expression
     *
     * @param name       String
     * @param expression Expression
     * @param alias      String
     * @return NamedExpression
     */
    public static NamedExpression named(String name, Expression expression, String alias) {
        return new NamedExpression(name, expression, alias);
    }

    /**
     * 初始化 命名表达式 Named Expression
     *
     * @param expression Expression
     * @return NamedExpression
     */
    public static NamedExpression named(Expression expression) {
        if (expression instanceof NamedExpression) {
            return (NamedExpression) expression;
        }
        return named(expression.toString(), expression);
    }

    /**
     * 初始化引用表达式 Reference Expression
     *
     * @param ref  String
     * @param type ExprType
     * @return ReferenceExpression
     */
    public static ReferenceExpression ref(String ref, ExprType type) {
        return new ReferenceExpression(ref, type);
    }

    /**
     * 初始化BuiltinFunctionName.EQUAL Function Expression
     *
     * @param expressions Expression...
     * @return FunctionExpression
     */
    public FunctionExpression equal(Expression... expressions) {
        return function(BuiltinFunctionName.EQUAL, expressions);
    }

    /**
     * 传入{@link BuiltinFunctionName}  初始化指定的Function Expression
     *
     * @param functionName BuiltinFunctionName
     * @param expressions  Expression...
     * @return FunctionExpression
     */
    private FunctionExpression function(BuiltinFunctionName functionName, Expression... expressions) {
        return (FunctionExpression) repository.compile(
                functionName.getName(), Arrays.asList(expressions));
    }
}
