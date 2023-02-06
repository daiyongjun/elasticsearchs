package cn.gsdata.elasticsearchs.open.sql.core.planner.physical;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValueUtils;
import cn.gsdata.elasticsearchs.open.sql.core.expression.DSL;
import cn.gsdata.elasticsearchs.open.sql.core.expression.config.ExpressionConfig;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Iterator;
import java.util.List;


@Configuration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ExpressionConfig.class})
public class PhysicalPlanTestBase {
    @Autowired
    protected DSL dsl;

    protected static final List<ExprValue> inputs = new ImmutableList.Builder<ExprValue>()
            .add(ExprValueUtils.tupleValue(ImmutableMap.of("ip", "209.160.24.63", "action", "GET", "response", 200, "referer",
                    "www.amazon.com")))
            .add(ExprValueUtils.tupleValue(ImmutableMap.of("ip", "209.160.24.63", "action", "GET", "response", 404, "referer",
                    "www.amazon.com")))
            .add(ExprValueUtils.tupleValue(ImmutableMap
                    .of("ip", "112.111.162.4", "action", "GET", "response", 200, "referer",
                            "www.amazon.com")))
            .add(ExprValueUtils.tupleValue(ImmutableMap
                    .of("ip", "74.125.19.106", "action", "POST", "response", 200, "referer",
                            "www.google.com")))
            .add(ExprValueUtils.tupleValue(ImmutableMap.of("ip", "74.125.19.106", "action", "POST", "response", 500)))
            .build();


    protected List<ExprValue> execute(PhysicalPlan plan) {
        ImmutableList.Builder<ExprValue> builder = new ImmutableList.Builder<>();
        plan.open();
        while (plan.hasNext()) {
            builder.add(plan.next());
        }
        plan.close();
        return builder.build();
    }

    /**
     * 用于测试的Scan类
     */
    protected static class TestScan extends PhysicalPlan {
        private final Iterator<ExprValue> iterator;

        public TestScan() {
            iterator = inputs.iterator();
        }

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
            return iterator.hasNext();
        }

        @Override
        public ExprValue next() {
            return iterator.next();
        }
    }
}