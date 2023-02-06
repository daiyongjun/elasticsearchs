package cn.gsdata.elasticsearchs.open.sql.core.expression.config;

import cn.gsdata.elasticsearchs.open.sql.core.expression.DSL;
import cn.gsdata.elasticsearchs.open.sql.core.expression.function.BuiltinFunctionRepository;
import cn.gsdata.elasticsearchs.open.sql.core.expression.operator.arthmetic.ArithmeticFunction;
import cn.gsdata.elasticsearchs.open.sql.core.expression.operator.predicate.BinaryPredicateOperator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * Expression 配置到Spring IoC。
 *
 * @author daiyongjun
 */
@Configuration
public class ExpressionConfig {
    /**
     * BuiltinFunctionRepository实例化
     *
     * @return BuiltinFunctionRepository
     */
    @Bean
    public BuiltinFunctionRepository functionRepository() {
        BuiltinFunctionRepository builtinFunctionRepository =
                new BuiltinFunctionRepository(new HashMap<>());
        BinaryPredicateOperator.register(builtinFunctionRepository);
        return builtinFunctionRepository;
    }

    /**
     * DSL实例化
     *
     * @param repository BuiltinFunctionRepository
     * @return DSL
     */
    @Bean
    public DSL dsl(BuiltinFunctionRepository repository) {
        return new DSL(repository);
    }
}
