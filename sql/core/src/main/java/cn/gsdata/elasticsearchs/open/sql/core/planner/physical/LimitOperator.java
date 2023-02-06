package cn.gsdata.elasticsearchs.open.sql.core.planner.physical;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Limit Operator 的PhysicalPlan
 *
 * @author daiyongjun
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class LimitOperator extends PhysicalPlan {
    /**
     * 输出结果集
     */
    private final PhysicalPlan input;
    /**
     * 限制条数
     */
    private final Integer limit;
    /**
     * 偏移量
     */
    private final Integer offset;
    /**
     * 当前数据读取到的下标，比如input是数组array[12]这里读取到array[2],2就是数组的下标
     */
    private Integer count = 0;

    @Override
    public List<PhysicalPlan> getChild() {
        return ImmutableList.of();
    }

    @Override
    public <R, C> R accept(PhysicalPlanNodeVisitor<R, C> visitor, C context) {
        return null;
    }

    @Override
    public boolean hasNext() {
        return input.hasNext() && count < offset + limit;
    }

    @Override
    public ExprValue next() {
        count++;
        return input.next();
    }

    @Override
    public void open() {
        super.open();
        // 将count下标移动到offset的位置
        while (input.hasNext() && count < offset) {
            count++;
            input.next();
        }
    }
}
