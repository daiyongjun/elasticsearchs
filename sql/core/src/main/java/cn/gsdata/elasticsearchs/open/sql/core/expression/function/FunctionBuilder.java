package cn.gsdata.elasticsearchs.open.sql.core.expression.function;

import cn.gsdata.elasticsearchs.open.sql.core.expression.Expression;

import java.util.List;

/**
 * Function 构建类
 *
 * @author daiyongjun
 */
public interface FunctionBuilder {
    /**
     * Function构建类的核心应用方法
     *
     * @param arguments List<Expression>
     * @return FunctionImplementation
     */
    FunctionImplementation apply(List<Expression> arguments);


}
