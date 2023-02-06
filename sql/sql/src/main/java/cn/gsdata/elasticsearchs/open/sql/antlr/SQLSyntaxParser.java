package cn.gsdata.elasticsearchs.open.sql.antlr;

import cn.gsdata.elasticsearchs.open.sql.common.antlr.CaseInsensitiveCharStream;
import gen.OpenDistroSQLLexer;
import gen.OpenDistroSQLParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * 封装了 ANTLR 解析器的 SQL 语法解析器。
 *
 * @author daiyongjun
 */
public class SQLSyntaxParser {
    /**
     * parser.root(); 获取root tree
     * root :  sqlStatement? SEMI? EOF
     * sqlStatement :  dmlStatement | adminStatement
     * dmlStatement : selectStatement
     * selectStatement : querySpecification
     * adminStatement : showStatement | describeStatement
     * showStatement : SHOW TABLES tableFilter?
     * describeStatement :  DESCRIBE TABLES tableFilter columnFilter?
     */
    public ParseTree parse(String query) {
        OpenDistroSQLLexer lexer = new OpenDistroSQLLexer(new CaseInsensitiveCharStream(query));
        OpenDistroSQLParser parser = new OpenDistroSQLParser(new CommonTokenStream(lexer));
        return parser.root();
    }
}
