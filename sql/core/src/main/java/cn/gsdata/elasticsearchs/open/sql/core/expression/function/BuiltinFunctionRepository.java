package cn.gsdata.elasticsearchs.open.sql.core.expression.function;

import cn.gsdata.elasticsearchs.open.sql.core.exception.ExpressionEvaluationException;
import cn.gsdata.elasticsearchs.open.sql.core.expression.Expression;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 内置的函数库
 *
 * @author daiyongjun
 */
@RequiredArgsConstructor
public class BuiltinFunctionRepository {
    /**
     * 基于functionName和FunctionResolver的函数库集合
     */
    private final Map<FunctionName, FunctionResolver> functionResolverMap;

    /**
     * 注册{@link FunctionResolver}到内置函数库。
     *
     * @param resolver 函数解析器
     */
    public void register(FunctionResolver resolver) {
        functionResolverMap.put(resolver.getFunctionName(), resolver);
    }

    /**
     * 编译FunctionExpression。{@link FunctionName}，{@link List<Expression>}
     *
     * @param functionName 待编译的FunctionExpression的名称
     * @param expressions  待编译的FunctionExpression的参数类型列表Expression
     * @return FunctionImplementation
     */
    public FunctionImplementation compile(FunctionName functionName, List<Expression> expressions) {
        FunctionBuilder resolvedFunctionBuilder = resolve(new FunctionSignature(functionName,
                expressions.stream().map(expression -> expression.type()).collect(Collectors.toList())));
        return resolvedFunctionBuilder.apply(expressions);
    }

    /**
     * 基于{@link FunctionSignature}解析内置函数库中对应function的builder的{@link FunctionBuilder}。
     *
     * @param functionSignature {@link FunctionSignature}
     * @return {@link FunctionBuilder}
     */
    public FunctionBuilder resolve(FunctionSignature functionSignature) {
        FunctionName functionName = functionSignature.getFunctionName();
        if (functionResolverMap.containsKey(functionName)) {
            return functionResolverMap.get(functionName).resolve(functionSignature);
        } else {
            throw new ExpressionEvaluationException(
                    String.format("unsupported function name: %s", functionName.getFunctionName()));
        }
    }
}