package cn.gsdata.elasticsearchs.open.sql.core.expression.function;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValueUtils;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;
import cn.gsdata.elasticsearchs.open.sql.core.expression.Expression;
import cn.gsdata.elasticsearchs.open.sql.core.expression.FunctionExpression;
import cn.gsdata.elasticsearchs.open.sql.core.expression.env.Environment;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Function 定义工具类
 *
 * @author daiyongjun
 */
@UtilityClass
public class FunctionDSL {

    /**
     * 基于functionName,SerializableFunction定义函数解析器functionName{@link FunctionResolver}
     *
     * @param functionName FunctionName 函数名称
     * @param functions    SerializableFunction<FunctionName, Pair<FunctionSignature,
     *                     FunctionBuilder>>... 函数实现列表
     * @return FunctionResolver
     */
    public static FunctionResolver define(FunctionName functionName, SerializableFunction<FunctionName, Pair<FunctionSignature, FunctionBuilder>>... functions) {
        return define(functionName, Arrays.asList(functions));
    }

    /**
     * 基于functionName,SerializableFunction定义函数解析器functionName{@link FunctionResolver} 重载方法
     *
     * @param functionName FunctionName 函数名称
     * @param functions    SerializableFunction<FunctionName, Pair<FunctionSignature,
     *                     FunctionBuilder>>... 函数实现列表
     * @return FunctionResolver
     */
    public static FunctionResolver define(FunctionName functionName, List<SerializableFunction<FunctionName, Pair<FunctionSignature, FunctionBuilder>>> functions) {
        FunctionResolver.FunctionResolverBuilder builder = FunctionResolver.builder();
        builder.functionName(functionName);
        for (Function<FunctionName, Pair<FunctionSignature, FunctionBuilder>> func : functions) {
            Pair<FunctionSignature, FunctionBuilder> functionBuilder = func.apply(functionName);
            builder.functionBundle(functionBuilder.getKey(), functionBuilder.getValue());
        }
        return builder.build();
    }

    /**
     * {@link SerializableFunction}具体实现,无参数函数
     *
     * @param function   SerializableNoArgFunction<ExprValue>  {@link SerializableNoArgFunction}
     * @param returnType ExprType 返回值类型
     * @return SerializableFunction<FunctionName, Pair < FunctionSignature, FunctionBuilder>>
     */
    public static SerializableFunction<FunctionName, Pair<FunctionSignature, FunctionBuilder>> impl(
            SerializableNoArgFunction<ExprValue> function,
            ExprType returnType) {

        return functionName -> {
            FunctionSignature functionSignature =
                    new FunctionSignature(functionName, Collections.emptyList());
            FunctionBuilder functionBuilder = arguments -> new FunctionExpression(functionName, Collections.emptyList()) {
                @Override
                public ExprValue valueOf(Environment<Expression, ExprValue> valueEnv) {
                    return function.get();
                }

                @Override
                public ExprType type() {
                    return returnType;
                }

                @Override
                public String toString() {
                    return String.format("%s()", functionName);
                }
            };
            return Pair.of(functionSignature, functionBuilder);
        };
    }

    /**
     * {@link SerializableFunction}具体实现,一元参数函数
     *
     * @param function   SerializableFunction<ExprValue, ExprValue>  {@link SerializableFunction}
     * @param returnType ExprType 返回值类型
     * @param argsType   ExprType 函数参数类型一
     * @return SerializableFunction<FunctionName, Pair < FunctionSignature, FunctionBuilder>>
     */
    public static SerializableFunction<FunctionName, Pair<FunctionSignature, FunctionBuilder>> impl(
            SerializableFunction<ExprValue, ExprValue> function,
            ExprType returnType,
            ExprType argsType) {
        return functionName -> {
            FunctionSignature functionSignature =
                    new FunctionSignature(functionName, Collections.singletonList(argsType));
            FunctionBuilder functionBuilder =
                    arguments -> new FunctionExpression(functionName, arguments) {
                        @Override
                        public ExprValue valueOf(Environment<Expression, ExprValue> valueEnv) {
                            ExprValue value = arguments.get(0).valueOf(valueEnv);
                            return function.apply(value);
                        }

                        @Override
                        public ExprType type() {
                            return returnType;
                        }

                        @Override
                        public String toString() {
                            return String.format("%s(%s)", functionName,
                                    arguments.stream()
                                            .map(Object::toString)
                                            .collect(Collectors.joining(", ")));
                        }
                    };
            return Pair.of(functionSignature, functionBuilder);
        };
    }


    /**
     * {@link SerializableFunction}具体实现,二元参数函数
     *
     * @param function   SerializableBiFunction<ExprValue, ExprValue, ExprValue>  {@link SerializableBiFunction}
     * @param returnType ExprType 返回值类型
     * @param args1Type  ExprType 函数参数类型一
     * @param args2Type  ExprType 函数参数类型二
     * @return SerializableFunction<FunctionName, Pair < FunctionSignature, FunctionBuilder>>
     */
    public static SerializableFunction<FunctionName, Pair<FunctionSignature, FunctionBuilder>> impl(SerializableBiFunction<ExprValue, ExprValue, ExprValue> function, ExprType returnType, ExprType args1Type, ExprType args2Type) {
        return functionName -> {
            FunctionSignature functionSignature =
                    new FunctionSignature(functionName, Arrays.asList(args1Type, args2Type));
            FunctionBuilder functionBuilder =
                    arguments -> new FunctionExpression(functionName, arguments) {
                        @Override
                        public ExprValue valueOf(Environment<Expression, ExprValue> valueEnv) {
                            ExprValue arg1 = arguments.get(0).valueOf(valueEnv);
                            ExprValue arg2 = arguments.get(1).valueOf(valueEnv);
                            return function.apply(arg1, arg2);
                        }

                        @Override
                        public ExprType type() {
                            return returnType;
                        }

                        @Override
                        public String toString() {
                            return String.format("%s(%s, %s)", functionName, arguments.get(0).toString(),
                                    arguments.get(1).toString());
                        }
                    };
            return Pair.of(functionSignature, functionBuilder);
        };
    }

    /**
     * 针对SerializableBiFunction中的(v1,v2)->function(v1,v2),增加参数v1和v2的逻辑判断
     *
     * @param function SerializableBiFunction<ExprValue, ExprValue, ExprValue> function
     * @return SerializableBiFunction<ExprValue, ExprValue, ExprValue>
     */
    public static SerializableBiFunction<ExprValue, ExprValue, ExprValue> nullMissingHandling(
            SerializableBiFunction<ExprValue, ExprValue, ExprValue> function) {
        return (v1, v2) -> {
            if (v1.isMissing() || v2.isMissing()) {
                return ExprValueUtils.missingValue();
            } else if (v1.isNull() || v2.isNull()) {
                return ExprValueUtils.nullValue();
            } else {
                return function.apply(v1, v2);
            }
        };
    }
}
