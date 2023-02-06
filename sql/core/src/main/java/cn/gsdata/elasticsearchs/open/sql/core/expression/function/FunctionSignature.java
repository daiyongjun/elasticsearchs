package cn.gsdata.elasticsearchs.open.sql.core.expression.function;

import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.WideningTypeRule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 函数签名，函数名称与参数类型组成
 *
 * @author daiyongjun
 */
@Getter
@RequiredArgsConstructor
public class FunctionSignature {
    /**
     * 未匹配
     */
    public static final Integer NOT_MATCH = Integer.MAX_VALUE;
    /**
     * 精确匹配
     */
    public static final Integer EXACTLY_MATCH = 0;

    /**
     * 函数名称
     */
    private final FunctionName functionName;
    /**
     * 函数参数类型列表
     */
    private final List<ExprType> paramTypeList;

    /**
     * 基于函数签名，判断两函数是否匹配
     * 未匹配：NOT_MATCH
     * 精确匹配 ：EXACTLY_MATCH
     *
     * @param functionSignature FunctionSignature
     * @return int
     */
    public int match(FunctionSignature functionSignature) {
        //functionName和paramTypeList.size()不相同则不匹配
        List<ExprType> functionTypeList = functionSignature.getParamTypeList();
        if (!functionName.equals(functionSignature.getFunctionName())
                || paramTypeList.size() != functionTypeList.size()) {
            return NOT_MATCH;
        }
        int matchDegree = EXACTLY_MATCH;
        for (int i = 0; i < paramTypeList.size(); i++) {
            ExprType paramType = paramTypeList.get(i);
            ExprType funcType = functionTypeList.get(i);
            //判断两个类型的关联关系
            int match = WideningTypeRule.distance(paramType, funcType);
            if (match == WideningTypeRule.IMPOSSIBLE_WIDENING) {
                return NOT_MATCH;
            } else {
                matchDegree += match;
            }
        }
        return matchDegree;
    }

    /**
     * 返回当前function的参数类型信息
     *
     * @return String
     */
    public String formatTypes() {
        return getParamTypeList().stream()
                .map(ExprType::typeName)
                .collect(Collectors.joining(",", "[", "]"));
    }
}
