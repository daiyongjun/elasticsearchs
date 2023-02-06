package cn.gsdata.elasticsearchs.open.sql.core.ast.expression;

import cn.gsdata.elasticsearchs.open.sql.core.expression.function.FunctionName;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;

import java.util.Map;

import static cn.gsdata.elasticsearchs.open.sql.core.expression.function.BuiltinFunctionName.*;

/**
 * Cast函数的表达式节点。参数包括待转换的参数(Literal)以及待转换的类型
 *
 * @author daiyongjun
 */
@AllArgsConstructor
public class Cast extends UnresolvedExpression {
    private static final Map<String, FunctionName> CONVERTED_TYPE_FUNCTION_NAME_MAP =
            new ImmutableMap.Builder<String, FunctionName>()
                    .put("string", CAST_TO_STRING.getName())
                    .put("int", CAST_TO_INT.getName())
                    .put("long", CAST_TO_LONG.getName())
                    .put("float", CAST_TO_FLOAT.getName())
                    .put("double", CAST_TO_DOUBLE.getName())
                    .put("boolean", CAST_TO_BOOLEAN.getName())
                    .put("date", CAST_TO_DATE.getName())
                    .put("time", CAST_TO_TIME.getName())
                    .put("timestamp", CAST_TO_TIMESTAMP.getName())
                    .build();

    /**
     * 待转换的源表达式
     */
    private final UnresolvedExpression expression;

    /**
     * 待转换的表达式的类型
     */
    private final UnresolvedExpression convertedType;

}
