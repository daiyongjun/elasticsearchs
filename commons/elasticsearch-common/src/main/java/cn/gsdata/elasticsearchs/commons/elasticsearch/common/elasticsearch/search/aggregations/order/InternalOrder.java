package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.aggregations.order;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.List;

/**
 * BucketOrder中间实现类，InternalOrder
 *
 * @author daiyongjun
 */
public class InternalOrder extends BucketOrder {
    static final byte COUNT_DESC_ID = 1;
    static final byte COUNT_ASC_ID = 2;
    static final byte KEY_DESC_ID = 3;
    static final byte KEY_ASC_ID = 4;
    /**
     * Order by the (higher) count of each bucket.
     */
    static final InternalOrder COUNT_DESC = new InternalOrder(COUNT_DESC_ID, "_count", false);

    /**
     * Order by the (lower) count of each bucket.
     */
    static final InternalOrder COUNT_ASC = new InternalOrder(COUNT_ASC_ID, "_count", true);

    /**
     * Order by the key of each bucket descending.
     */
    static final InternalOrder KEY_DESC = new InternalOrder(KEY_DESC_ID, "_key", false);

    /**
     * Order by the key of each bucket ascending.
     */
    static final InternalOrder KEY_ASC = new InternalOrder(KEY_ASC_ID, "_key", true);

    private final byte id;
    private final String key;
    protected final boolean asc;

    /**
     * BucketOrder 构造方法，按指定排序属性
     *
     * @param id  Order标识
     * @param key Order
     * @param asc 是否倒排
     */
    public InternalOrder(byte id, String key, boolean asc) {
        this.id = id;
        this.key = key;
        this.asc = asc;
    }

    public static boolean isKeyOrder(BucketOrder order) {
        return isOrder(order, KEY_ASC) || isOrder(order, KEY_DESC);
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
        return id;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder) throws IOException {
        return builder.startObject().field(key, asc ? "asc" : "desc").endObject();
    }
}
