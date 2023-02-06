// Generated from /Users/daiyongjun/编码/projects/elasticsearchs/sql/sql/src/main/java/antlr/OpenDistroSQLIdentifierParser.g4 by ANTLR 4.10.1
package antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link OpenDistroSQLIdentifierParser}.
 */
public interface OpenDistroSQLIdentifierParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link OpenDistroSQLIdentifierParser#tableName}.
	 * @param ctx the parse tree
	 */
	void enterTableName(OpenDistroSQLIdentifierParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link OpenDistroSQLIdentifierParser#tableName}.
	 * @param ctx the parse tree
	 */
	void exitTableName(OpenDistroSQLIdentifierParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link OpenDistroSQLIdentifierParser#columnName}.
	 * @param ctx the parse tree
	 */
	void enterColumnName(OpenDistroSQLIdentifierParser.ColumnNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link OpenDistroSQLIdentifierParser#columnName}.
	 * @param ctx the parse tree
	 */
	void exitColumnName(OpenDistroSQLIdentifierParser.ColumnNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link OpenDistroSQLIdentifierParser#alias}.
	 * @param ctx the parse tree
	 */
	void enterAlias(OpenDistroSQLIdentifierParser.AliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link OpenDistroSQLIdentifierParser#alias}.
	 * @param ctx the parse tree
	 */
	void exitAlias(OpenDistroSQLIdentifierParser.AliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link OpenDistroSQLIdentifierParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(OpenDistroSQLIdentifierParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link OpenDistroSQLIdentifierParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(OpenDistroSQLIdentifierParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link OpenDistroSQLIdentifierParser#ident}.
	 * @param ctx the parse tree
	 */
	void enterIdent(OpenDistroSQLIdentifierParser.IdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OpenDistroSQLIdentifierParser#ident}.
	 * @param ctx the parse tree
	 */
	void exitIdent(OpenDistroSQLIdentifierParser.IdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OpenDistroSQLIdentifierParser#keywordsCanBeId}.
	 * @param ctx the parse tree
	 */
	void enterKeywordsCanBeId(OpenDistroSQLIdentifierParser.KeywordsCanBeIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link OpenDistroSQLIdentifierParser#keywordsCanBeId}.
	 * @param ctx the parse tree
	 */
	void exitKeywordsCanBeId(OpenDistroSQLIdentifierParser.KeywordsCanBeIdContext ctx);
}