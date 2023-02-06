package cn.gsdata.elasticsearchs.open.sql.core.data.model;

import cn.gsdata.elasticsearchs.open.sql.core.exception.ExpressionEvaluationException;

/**
 * Expression的Value的接口的抽象实现,抽象实现重新定义compareTo以及equals方法实现
 *
 * @author daiyongjun
 */
public abstract class AbstractExprValue implements ExprValue {

    /**
     * ExprValue extends Comparable 在抽象类中实现了compareTo方法实现的compareTo增加了isNUll、isMissing的判断
     * 以及比较只局限于数字和相同类型之间，设置一个compare的钩子方法，具体实现以实现类
     *
     * @param other the object to be compared.
     * @return int
     */
    @Override
    public int compareTo(ExprValue other) {
        if (this.isNull() || this.isMissing() || other.isNull() || other.isMissing()) {
            throw new IllegalStateException(
                    "[BUG] Unreachable, Comparing with NULL or MISSING is undefined");
        }
        boolean condition = (this.isNumber() && other.isNumber()) || this.type() == other.type();
        if (condition) {
            return compare(other);
        } else {
            throw new ExpressionEvaluationException(
                    String.format(
                            "compare expected value have same type, but with [%s, %s]",
                            this.type(), other.type()));
        }
    }

    /**
     * 重写了equals方法
     * this is null is missing 或者 其他情况  equal(other);
     * other is null is missing other.equals(this);
     * equals()方法为抽象方法，具体实现由实现类决定
     * NULL MISSING 如下: A       B       A == B ; NULL    NULL    TRUE ; NULL    MISSING FALSE ; MISSING NULL    FALSE ; MISSING MISSING TRUE
     *
     * @param o Object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ExprValue)) {
            return false;
        }
        ExprValue other = (ExprValue) o;
        if (this.isNull() || this.isMissing()) {
            return equal(other);
        } else if (other.isNull() || other.isMissing()) {
            return other.equals(this);
        } else {
            return equal(other);
        }
    }

    /**
     * 抽象的compare方法，按具体实现类去实现
     *
     * @param other ExprValue
     * @return int
     */
    public abstract int compare(ExprValue other);

    /**
     * 抽象的compare方法，按具体实现类去实现
     *
     * @param other ExprValue
     * @return boolean
     */
    public abstract boolean equal(ExprValue other);
}
