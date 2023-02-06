package cn.gsdata.elasticsearchs.open.sql.core.expression.function;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * @author daiyongjun
 */
@EqualsAndHashCode
@RequiredArgsConstructor
public class FunctionName implements Serializable {
    @Getter
    private final String functionName;

    /**
     * of方法是jdk1.9对集合添加元素的优化，of提供一个静态方法方便创建当前对象
     *
     * @param functionName 方法名称
     * @return FunctionName
     */
    public static FunctionName of(String functionName) {
        return new FunctionName(functionName.toLowerCase());
    }

    @Override
    public String toString() {
        return functionName;
    }
}
