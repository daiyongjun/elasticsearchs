package cn.gsdata.elasticsearchs.open.sql.core.expression;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.*;
import cn.gsdata.elasticsearchs.open.sql.core.expression.env.Environment;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValueUtils.*;
import static cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValueUtils.missingValue;
import static cn.gsdata.elasticsearchs.open.sql.core.expression.ReferenceExpressionTest.*;

/**
 * 注解详细解释：
 * configuration 声明当前类交由spring容器管理，相当于配置xml
 * extendWith 在当前类设置一个关注点(joint point)，这个关注点与程序运行内容无关，如Spring单测需要启动一个spring容器, 完成指定bean的构建; 或执行测试数据库前进行连接数据库等工作.
 * contextConfiguration 声明我们在当前测试类需要引入的bean，常见配置有xml,指定类等
 */
@Configuration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ExpressionTestBase.class})
public class ExpressionTestBase {
    protected static Environment<Expression, ExprValue> valueEnv() {
        return var -> {
            if (var instanceof ReferenceExpression) {
                switch (((ReferenceExpression) var).getAttr()) {
                    case "integer_value":
                        return integerValue(1);
                    case "long_value":
                        return longValue(1L);
                    case "float_value":
                        return floatValue(1f);
                    case "double_value":
                        return doubleValue(1d);
                    case "boolean_value":
                        return booleanValue(true);
                    case "string_value":
                        return stringValue("str");
                    case "struct_value":
                        return tupleValue(ImmutableMap.of("str", 1));
                    case "array_value":
                        return collectionValue(ImmutableList.of(1));
                    case BOOL_TYPE_NULL_VALUE_FIELD:
                    case INT_TYPE_NULL_VALUE_FIELD:
                    case DOUBLE_TYPE_NULL_VALUE_FIELD:
                    case STRING_TYPE_NULL_VALUE_FILED:
                        return nullValue();
                    case INT_TYPE_MISSING_VALUE_FIELD:
                    case BOOL_TYPE_MISSING_VALUE_FIELD:
                    case DOUBLE_TYPE_MISSING_VALUE_FIELD:
                    case STRING_TYPE_MISSING_VALUE_FILED:
                        return missingValue();
                    default:
                        throw new IllegalArgumentException("undefined reference");
                }
            } else {
                throw new IllegalArgumentException("var must be ReferenceExpression");
            }
        };
    }


    /**
     * tuple 表达结构如
     * {name:xxx,project.year:xxx,project:xxx,address:xxx,address.local:xxx}
     * {
     * "name": "bob smith"
     * "project.year": 1990,
     * "project": {
     * "year": 2020
     * },
     * "address": {
     * "state": "WA",
     * "city": "seattle"
     * "project.year": 1990
     * },
     * "address.local": {
     * "state": "WA",
     * }
     * }
     *
     * @return ExprTupleValue
     */
    ExprTupleValue tuple() {
        ExprValue address =
                ExprValueUtils.tupleValue(ImmutableMap.of("state", "WA", "city", "seattle", "project"
                        + ".year", 1990));
        ExprValue project =
                ExprValueUtils.tupleValue(ImmutableMap.of("year", 2020));
        ExprValue addressLocal =
                ExprValueUtils.tupleValue(ImmutableMap.of("state", "WA"));
        return ExprTupleValue.fromExprValueMap(ImmutableMap.of(
                "name", new ExprStringValue("bob smith"),
                "project.year", new ExprIntegerValue(1990),
                "project", project,
                "address", address,
                "address.local", addressLocal
        ));
    }
}
