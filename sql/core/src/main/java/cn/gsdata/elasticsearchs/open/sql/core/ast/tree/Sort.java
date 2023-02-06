package cn.gsdata.elasticsearchs.open.sql.core.ast.tree;

import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.Field;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.List;

import static cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.NullOrder.NULL_FIRST;
import static cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.NullOrder.NULL_LAST;
import static cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.SortOrder.ASC;
import static cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.SortOrder.DESC;

/**
 * Sort节点表示排序表达式和排序选项的列表。
 *
 * @author daiyongjun
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Sort extends UnresolvedPlan {
    /**
     * 子tree信息
     */
    private UnresolvedPlan child;
    /**
     * 排序选项的列表
     */
    private final List<Field> sortList;

    @Override
    public Sort attach(UnresolvedPlan child) {
        this.child = child;
        return this;
    }

    @Override
    public List<UnresolvedPlan> getChild() {
        return ImmutableList.of(this.child);
    }

    /**
     * Sort 选项值，DEFAULT_ASC、DEFAULT_DESC
     */
    @Data
    public static class SortOption {
        /**
         * 默认情况升序，ASC(0) 升序、NULL_FIRST (0)强制null放在最前
         */
        public static SortOption DEFAULT_ASC = new SortOption(ASC, NULL_FIRST);
        /**
         * 默认情况降序，DESC(1) 降序、NULL_LAST(1) 强制null放在最后
         */
        public static SortOption DEFAULT_DESC = new SortOption(DESC, NULL_LAST);

        private final SortOrder sortOrder;

        private final NullOrder nullOrder;
    }

    public enum SortOrder {
        /**
         * 定义排序规则 ASC(0) 升序 DESC(1) 降序
         */
        ASC,
        DESC
    }

    public enum NullOrder {
        /**
         * NULL_FIRST (0)强制null放在最前，NULL_LAST(1) 强制null放在最后
         */
        NULL_FIRST,
        NULL_LAST
    }
}
