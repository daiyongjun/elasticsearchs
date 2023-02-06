package cn.gsdata.elasticsearchs.open.sql.core.data.model;

import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprCoreType;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Expression Collection Value
 *
 * @author daiyongjun
 */
@RequiredArgsConstructor
public class ExprCollectionValue extends AbstractExprValue {
    private final List<ExprValue> valueList;

    @Override
    public int compare(ExprValue other) {
        return Integer.compare(valueList.size(), other.collectionValue().size());
    }

    @Override
    public boolean equal(ExprValue other) {
        if (!(other instanceof ExprCollectionValue)) {
            return false;
        } else {
            ExprCollectionValue collectionOther = (ExprCollectionValue) other;
            Iterator<ExprValue> thisIterator = this.valueList.iterator();
            Iterator<ExprValue> otherIterator = collectionOther.valueList.iterator();

            while (thisIterator.hasNext() && otherIterator.hasNext()) {
                ExprValue thisEntry = thisIterator.next();
                ExprValue otherEntry = otherIterator.next();
                if (!thisEntry.equals(otherEntry)) {
                    return false;
                }
            }
            return !(thisIterator.hasNext() || otherIterator.hasNext());
        }
    }

    @Override
    public Object value() {
        List<Object> results = new ArrayList<>();
        for (ExprValue exprValue : valueList) {
            results.add(exprValue.value());
        }
        return results;
    }

    @Override
    public ExprType type() {
        return ExprCoreType.ARRAY;
    }
}
