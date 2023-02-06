package cn.gsdata.elasticsearchs.open.sql.core.ast.expression;

import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

/**
 * 定义字段类型
 *
 * @author daiyongjun
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Field extends UnresolvedExpression {
    private final UnresolvedExpression field;

    private final List<Argument> fieldArgs;

    /**
     * Constructor of Field.
     */
    public Field(UnresolvedExpression field) {
        this(field, Collections.emptyList());
    }

    /**
     * Constructor of Field.
     */
    public Field(UnresolvedExpression field, List<Argument> fieldArgs) {
        this.field = field;
        this.fieldArgs = fieldArgs;
    }

    @Override
    public List<UnresolvedExpression> getChild() {
        return ImmutableList.of(this.field);
    }
}
