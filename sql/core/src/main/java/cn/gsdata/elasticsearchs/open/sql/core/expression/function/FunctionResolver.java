package cn.gsdata.elasticsearchs.open.sql.core.expression.function;

import cn.gsdata.elasticsearchs.open.sql.core.exception.ExpressionEvaluationException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

import java.util.AbstractMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 函数解析器包含重载{@link FunctionBuilder}实现，
 * 由{@link FunctionName}组成，它标识了函数名和{@link FunctionSignature}和{@link FunctionBuilder}的映射来表示重载实现
 *
 * @author daiyongjun
 */
@Builder
@RequiredArgsConstructor
public class FunctionResolver {
    /**
     * {@link FunctionName}
     */
    @Getter
    private final FunctionName functionName;
    /**
     * 函数名和{@link FunctionSignature}和{@link FunctionBuilder}的映射
     */
    @Singular("functionBundle")
    private final Map<FunctionSignature, FunctionBuilder> functionBundle;

    /**
     * 通过使用输入{@link FunctionSignature}解 析{@link FunctionBuilder}，如果{@link FunctionBuilder}与输入{@link FunctionSignature}完全匹配，则返回它。如果应用扩大规则，找到最匹配的，返回它。
     * 如果没有发现，抛出{@link ExpressionEvaluationException}
     *
     * @param unresolvedSignature FunctionSignature
     * @return FunctionBuilder
     */
    public FunctionBuilder resolve(FunctionSignature unresolvedSignature) {
        PriorityQueue<Map.Entry<Integer, FunctionSignature>> functionMatchQueue = new PriorityQueue<>(
                Map.Entry.comparingByKey());
        for (FunctionSignature functionSignature : functionBundle.keySet()) {
            functionMatchQueue.add(
                    new AbstractMap.SimpleEntry<>(unresolvedSignature.match(functionSignature),
                            functionSignature));
        }
        Map.Entry<Integer, FunctionSignature> bestMatchEntry = functionMatchQueue.peek();
        if (FunctionSignature.NOT_MATCH.equals(bestMatchEntry.getKey())) {
            throw new ExpressionEvaluationException(
                    String.format("%s function expected %s, but get %s", functionName,
                            formatFunctions(functionBundle.keySet()),
                            unresolvedSignature.formatTypes()
                    ));
        } else {
            return functionBundle.get(bestMatchEntry.getValue());
        }
    }

    private String formatFunctions(Set<FunctionSignature> functionSignatures) {
        return functionSignatures.stream().map(FunctionSignature::formatTypes)
                .collect(Collectors.joining(",", "{", "}"));
    }
}