package cn.gsdata.elasticsearchs.open.sql.core.ast.tree;

import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.Argument;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.UnresolvedExpression;
import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

/**
 * 用于描述搜索的所有字段列表,如es中的include :[] 的列表值
 *
 * @author daiyongjun
 */
@ToString
@Getter
@EqualsAndHashCode(callSuper = false)
public class Project extends UnresolvedPlan {
    /**
     * 自定义字段列表，如表达式、保留字等
     */
    @Setter
    private List<UnresolvedExpression> projectList;
    /**
     * 定义的字段列表
     */
    private List<Argument> argExprList;
    /**
     * 子tree信息
     */
    private UnresolvedPlan child;

    public Project(List<UnresolvedExpression> projectList) {
        this.projectList = projectList;
        this.argExprList = Collections.emptyList();
    }

    public Project(List<UnresolvedExpression> projectList, List<Argument> argExprList) {
        this.projectList = projectList;
        this.argExprList = argExprList;
    }

    /**
     * 判断是否存在定义的字段列表
     *
     * @return boolean
     */
    public boolean hasArgument() {
        return !argExprList.isEmpty();
    }

    @Override
    public Project attach(UnresolvedPlan child) {
        this.child = child;
        return this;
    }

    @Override
    public List<UnresolvedPlan> getChild() {
        return ImmutableList.of(this.child);
    }

}
