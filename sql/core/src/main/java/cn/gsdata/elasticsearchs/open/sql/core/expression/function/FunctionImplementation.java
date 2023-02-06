package cn.gsdata.elasticsearchs.open.sql.core.expression.function;

import cn.gsdata.elasticsearchs.open.sql.core.expression.Expression;

import java.util.List;

/**
 * Expression中的Function接口定义
 *
 * @author daiyongjun
 */
public interface FunctionImplementation {
    /**
     * Function的名称
     *
     * @return FunctionName
     */
    FunctionName getFunctionName();

    /**
     * Function的参数
     *
     * @return List<Expression>
     */
    List<Expression> getArguments();
}
