package cn.gsdata.elasticsearchs.open.sql.core.storage.bindingtuple;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprTupleValue;
import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import cn.gsdata.elasticsearchs.open.sql.core.expression.ReferenceExpression;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

/**
 * @author daiyongjun
 */
@RequiredArgsConstructor
public class LazyBindingTuple extends BindingTuple {
    /**
     * 
     */
    private final Supplier<ExprTupleValue> lazyBinding;

    @Override
    public ExprValue resolve(ReferenceExpression ref) {
        return ref.resolve(lazyBinding.get());
    }
}
