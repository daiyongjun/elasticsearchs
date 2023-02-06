package cn.gsdata.elasticsearchs.open.sql.core.ast.expression;

import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * 定义标识符，什么是标识符在任何语言中都有标识符的定义，如java语言中用来表示类名，变量名，方法名，类型名，数组名，文件名的有效字符序列称为标识符。
 * 通常它也有着特定的规则，如同java语言一样我们可以进行定义成columnName、tableName等
 * columnName : qualifiedName ,tableName : qualifiedName
 *
 * @author daiyongjun
 */
public class QualifiedName extends UnresolvedExpression {
    /**
     * QualifiedName 的name值 ident (DOT ident)*
     * ident : DOT? ID | BACKTICK_QUOTE_ID | keywordsCanBeId
     * DOT : '.'
     * ID : ID_LITERAL
     * ID_LITERAL :  [@*A-Z]+?[*A-Z_\-0-9]*
     * BACKTICK_QUOTE_ID : BQUOTA_STRING
     * BQUOTA_STRING : '`' ( '\\'. | '``' | ~('`'|'\\'))* '`'
     * keywordsCanBeId : (FULL : 'FULL')｜(FIELD : 'FIELD')｜(D : 'D')｜(T : 'T')｜(TS : 'TS') // OD SQL and ODBC special | (COUNT : 'COUNT') | (SUM : 'SUM') | (AVG : 'AVG') | (MAX : 'MAX') | (MIN : 'MIN') | (TIMESTAMP : 'TIMESTAMP') | (DATE : 'DATE')| (TIME : 'TIME') | (DAYOFWEEK : 'DAYOFWEEK') | (FIRST : 'FIRST') | (LAST : 'LAST')
     */
    private final List<String> parts;

    public QualifiedName(String name) {
        this.parts = Collections.singletonList(name);
    }

    public QualifiedName(Iterable<String> parts) {
        List<String> partsList = StreamSupport.stream(parts.spliterator(), false).collect(toList());
        if (partsList.isEmpty()) {
            throw new IllegalArgumentException("parts is empty");
        }
        this.parts = partsList;
    }

    @Override
    public String toString() {
        return String.join(".", this.parts);
    }
}
