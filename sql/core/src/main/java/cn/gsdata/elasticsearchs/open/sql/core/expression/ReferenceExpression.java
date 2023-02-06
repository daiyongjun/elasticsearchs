package cn.gsdata.elasticsearchs.open.sql.core.expression;

import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprTupleValue;
import cn.gsdata.elasticsearchs.open.sql.core.data.model.ExprValue;
import cn.gsdata.elasticsearchs.open.sql.core.data.type.ExprType;
import cn.gsdata.elasticsearchs.open.sql.core.expression.env.Environment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static cn.gsdata.elasticsearchs.open.sql.core.utils.ExpressionUtils.PATH_SEP;

/**
 * 引用表达式，基于path从结果集合中获取value值
 *
 * @author daiyongjun
 */
@RequiredArgsConstructor
public class ReferenceExpression implements Expression {
    /**
     * 引用表达式的属性·
     */
    @Getter
    private final String attr;

    /**
     * 属性对应的路径
     */
    @Getter
    private final List<String> paths;

    /**
     * 属性类型
     */
    private final ExprType type;

    public ReferenceExpression(String ref, ExprType type) {
        this.attr = ref;
        this.paths = Arrays.asList(ref.split("\\."));
        this.type = type;
    }

    @Override
    public ExprValue valueOf(Environment<Expression, ExprValue> env) {
        return env.resolve(this);
    }

    /**
     * 基于引用(paths)从结果集合(Expression)获取对应定义引用的value值
     *
     * @param value ExprTupleValue
     * @return ExprValue
     */
    public ExprValue resolve(ExprTupleValue value) {
        return resolve(value, paths);
    }

    /**
     * 基于引用(paths)从结果集合(Expression)获取对应定义引用的value值
     *
     * @param value ExprValue   ExprTupleValue等结果集
     * @param paths List<String>   paths[0] 根节点 paths[1,length]子节点
     * @return ExprValue
     */
    private ExprValue resolve(ExprValue value, List<String> paths) {
        final ExprValue wholePathValue = value.keyValue(String.join(PATH_SEP, paths));
        if (!wholePathValue.isMissing() || paths.size() == 1) {
            return wholePathValue;
        } else {
            //获取根节点信息并返回节点路径
            return resolve(value.keyValue(paths.get(0)), paths.subList(1, paths.size()));
        }
    }

    @Override
    public ExprType type() {
        return type;
    }

    @Override
    public <T, C> T accept(ExpressionNodeVisitor<T, C> visitor, C context) {
        return null;
    }
}
