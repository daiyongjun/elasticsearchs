package cn.gsdata.elasticsearchs.open.sql.core.expression.aggregation;

import cn.gsdata.elasticsearchs.open.sql.core.expression.Expression;
import cn.gsdata.elasticsearchs.open.sql.core.expression.function.FunctionImplementation;

/**
 *
 * @author daiyongjun
 */
public abstract class Aggregator<S extends AggregationState>
        implements FunctionImplementation, Expression {

}

