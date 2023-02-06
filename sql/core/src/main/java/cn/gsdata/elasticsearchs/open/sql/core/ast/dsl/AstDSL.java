package cn.gsdata.elasticsearchs.open.sql.core.ast.dsl;

import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.*;
import cn.gsdata.elasticsearchs.open.sql.core.ast.tree.*;
import cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.*;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

/**
 * 初始化特定节点实例的静态方法类。
 *
 * @author daiyongjun
 */
@UtilityClass
public class AstDSL {

    public static Argument argument(String argName, Literal argValue) {
        return new Argument(argName, argValue);
    }

    /**
     * 初始化Field节点 基于UnresolvedExpression field
     *
     * @param field UnresolvedExpression    field
     * @return Field  Field节点   field,fieldArgs = Collections.emptyList()
     */
    public Field field(UnresolvedExpression field) {
        return new Field(field);
    }

    /**
     * 初始化Field节点 基于UnresolvedExpression field
     *
     * @param field     UnresolvedExpression
     * @param fieldArgs Argument...
     * @return Field  Field节点   field,fieldArgs = Arrays.asList(fieldArgs)
     */
    public Field field(UnresolvedExpression field, Argument... fieldArgs) {
        return field(field, Arrays.asList(fieldArgs));
    }


    /**
     * 初始化Field节点 基于 field(String)   fieldArgs(Argument...)
     *
     * @param field     String
     * @param fieldArgs Argument...
     * @return Field    Field节点
     */
    public Field field(String field, Argument... fieldArgs) {
        return field(qualifiedName(field), Arrays.asList(fieldArgs));
    }

    /**
     * 初始化Field节点   基于field(UnresolvedExpression)  fieldArgs(List<Argument>)
     *
     * @param field     UnresolvedExpression
     * @param fieldArgs List<Argument>
     * @return Field    Field节点
     */
    public Field field(UnresolvedExpression field, List<Argument> fieldArgs) {
        return new Field(field, fieldArgs);
    }

    /**
     * 初始化window节点 基于 窗口函数(function) 分类表达式(partitionByList)  排序表达式(sortList)
     *
     * @param function        UnresolvedExpression    窗口函数(function)
     * @param partitionByList List<UnresolvedExpression>  分类表达式(partitionByList)
     * @param sortList        List<Pair<SortOption, UnresolvedExpression>>   排序表达式
     * @return UnresolvedExpression
     */
    public UnresolvedExpression window(UnresolvedExpression function,
                                       List<UnresolvedExpression> partitionByList,
                                       List<Pair<SortOption, UnresolvedExpression>> sortList) {
        return new WindowFunction(function, partitionByList, sortList);
    }

    /**
     * 初始化Filter节点实例
     *
     * @param input      UnresolvedPlan
     * @param expression UnresolvedExpression
     * @return UnresolvedPlan
     */
    public static UnresolvedPlan filter(UnresolvedPlan input, UnresolvedExpression expression) {
        return new Filter(expression).attach(input);
    }

    /**
     * 初始化Sort节点实例
     *
     * @param input UnresolvedPlan
     * @param sorts Field...
     * @return Sort
     */
    public static Sort sort(UnresolvedPlan input, Field... sorts) {
        return new Sort(input, Arrays.asList(sorts));
    }

    /**
     * 初始化Relation节点实例
     *
     * @param tableName 表名
     * @return UnresolvedPlan
     */
    public UnresolvedPlan relation(String tableName) {
        return new Relation(qualifiedName(tableName));
    }

    /**
     * 初始化Relation节点实例
     *
     * @param tableName 表名
     * @param alias     别名
     * @return UnresolvedPlan
     */
    public UnresolvedPlan relation(String tableName, String alias) {
        return new Relation(qualifiedName(tableName), alias);
    }

    /**
     * 初始化文字行节点
     *
     * @param values 每行都是文字值列表的行
     * @return Values node
     */
    @SafeVarargs
    public Values values(List<Literal>... values) {
        return new Values(Arrays.asList(values));
    }

    /**
     * 初始化Alias节点实例
     *
     * @param name 原始字段名称
     * @param expr 表达式别名
     * @return Alias node
     */
    public Alias alias(String name, UnresolvedExpression expr) {
        return new Alias(name, expr);
    }

    /**
     * 初始化Alias节点实例
     *
     * @param name  原始字段名称
     * @param expr  表达式别名
     * @param alias 字段别名
     * @return Alias
     */
    public Alias alias(String name, UnresolvedExpression expr, String alias) {
        return new Alias(name, expr, alias);
    }

    /**
     * 初始化Aggregation节点实例
     *
     * @param input     relation
     * @param aggList   aggregateFunction : AVG | COUNT | SUM | MIN | MAX | COUNT LR_BRACKET STAR RR_BRACKET)
     * @param sortList  orderByClause : ORDER BY orderByElement (COMMA orderByElement)*
     * @param groupList groupByClause :  GROUP BY groupByElements
     * @param argList   List<Argument>
     * @return UnresolvedPlan
     */
    public static UnresolvedPlan agg(
            UnresolvedPlan input,
            List<UnresolvedExpression> aggList,
            List<UnresolvedExpression> sortList,
            List<UnresolvedExpression> groupList,
            List<Argument> argList) {
        return new Aggregation(aggList, sortList, groupList, argList).attach(input);
    }

    /**
     * 初始化Project节点实例
     *
     * @param input       UnresolvedPlan
     * @param projectList UnresolvedExpression...
     * @return Project
     */
    public static Project project(UnresolvedPlan input, UnresolvedExpression... projectList) {
        return new Project(Arrays.asList(projectList)).attach(input);
    }

    /**
     * 初始化relationSubQuery定义子关系节点（关系节点为搜索源）
     *
     * @param subQuery      UnresolvedPlan  子关系节点
     * @param subQueryAlias String  子关系节点的别名
     * @return UnresolvedPlan
     */
    public static UnresolvedPlan relationSubQuery(UnresolvedPlan subQuery, String subQueryAlias) {
        return new RelationSubQuery(subQuery, subQueryAlias);
    }

    /**
     * 初始化标识符节点实例
     *
     * @param parts String...
     * @return QualifiedName node(标识符节点)
     */
    public static QualifiedName qualifiedName(String... parts) {
        return new QualifiedName(Arrays.asList(parts));
    }

    /**
     * 初始化null类型的Literal(文字)节点实例
     *
     * @return Literal node(null)
     */
    public static Literal nullLiteral() {
        return literal(null, DataType.NULL);
    }

    /**
     * 初始化int类型的Literal(文字)节点实例
     *
     * @param value Integer
     * @return Literal node(integer)
     */
    public static Literal intLiteral(Integer value) {
        return literal(value, DataType.INTEGER);
    }

    /**
     * 初始化double类型的Literal(文字)节点实例
     *
     * @param value double
     * @return Literal node(double)
     */
    public static Literal doubleLiteral(double value) {
        return literal(value, DataType.DOUBLE);
    }

    /**
     * 初始化String类型的Literal(文字)节点实例
     *
     * @param value String
     * @return Literal node(string)
     */
    public static Literal stringLiteral(String value) {
        return literal(value, DataType.STRING);
    }

    /**
     * 初始化boolean类型的Literal(文字)节点实例
     *
     * @param value boolean
     * @return Literal node(boolean)
     */
    public static Literal booleanLiteral(boolean value) {
        return literal(value, DataType.BOOLEAN);
    }

    /**
     * 初始化Date类型的Literal(文字)节点实例 ex: “YYYY-MM-DD”   ‘1000-01-01’ ~ ‘9999-12-31’
     *
     * @param value String
     * @return Literal node(date)
     */
    public static Literal dateLiteral(String value) {
        return literal(value, DataType.DATE);
    }

    /**
     * 初始化Time类型的Literal(文字)节点实例    ex: “HH:MM:SS” ‘-838:59:59’ ~ ’838:59:59’
     *
     * @param value String
     * @return Literal node(time)
     */
    public static Literal timeLiteral(String value) {
        return literal(value, DataType.TIME);
    }

    /**
     * 初始化Timestamp类型的Literal(文字)节点实例    ex: ’1970-01-01 00:00:01’ ~ ‘2038-01-19 03:14:07’
     *
     * @param value String
     * @return Literal  node(timestamp)
     */
    public static Literal timestampLiteral(String value) {
        return literal(value, DataType.TIMESTAMP);
    }

    /**
     * 初始化Interval类型的间隔节点实例
     *
     * @param value Object Interval类型的值信息
     * @param type  数据类型
     * @param unit  间隔单位
     * @return Interval node
     */
    public static Interval intervalLiteral(Object value, DataType type, String unit) {
        return new Interval(literal(value, type), unit);
    }

    /**
     * 初始化Literal(文字)节点实例
     *
     * @param value Object 文字对应的值
     * @param type  定义Literal的最小单位文字的类型
     * @return Literal node
     */
    private static Literal literal(Object value, DataType type) {
        return new Literal(value, type);
    }

    /**
     * 初始化逻辑与的表达式节点实例
     *
     * @param left  左表达式
     * @param right 右表达式
     * @return And
     */
    public static And and(UnresolvedExpression left, UnresolvedExpression right) {
        return new And(left, right);
    }

    /**
     * 初始化逻辑或的表达式节点实例
     *
     * @param left  左表达式
     * @param right 右表达式
     * @return Or
     */
    public static Or or(UnresolvedExpression left, UnresolvedExpression right) {
        return new Or(left, right);
    }

    /**
     * 初始化逻辑非的表达式节点实例
     *
     * @param expression 表达式
     * @return Not
     */
    public static Not not(UnresolvedExpression expression) {
        return new Not(expression);
    }


    /**
     * Case Function CASE age WHEN 30 THEN 'age1' ELSE 'age2' END
     *
     * @param caseValueExpr UnresolvedExpression CASE  age  (单个表达式 类似于switch 中的  switch(Expression))
     * @param elseClause    UnresolvedExpression    ELSE 'age2' (单个else 类似于switch条件中的 default)
     * @param whenClauses   When... WHEN 30 THEN 'age1' ... (多个when类似于switch条件中的 case ...)
     * @return Case
     */
    public Case caseWhen(UnresolvedExpression caseValueExpr,
                         UnresolvedExpression elseClause,
                         When... whenClauses) {
        return new Case(caseValueExpr, Arrays.asList(whenClauses), elseClause);
    }

    /**
     * 上述Case 语句中When...中的单个when  如：Case age When 30
     *
     * @param condition UnresolvedExpression  Case  Expression (age)
     * @param result    UnresolvedExpression When Expression(age)
     * @return When
     */
    public When when(UnresolvedExpression condition, UnresolvedExpression result) {
        return new When(condition, result);
    }

    /**
     * 初始化Cast函数的表达式节点，参数包括待转换的参数(Literal)以及待转换的类型
     *
     * @param expr UnresolvedExpression    待转换的参数(Literal)
     * @param type Literal 待转换的类型
     * @return Cast
     */
    public Cast cast(UnresolvedExpression expr, Literal type) {
        return new Cast(expr, type);
    }

    /**
     * 初始化AggregateFunction函数的表达式节点
     *
     * @param funcName String AggregateFunction函数名称
     * @param field    UnresolvedExpression 字段的参数
     * @return AggregateFunction
     */
    public static AggregateFunction aggregate(String funcName, UnresolvedExpression field) {
        return new AggregateFunction(funcName, field);
    }

    /**
     * 初始化function(方法)节点实例
     *
     * @param funcName 方法名称
     * @param funcArgs 方法参数
     * @return Function
     */
    public static Function function(String funcName, UnresolvedExpression... funcArgs) {
        return new Function(funcName, Arrays.asList(funcArgs));
    }
}
