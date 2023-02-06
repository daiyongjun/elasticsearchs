package cn.gsdata.elasticsearchs.open.sql.core.data.model;

import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;
import cn.gsdata.elasticsearchs.open.sql.core.storage.bindingtuple.BindingTuple;
import cn.gsdata.elasticsearchs.open.sql.core.storage.bindingtuple.LazyBindingTuple;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Expression Tuple Value
 *
 * @author daiyongjun
 */
@RequiredArgsConstructor
public class ExprTupleValue extends AbstractExprValue {

    private final LinkedHashMap<String, ExprValue> valueMap;

    public static ExprTupleValue fromExprValueMap(Map<String, ExprValue> map) {
        LinkedHashMap<String, ExprValue> linkedHashMap = new LinkedHashMap<>(map);
        return new ExprTupleValue(linkedHashMap);
    }


    @Override
    public int compare(ExprValue other) {
        return 0;
    }

    @Override
    public Object value() {
        return null;
    }

    @Override
    public ExprType type() {
        return null;
    }

    @Override
    public ExprValue keyValue(String key) {
        return valueMap.getOrDefault(key, ExprMissingValue.of());
    }


    @Override
    public BindingTuple bindingTuples() {
        return new LazyBindingTuple(() -> this);
    }

    @Override
    public boolean equal(ExprValue o) {
        if (!(o instanceof ExprTupleValue)) {
            return false;
        } else {
            ExprTupleValue other = (ExprTupleValue) o;
            Iterator<Map.Entry<String, ExprValue>> thisIterator = this.valueMap.entrySet().iterator();
            Iterator<Map.Entry<String, ExprValue>> otherIterator = other.valueMap.entrySet().iterator();
            //遍历valueMap(LinkedHashMap<String, ExprValue>)
            while (thisIterator.hasNext() && otherIterator.hasNext()) {
                Map.Entry<String, ExprValue> thisEntry = thisIterator.next();
                Map.Entry<String, ExprValue> otherEntry = otherIterator.next();
                //比较LinkedHashMap<String, ExprValue>的 key 如果相同则表示相同
                if (!(thisEntry.getKey().equals(otherEntry.getKey())
                        && thisEntry.getValue().equals(otherEntry.getValue()))) {
                    return false;
                }
            }
            // !(false||false)
            return !(thisIterator.hasNext() || otherIterator.hasNext());
        }
    }
}
