package cn.gsdata.elasticsearchs.open.sql.core.ast.expression;

import cn.gsdata.elasticsearchs.open.sql.core.ast.AbstractNodeVisitor;
import cn.gsdata.elasticsearchs.open.sql.core.ast.Node;
import cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.*;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.tuple.Pair;
import lombok.*;

import java.util.List;

/**
 * 窗口函数的表达式节点。参数包括函数 和 分类列表 以及 排序列表
 * 下述为窗口函数的定义： 排名数据，按班级的分数高低分排序，排名数据的表达式为(rank())）按班级分类表达式为(partition by 班级)按分数高低分排序表达式为(order by 成绩 desc)
 * 上述的窗口函数可以定义为 rank() over(partition by 班级 order by 成绩 desc) as ranking
 * top N 问题：找出每个班级成绩排名前 N 的学生
 *
 * @author daiyongjun
 */
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@RequiredArgsConstructor
@ToString
public class WindowFunction extends UnresolvedExpression {
    /**
     * function 表述窗口函数 方法名 排名数据的表达式为(rank())
     */
    private final UnresolvedExpression function;
    /**
     * partitionByList 按班级分类表达式为(partition by 班级)
     */
    private List<UnresolvedExpression> partitionByList;
    /**
     * sortList 按分数高低分排序表达式为(order by 成绩 desc)
     */
    private List<Pair<SortOption, UnresolvedExpression>> sortList;

    @Override
    public List<? extends Node> getChild() {
        ImmutableList.Builder<UnresolvedExpression> children = ImmutableList.builder();
        children.add(function);
        children.addAll(partitionByList);
        sortList.forEach(pair -> children.add(pair.getRight()));
        return children.build();
    }

    @Override
    public <T, C> T accept(AbstractNodeVisitor<T, C> nodeVisitor, C context) {
        return nodeVisitor.visitWindowFunction(this, context);
    }
}
