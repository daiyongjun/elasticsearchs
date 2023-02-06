// Generated from /Users/daiyongjun/编码/projects/elasticsearchs/sql/sql/src/main/java/antlr/OpenDistroSQLIdentifierParser.g4 by ANTLR 4.10.1
package antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class OpenDistroSQLIdentifierParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		SPACE=1, SPEC_SQL_COMMENT=2, COMMENT_INPUT=3, LINE_COMMENT=4, ALL=5, AND=6, 
		AS=7, ASC=8, BOOLEAN=9, BETWEEN=10, BY=11, CASE=12, CAST=13, CROSS=14, 
		COLUMNS=15, DATETIME=16, DELETE=17, DESC=18, DESCRIBE=19, DISTINCT=20, 
		DOUBLE=21, ELSE=22, EXISTS=23, FALSE=24, FLOAT=25, FIRST=26, FROM=27, 
		GROUP=28, HAVING=29, IN=30, INNER=31, INT=32, IS=33, JOIN=34, LAST=35, 
		LEFT=36, LIKE=37, LIMIT=38, LONG=39, MATCH=40, NATURAL=41, MISSING_LITERAL=42, 
		NOT=43, NULL_LITERAL=44, NULLS=45, ON=46, OR=47, ORDER=48, OUTER=49, OVER=50, 
		PARTITION=51, REGEXP=52, RIGHT=53, SELECT=54, SHOW=55, STRING=56, THEN=57, 
		TRUE=58, UNION=59, USING=60, WHEN=61, WHERE=62, MISSING=63, EXCEPT=64, 
		AVG=65, COUNT=66, MAX=67, MIN=68, SUM=69, SUBSTRING=70, TRIM=71, END=72, 
		FULL=73, OFFSET=74, INTERVAL=75, MICROSECOND=76, SECOND=77, MINUTE=78, 
		HOUR=79, DAY=80, WEEK=81, MONTH=82, QUARTER=83, YEAR=84, SECOND_MICROSECOND=85, 
		MINUTE_MICROSECOND=86, MINUTE_SECOND=87, HOUR_MICROSECOND=88, HOUR_SECOND=89, 
		HOUR_MINUTE=90, DAY_MICROSECOND=91, DAY_SECOND=92, DAY_MINUTE=93, DAY_HOUR=94, 
		YEAR_MONTH=95, TABLES=96, ABS=97, ACOS=98, ADD=99, ASCII=100, ASIN=101, 
		ATAN=102, ATAN2=103, CBRT=104, CEIL=105, CEILING=106, CONCAT=107, CONCAT_WS=108, 
		CONV=109, COS=110, COSH=111, COT=112, CRC32=113, CURDATE=114, DATE=115, 
		DATE_FORMAT=116, DATE_ADD=117, DATE_SUB=118, DAYOFMONTH=119, DAYOFWEEK=120, 
		DAYOFYEAR=121, DAYNAME=122, DEGREES=123, E=124, EXP=125, EXPM1=126, FLOOR=127, 
		FROM_DAYS=128, IF=129, IFNULL=130, ISNULL=131, LENGTH=132, LN=133, LOCATE=134, 
		LOG=135, LOG10=136, LOG2=137, LOWER=138, LTRIM=139, MAKETIME=140, MODULUS=141, 
		MONTHNAME=142, MULTIPLY=143, NOW=144, NULLIF=145, PI=146, POW=147, POWER=148, 
		RADIANS=149, RAND=150, REPLACE=151, RINT=152, ROUND=153, RTRIM=154, SIGN=155, 
		SIGNUM=156, SIN=157, SINH=158, SQRT=159, SUBDATE=160, SUBTRACT=161, TAN=162, 
		TIME=163, TIME_TO_SEC=164, TIMESTAMP=165, TRUNCATE=166, TO_DAYS=167, UPPER=168, 
		D=169, T=170, TS=171, LEFT_BRACE=172, RIGHT_BRACE=173, DENSE_RANK=174, 
		RANK=175, ROW_NUMBER=176, DATE_HISTOGRAM=177, DAY_OF_MONTH=178, DAY_OF_YEAR=179, 
		DAY_OF_WEEK=180, EXCLUDE=181, EXTENDED_STATS=182, FIELD=183, FILTER=184, 
		GEO_BOUNDING_BOX=185, GEO_CELL=186, GEO_DISTANCE=187, GEO_DISTANCE_RANGE=188, 
		GEO_INTERSECTS=189, GEO_POLYGON=190, HISTOGRAM=191, HOUR_OF_DAY=192, INCLUDE=193, 
		IN_TERMS=194, MATCHPHRASE=195, MATCH_PHRASE=196, MATCHQUERY=197, MATCH_QUERY=198, 
		MINUTE_OF_DAY=199, MINUTE_OF_HOUR=200, MONTH_OF_YEAR=201, MULTIMATCH=202, 
		MULTI_MATCH=203, NESTED=204, PERCENTILES=205, REGEXP_QUERY=206, REVERSE_NESTED=207, 
		QUERY=208, RANGE=209, SCORE=210, SECOND_OF_MINUTE=211, STATS=212, TERM=213, 
		TERMS=214, TOPHITS=215, WEEK_OF_YEAR=216, WILDCARDQUERY=217, WILDCARD_QUERY=218, 
		SUBSTR=219, STRCMP=220, ADDDATE=221, STAR=222, DIVIDE=223, MODULE=224, 
		PLUS=225, MINUS=226, DIV=227, MOD=228, EQUAL_SYMBOL=229, GREATER_SYMBOL=230, 
		LESS_SYMBOL=231, EXCLAMATION_SYMBOL=232, BIT_NOT_OP=233, BIT_OR_OP=234, 
		BIT_AND_OP=235, BIT_XOR_OP=236, DOT=237, LR_BRACKET=238, RR_BRACKET=239, 
		COMMA=240, SEMI=241, AT_SIGN=242, ZERO_DECIMAL=243, ONE_DECIMAL=244, TWO_DECIMAL=245, 
		SINGLE_QUOTE_SYMB=246, DOUBLE_QUOTE_SYMB=247, REVERSE_QUOTE_SYMB=248, 
		COLON_SYMB=249, START_NATIONAL_STRING_LITERAL=250, STRING_LITERAL=251, 
		DECIMAL_LITERAL=252, HEXADECIMAL_LITERAL=253, REAL_LITERAL=254, NULL_SPEC_LITERAL=255, 
		BIT_STRING=256, ID=257, DOUBLE_QUOTE_ID=258, BACKTICK_QUOTE_ID=259, ERROR_RECOGNITION=260;
	public static final int
		RULE_tableName = 0, RULE_columnName = 1, RULE_alias = 2, RULE_qualifiedName = 3, 
		RULE_ident = 4, RULE_keywordsCanBeId = 5;
	private static String[] makeRuleNames() {
		return new String[] {
			"tableName", "columnName", "alias", "qualifiedName", "ident", "keywordsCanBeId"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, "'ALL'", "'AND'", "'AS'", "'ASC'", "'BOOLEAN'", 
			"'BETWEEN'", "'BY'", "'CASE'", "'CAST'", "'CROSS'", "'COLUMNS'", "'DATETIME'", 
			"'DELETE'", "'DESC'", "'DESCRIBE'", "'DISTINCT'", "'DOUBLE'", "'ELSE'", 
			"'EXISTS'", "'FALSE'", "'FLOAT'", "'FIRST'", "'FROM'", "'GROUP'", "'HAVING'", 
			"'IN'", "'INNER'", "'INT'", "'IS'", "'JOIN'", "'LAST'", "'LEFT'", "'LIKE'", 
			"'LIMIT'", "'LONG'", "'MATCH'", "'NATURAL'", null, "'NOT'", "'NULL'", 
			"'NULLS'", "'ON'", "'OR'", "'ORDER'", "'OUTER'", "'OVER'", "'PARTITION'", 
			"'REGEXP'", "'RIGHT'", "'SELECT'", "'SHOW'", "'STRING'", "'THEN'", "'TRUE'", 
			"'UNION'", "'USING'", "'WHEN'", "'WHERE'", null, "'MINUS'", "'AVG'", 
			"'COUNT'", "'MAX'", "'MIN'", "'SUM'", "'SUBSTRING'", "'TRIM'", "'END'", 
			"'FULL'", "'OFFSET'", "'INTERVAL'", "'MICROSECOND'", "'SECOND'", "'MINUTE'", 
			"'HOUR'", "'DAY'", "'WEEK'", "'MONTH'", "'QUARTER'", "'YEAR'", "'SECOND_MICROSECOND'", 
			"'MINUTE_MICROSECOND'", "'MINUTE_SECOND'", "'HOUR_MICROSECOND'", "'HOUR_SECOND'", 
			"'HOUR_MINUTE'", "'DAY_MICROSECOND'", "'DAY_SECOND'", "'DAY_MINUTE'", 
			"'DAY_HOUR'", "'YEAR_MONTH'", "'TABLES'", "'ABS'", "'ACOS'", "'ADD'", 
			"'ASCII'", "'ASIN'", "'ATAN'", "'ATAN2'", "'CBRT'", "'CEIL'", "'CEILING'", 
			"'CONCAT'", "'CONCAT_WS'", "'CONV'", "'COS'", "'COSH'", "'COT'", "'CRC32'", 
			"'CURDATE'", "'DATE'", "'DATE_FORMAT'", "'DATE_ADD'", "'DATE_SUB'", "'DAYOFMONTH'", 
			"'DAYOFWEEK'", "'DAYOFYEAR'", "'DAYNAME'", "'DEGREES'", "'E'", "'EXP'", 
			"'EXPM1'", "'FLOOR'", "'FROM_DAYS'", "'IF'", "'IFNULL'", "'ISNULL'", 
			"'LENGTH'", "'LN'", "'LOCATE'", "'LOG'", "'LOG10'", "'LOG2'", "'LOWER'", 
			"'LTRIM'", "'MAKETIME'", "'MODULUS'", "'MONTHNAME'", "'MULTIPLY'", "'NOW'", 
			"'NULLIF'", "'PI'", "'POW'", "'POWER'", "'RADIANS'", "'RAND'", "'REPLACE'", 
			"'RINT'", "'ROUND'", "'RTRIM'", "'SIGN'", "'SIGNUM'", "'SIN'", "'SINH'", 
			"'SQRT'", "'SUBDATE'", "'SUBTRACT'", "'TAN'", "'TIME'", "'TIME_TO_SEC'", 
			"'TIMESTAMP'", "'TRUNCATE'", "'TO_DAYS'", "'UPPER'", "'D'", "'T'", "'TS'", 
			"'{'", "'}'", "'DENSE_RANK'", "'RANK'", "'ROW_NUMBER'", "'DATE_HISTOGRAM'", 
			"'DAY_OF_MONTH'", "'DAY_OF_YEAR'", "'DAY_OF_WEEK'", "'EXCLUDE'", "'EXTENDED_STATS'", 
			"'FIELD'", "'FILTER'", "'GEO_BOUNDING_BOX'", "'GEO_CELL'", "'GEO_DISTANCE'", 
			"'GEO_DISTANCE_RANGE'", "'GEO_INTERSECTS'", "'GEO_POLYGON'", "'HISTOGRAM'", 
			"'HOUR_OF_DAY'", "'INCLUDE'", "'IN_TERMS'", "'MATCHPHRASE'", "'MATCH_PHRASE'", 
			"'MATCHQUERY'", "'MATCH_QUERY'", "'MINUTE_OF_DAY'", "'MINUTE_OF_HOUR'", 
			"'MONTH_OF_YEAR'", "'MULTIMATCH'", "'MULTI_MATCH'", "'NESTED'", "'PERCENTILES'", 
			"'REGEXP_QUERY'", "'REVERSE_NESTED'", "'QUERY'", "'RANGE'", "'SCORE'", 
			"'SECOND_OF_MINUTE'", "'STATS'", "'TERM'", "'TERMS'", "'TOPHITS'", "'WEEK_OF_YEAR'", 
			"'WILDCARDQUERY'", "'WILDCARD_QUERY'", "'SUBSTR'", "'STRCMP'", "'ADDDATE'", 
			"'*'", "'/'", "'%'", "'+'", "'-'", "'DIV'", "'MOD'", "'='", "'>'", "'<'", 
			"'!'", "'~'", "'|'", "'&'", "'^'", "'.'", "'('", "')'", "','", "';'", 
			"'@'", "'0'", "'1'", "'2'", "'''", "'\"'", "'`'", "':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "SPACE", "SPEC_SQL_COMMENT", "COMMENT_INPUT", "LINE_COMMENT", "ALL", 
			"AND", "AS", "ASC", "BOOLEAN", "BETWEEN", "BY", "CASE", "CAST", "CROSS", 
			"COLUMNS", "DATETIME", "DELETE", "DESC", "DESCRIBE", "DISTINCT", "DOUBLE", 
			"ELSE", "EXISTS", "FALSE", "FLOAT", "FIRST", "FROM", "GROUP", "HAVING", 
			"IN", "INNER", "INT", "IS", "JOIN", "LAST", "LEFT", "LIKE", "LIMIT", 
			"LONG", "MATCH", "NATURAL", "MISSING_LITERAL", "NOT", "NULL_LITERAL", 
			"NULLS", "ON", "OR", "ORDER", "OUTER", "OVER", "PARTITION", "REGEXP", 
			"RIGHT", "SELECT", "SHOW", "STRING", "THEN", "TRUE", "UNION", "USING", 
			"WHEN", "WHERE", "MISSING", "EXCEPT", "AVG", "COUNT", "MAX", "MIN", "SUM", 
			"SUBSTRING", "TRIM", "END", "FULL", "OFFSET", "INTERVAL", "MICROSECOND", 
			"SECOND", "MINUTE", "HOUR", "DAY", "WEEK", "MONTH", "QUARTER", "YEAR", 
			"SECOND_MICROSECOND", "MINUTE_MICROSECOND", "MINUTE_SECOND", "HOUR_MICROSECOND", 
			"HOUR_SECOND", "HOUR_MINUTE", "DAY_MICROSECOND", "DAY_SECOND", "DAY_MINUTE", 
			"DAY_HOUR", "YEAR_MONTH", "TABLES", "ABS", "ACOS", "ADD", "ASCII", "ASIN", 
			"ATAN", "ATAN2", "CBRT", "CEIL", "CEILING", "CONCAT", "CONCAT_WS", "CONV", 
			"COS", "COSH", "COT", "CRC32", "CURDATE", "DATE", "DATE_FORMAT", "DATE_ADD", 
			"DATE_SUB", "DAYOFMONTH", "DAYOFWEEK", "DAYOFYEAR", "DAYNAME", "DEGREES", 
			"E", "EXP", "EXPM1", "FLOOR", "FROM_DAYS", "IF", "IFNULL", "ISNULL", 
			"LENGTH", "LN", "LOCATE", "LOG", "LOG10", "LOG2", "LOWER", "LTRIM", "MAKETIME", 
			"MODULUS", "MONTHNAME", "MULTIPLY", "NOW", "NULLIF", "PI", "POW", "POWER", 
			"RADIANS", "RAND", "REPLACE", "RINT", "ROUND", "RTRIM", "SIGN", "SIGNUM", 
			"SIN", "SINH", "SQRT", "SUBDATE", "SUBTRACT", "TAN", "TIME", "TIME_TO_SEC", 
			"TIMESTAMP", "TRUNCATE", "TO_DAYS", "UPPER", "D", "T", "TS", "LEFT_BRACE", 
			"RIGHT_BRACE", "DENSE_RANK", "RANK", "ROW_NUMBER", "DATE_HISTOGRAM", 
			"DAY_OF_MONTH", "DAY_OF_YEAR", "DAY_OF_WEEK", "EXCLUDE", "EXTENDED_STATS", 
			"FIELD", "FILTER", "GEO_BOUNDING_BOX", "GEO_CELL", "GEO_DISTANCE", "GEO_DISTANCE_RANGE", 
			"GEO_INTERSECTS", "GEO_POLYGON", "HISTOGRAM", "HOUR_OF_DAY", "INCLUDE", 
			"IN_TERMS", "MATCHPHRASE", "MATCH_PHRASE", "MATCHQUERY", "MATCH_QUERY", 
			"MINUTE_OF_DAY", "MINUTE_OF_HOUR", "MONTH_OF_YEAR", "MULTIMATCH", "MULTI_MATCH", 
			"NESTED", "PERCENTILES", "REGEXP_QUERY", "REVERSE_NESTED", "QUERY", "RANGE", 
			"SCORE", "SECOND_OF_MINUTE", "STATS", "TERM", "TERMS", "TOPHITS", "WEEK_OF_YEAR", 
			"WILDCARDQUERY", "WILDCARD_QUERY", "SUBSTR", "STRCMP", "ADDDATE", "STAR", 
			"DIVIDE", "MODULE", "PLUS", "MINUS", "DIV", "MOD", "EQUAL_SYMBOL", "GREATER_SYMBOL", 
			"LESS_SYMBOL", "EXCLAMATION_SYMBOL", "BIT_NOT_OP", "BIT_OR_OP", "BIT_AND_OP", 
			"BIT_XOR_OP", "DOT", "LR_BRACKET", "RR_BRACKET", "COMMA", "SEMI", "AT_SIGN", 
			"ZERO_DECIMAL", "ONE_DECIMAL", "TWO_DECIMAL", "SINGLE_QUOTE_SYMB", "DOUBLE_QUOTE_SYMB", 
			"REVERSE_QUOTE_SYMB", "COLON_SYMB", "START_NATIONAL_STRING_LITERAL", 
			"STRING_LITERAL", "DECIMAL_LITERAL", "HEXADECIMAL_LITERAL", "REAL_LITERAL", 
			"NULL_SPEC_LITERAL", "BIT_STRING", "ID", "DOUBLE_QUOTE_ID", "BACKTICK_QUOTE_ID", 
			"ERROR_RECOGNITION"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "OpenDistroSQLIdentifierParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public OpenDistroSQLIdentifierParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class TableNameContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TableNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OpenDistroSQLIdentifierParserListener ) ((OpenDistroSQLIdentifierParserListener)listener).enterTableName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OpenDistroSQLIdentifierParserListener ) ((OpenDistroSQLIdentifierParserListener)listener).exitTableName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OpenDistroSQLIdentifierParserVisitor ) return ((OpenDistroSQLIdentifierParserVisitor<? extends T>)visitor).visitTableName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableNameContext tableName() throws RecognitionException {
		TableNameContext _localctx = new TableNameContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_tableName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(12);
			qualifiedName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnNameContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public ColumnNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OpenDistroSQLIdentifierParserListener ) ((OpenDistroSQLIdentifierParserListener)listener).enterColumnName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OpenDistroSQLIdentifierParserListener ) ((OpenDistroSQLIdentifierParserListener)listener).exitColumnName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OpenDistroSQLIdentifierParserVisitor ) return ((OpenDistroSQLIdentifierParserVisitor<? extends T>)visitor).visitColumnName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnNameContext columnName() throws RecognitionException {
		ColumnNameContext _localctx = new ColumnNameContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_columnName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(14);
			qualifiedName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AliasContext extends ParserRuleContext {
		public IdentContext ident() {
			return getRuleContext(IdentContext.class,0);
		}
		public AliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OpenDistroSQLIdentifierParserListener ) ((OpenDistroSQLIdentifierParserListener)listener).enterAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OpenDistroSQLIdentifierParserListener ) ((OpenDistroSQLIdentifierParserListener)listener).exitAlias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OpenDistroSQLIdentifierParserVisitor ) return ((OpenDistroSQLIdentifierParserVisitor<? extends T>)visitor).visitAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AliasContext alias() throws RecognitionException {
		AliasContext _localctx = new AliasContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(16);
			ident();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QualifiedNameContext extends ParserRuleContext {
		public List<IdentContext> ident() {
			return getRuleContexts(IdentContext.class);
		}
		public IdentContext ident(int i) {
			return getRuleContext(IdentContext.class,i);
		}
		public List<TerminalNode> DOT() { return getTokens(OpenDistroSQLIdentifierParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(OpenDistroSQLIdentifierParser.DOT, i);
		}
		public QualifiedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OpenDistroSQLIdentifierParserListener ) ((OpenDistroSQLIdentifierParserListener)listener).enterQualifiedName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OpenDistroSQLIdentifierParserListener ) ((OpenDistroSQLIdentifierParserListener)listener).exitQualifiedName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OpenDistroSQLIdentifierParserVisitor ) return ((OpenDistroSQLIdentifierParserVisitor<? extends T>)visitor).visitQualifiedName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifiedNameContext qualifiedName() throws RecognitionException {
		QualifiedNameContext _localctx = new QualifiedNameContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18);
			ident();
			setState(23);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOT) {
				{
				{
				setState(19);
				match(DOT);
				setState(20);
				ident();
				}
				}
				setState(25);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(OpenDistroSQLIdentifierParser.ID, 0); }
		public TerminalNode DOT() { return getToken(OpenDistroSQLIdentifierParser.DOT, 0); }
		public TerminalNode BACKTICK_QUOTE_ID() { return getToken(OpenDistroSQLIdentifierParser.BACKTICK_QUOTE_ID, 0); }
		public KeywordsCanBeIdContext keywordsCanBeId() {
			return getRuleContext(KeywordsCanBeIdContext.class,0);
		}
		public IdentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ident; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OpenDistroSQLIdentifierParserListener ) ((OpenDistroSQLIdentifierParserListener)listener).enterIdent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OpenDistroSQLIdentifierParserListener ) ((OpenDistroSQLIdentifierParserListener)listener).exitIdent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OpenDistroSQLIdentifierParserVisitor ) return ((OpenDistroSQLIdentifierParserVisitor<? extends T>)visitor).visitIdent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentContext ident() throws RecognitionException {
		IdentContext _localctx = new IdentContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_ident);
		int _la;
		try {
			setState(32);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DOT:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(27);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==DOT) {
					{
					setState(26);
					match(DOT);
					}
				}

				setState(29);
				match(ID);
				}
				break;
			case BACKTICK_QUOTE_ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(30);
				match(BACKTICK_QUOTE_ID);
				}
				break;
			case FIRST:
			case LAST:
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
			case FULL:
			case DATE:
			case DAYOFWEEK:
			case TIME:
			case TIMESTAMP:
			case D:
			case T:
			case TS:
			case FIELD:
				enterOuterAlt(_localctx, 3);
				{
				setState(31);
				keywordsCanBeId();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeywordsCanBeIdContext extends ParserRuleContext {
		public TerminalNode FULL() { return getToken(OpenDistroSQLIdentifierParser.FULL, 0); }
		public TerminalNode FIELD() { return getToken(OpenDistroSQLIdentifierParser.FIELD, 0); }
		public TerminalNode D() { return getToken(OpenDistroSQLIdentifierParser.D, 0); }
		public TerminalNode T() { return getToken(OpenDistroSQLIdentifierParser.T, 0); }
		public TerminalNode TS() { return getToken(OpenDistroSQLIdentifierParser.TS, 0); }
		public TerminalNode COUNT() { return getToken(OpenDistroSQLIdentifierParser.COUNT, 0); }
		public TerminalNode SUM() { return getToken(OpenDistroSQLIdentifierParser.SUM, 0); }
		public TerminalNode AVG() { return getToken(OpenDistroSQLIdentifierParser.AVG, 0); }
		public TerminalNode MAX() { return getToken(OpenDistroSQLIdentifierParser.MAX, 0); }
		public TerminalNode MIN() { return getToken(OpenDistroSQLIdentifierParser.MIN, 0); }
		public TerminalNode TIMESTAMP() { return getToken(OpenDistroSQLIdentifierParser.TIMESTAMP, 0); }
		public TerminalNode DATE() { return getToken(OpenDistroSQLIdentifierParser.DATE, 0); }
		public TerminalNode TIME() { return getToken(OpenDistroSQLIdentifierParser.TIME, 0); }
		public TerminalNode DAYOFWEEK() { return getToken(OpenDistroSQLIdentifierParser.DAYOFWEEK, 0); }
		public TerminalNode FIRST() { return getToken(OpenDistroSQLIdentifierParser.FIRST, 0); }
		public TerminalNode LAST() { return getToken(OpenDistroSQLIdentifierParser.LAST, 0); }
		public KeywordsCanBeIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keywordsCanBeId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OpenDistroSQLIdentifierParserListener ) ((OpenDistroSQLIdentifierParserListener)listener).enterKeywordsCanBeId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OpenDistroSQLIdentifierParserListener ) ((OpenDistroSQLIdentifierParserListener)listener).exitKeywordsCanBeId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OpenDistroSQLIdentifierParserVisitor ) return ((OpenDistroSQLIdentifierParserVisitor<? extends T>)visitor).visitKeywordsCanBeId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeywordsCanBeIdContext keywordsCanBeId() throws RecognitionException {
		KeywordsCanBeIdContext _localctx = new KeywordsCanBeIdContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_keywordsCanBeId);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(34);
			_la = _input.LA(1);
			if ( !(_la==FIRST || _la==LAST || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (AVG - 65)) | (1L << (COUNT - 65)) | (1L << (MAX - 65)) | (1L << (MIN - 65)) | (1L << (SUM - 65)) | (1L << (FULL - 65)) | (1L << (DATE - 65)) | (1L << (DAYOFWEEK - 65)))) != 0) || ((((_la - 163)) & ~0x3f) == 0 && ((1L << (_la - 163)) & ((1L << (TIME - 163)) | (1L << (TIMESTAMP - 163)) | (1L << (D - 163)) | (1L << (T - 163)) | (1L << (TS - 163)) | (1L << (FIELD - 163)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0104%\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001"+
		"\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003\u0016"+
		"\b\u0003\n\u0003\f\u0003\u0019\t\u0003\u0001\u0004\u0003\u0004\u001c\b"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004!\b\u0004\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0000\u0000\u0006\u0000\u0002\u0004\u0006"+
		"\b\n\u0000\u0001\n\u0000\u001a\u001a##AEIIssxx\u00a3\u00a3\u00a5\u00a5"+
		"\u00a9\u00ab\u00b7\u00b7\"\u0000\f\u0001\u0000\u0000\u0000\u0002\u000e"+
		"\u0001\u0000\u0000\u0000\u0004\u0010\u0001\u0000\u0000\u0000\u0006\u0012"+
		"\u0001\u0000\u0000\u0000\b \u0001\u0000\u0000\u0000\n\"\u0001\u0000\u0000"+
		"\u0000\f\r\u0003\u0006\u0003\u0000\r\u0001\u0001\u0000\u0000\u0000\u000e"+
		"\u000f\u0003\u0006\u0003\u0000\u000f\u0003\u0001\u0000\u0000\u0000\u0010"+
		"\u0011\u0003\b\u0004\u0000\u0011\u0005\u0001\u0000\u0000\u0000\u0012\u0017"+
		"\u0003\b\u0004\u0000\u0013\u0014\u0005\u00ed\u0000\u0000\u0014\u0016\u0003"+
		"\b\u0004\u0000\u0015\u0013\u0001\u0000\u0000\u0000\u0016\u0019\u0001\u0000"+
		"\u0000\u0000\u0017\u0015\u0001\u0000\u0000\u0000\u0017\u0018\u0001\u0000"+
		"\u0000\u0000\u0018\u0007\u0001\u0000\u0000\u0000\u0019\u0017\u0001\u0000"+
		"\u0000\u0000\u001a\u001c\u0005\u00ed\u0000\u0000\u001b\u001a\u0001\u0000"+
		"\u0000\u0000\u001b\u001c\u0001\u0000\u0000\u0000\u001c\u001d\u0001\u0000"+
		"\u0000\u0000\u001d!\u0005\u0101\u0000\u0000\u001e!\u0005\u0103\u0000\u0000"+
		"\u001f!\u0003\n\u0005\u0000 \u001b\u0001\u0000\u0000\u0000 \u001e\u0001"+
		"\u0000\u0000\u0000 \u001f\u0001\u0000\u0000\u0000!\t\u0001\u0000\u0000"+
		"\u0000\"#\u0007\u0000\u0000\u0000#\u000b\u0001\u0000\u0000\u0000\u0003"+
		"\u0017\u001b ";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}