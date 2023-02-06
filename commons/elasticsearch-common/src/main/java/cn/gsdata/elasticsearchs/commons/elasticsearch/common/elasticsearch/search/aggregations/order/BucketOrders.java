package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.order;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 复合BucketOrder排序类
 *
 * @author daiyongjun
 */
public class BucketOrders extends BucketOrder {
    static final byte ID = -1;
    final List<BucketOrder> orderElements;

    public BucketOrders(List<BucketOrder> bucketOrders) {
        this(bucketOrders, true);
    }

    BucketOrders(List<BucketOrder> compoundOrder, boolean absoluteOrdering) {
        this.orderElements = new LinkedList<>(compoundOrder);
        BucketOrder lastElement = null;
        for (BucketOrder order : orderElements) {
            if (order instanceof BucketOrders) {
                throw new IllegalArgumentException("nested compound order not supported");
            }
            lastElement = order;
        }
        if (absoluteOrdering && !isKeyOrder(lastElement)) {
            // add key order ascending as a tie-breaker to avoid non-deterministic ordering
            // if all user provided comparators return 0.
            this.orderElements.add(InternalOrder.KEY_ASC);
        }
    }

    public static boolean isKeyOrder(BucketOrder order) {
        return isOrder(order, InternalOrder.KEY_ASC) || isOrder(order, InternalOrder.KEY_DESC);
    }

    private static boolean isOrder(BucketOrder order, BucketOrder expected) {
        if (order == expected) {
            return true;
        } else if (order instanceof BucketOrders) {
            // check if its a compound order with the first element that matches
            List<BucketOrder> orders = ((BucketOrders) order).orderElements;
            if (orders.size() >= 1) {
                return isOrder(orders.get(0), expected);
            }
        }
        return false;
    }

    @Override
    byte id() {
        return ID;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        builder.startArray();
        for (BucketOrder order : orderElements) {
            order.toXContent(builder);
        }
        return builder.endArray();
    }
}
