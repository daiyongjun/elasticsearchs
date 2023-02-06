package cn.gsdata.elasticsearchs.open.sql.parser;

import cn.gsdata.elasticsearchs.open.sql.common.utils.StringUtils;
import cn.gsdata.elasticsearchs.open.sql.core.ast.dsl.AstDSL;
import cn.gsdata.elasticsearchs.open.sql.core.ast.expression.*;
import cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.*;
import gen.OpenDistroSQLParser.*;
import gen.OpenDistroSQLParserBaseVisitor;
import org.antlr.v4.runtime.RuleContext;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cn.gsdata.elasticsearchs.open.sql.parser.ParserUtils.createSortOption;

/**
 * Ast(定义的基础类型)表达式构建类，Ast 定义为node,其详细实现有expression和tree
 * (expression)表达式构建类，拓展 node节点分为expression(表达式)和tree两种，后期补充的注释
 *
 * @author daiyongjun
 */
public class AstExpressionBuilder extends OpenDistroSQLParserBaseVisitor<UnresolvedExpression> {

    /*
      functionCall : scalarFunctionCall ｜specificFunctionCall ｜windowFunctionCall ｜aggregateFunctionCall ｜filteredAggregationFunctionCall
      scalarFunctionCall ：scalarFunctionName LR_BRACKET functionArgs? RR_BRACKET
      specificFunctionCall ：specificFunction
      windowFunctionCall ： windowFunctionClause
      aggregateFunctionCall ：aggregateFunction
      filteredAggregationFunctionCall : aggregateFunction (orderByClause)? filterClause
     */

    /**
     * scalarFunctionCall : scalarFunctionName LR_BRACKET functionArgs? RR_BRACKET
     * scalarFunctionName : mathematicalFunctionName | dateTimeFunctionName | textFunctionName  | flowControlFunctionName
     * mathematicalFunctionName :
     * (ABS : 'ABS') | CEIL | CEILING | CONV | CRC32 | E | EXP | FLOOR | LN | LOG | LOG10 | LOG2 | MOD | PI | POW | POWER
     * | RAND | ROUND | SIGN | SQRT | TRUNCATE
     * | (trigonometricFunctionName : ACOS | ASIN | ATAN | ATAN2 | COS | COT | DEGREES | RADIANS | SIN | TAN)
     * dateTimeFunctionName :
     * ADDDATE | DATE | DATE_ADD | DATE_SUB | DAY | DAYNAME | DAYOFMONTH | DAYOFWEEK | DAYOFYEAR | FROM_DAYS
     * | HOUR | MICROSECOND | MINUTE | MONTH | MONTHNAME | QUARTER | SECOND | SUBDATE | TIME | TIME_TO_SEC
     * | TIMESTAMP | TO_DAYS | YEAR | WEEK | DATE_FORMAT
     * textFunctionName
     * : SUBSTR | SUBSTRING | TRIM | LTRIM | RTRIM | LOWER | UPPER
     * | CONCAT | CONCAT_WS | SUBSTR | LENGTH | STRCMP | RIGHT
     * flowControlFunctionName
     * : IF | IFNULL | NULLIF | ISNULL
     * LR_BRACKET : '('
     * functionArgs? : functionArg (COMMA functionArg)*?
     * RR_BRACKET :  ')'
     * functionArg : expression
     * expression : NOT expression | left=expression AND right=expression | left=expression OR right=expressio | predicate
     * COMMA : ','
     */
    @Override
    public UnresolvedExpression visitScalarFunctionCall(ScalarFunctionCallContext ctx) {
        return visitFunction(ctx.scalarFunctionName().getText(), ctx.functionArgs());
    }

    /**
     * VisitScalarFunctionCall Expression详细执行，初始化function
     *
     * @param functionName 方法名称
     * @param args         方法参数
     * @return Function 初始化Function节点
     */
    private Function visitFunction(String functionName, FunctionArgsContext args) {
        if (args == null) {
            return new Function(functionName, Collections.emptyList());
        }
        return new Function(functionName, args.functionArg().stream().map(this::visitFunctionArg).collect(Collectors.toList()));
    }

    /*
       specificFunction : caseFunctionCall  |  caseFunctionCall |  dataTypeFunctionCall
       caseFunctionCall : CASE expression caseFuncAlternative+ (ELSE elseArg=functionArg)? END
       caseFunctionCall : CASE caseFuncAlternative+ (ELSE elseArg=functionArg)? END
       dataTypeFunctionCall : CAST '(' expression AS convertedDataType ')'
       caseFuncAlternative : WHEN condition=functionArg THEN consequent=functionArg
       functionArg  : expression
     */

    /**
     * caseFunctionCall : CASE expression caseFuncAlternative+ (ELSE elseArg=functionArg)? END
     * caseFunctionCall : CASE caseFuncAlternative+ (ELSE elseArg=functionArg)? END
     * CASE : 'CASE'
     * expression :  notExpression | andExpression  | orExpression  | predicateExpression
     * caseFuncAlternative + :  (WHEN condition=functionArg THEN consequent=functionArg)+
     * (ELSE elseArg=functionArg)? : (ELSE elseArg=functionArg)?
     * functionArg  : expression
     */
    @Override
    public UnresolvedExpression visitCaseFunctionCall(CaseFunctionCallContext ctx) {
        UnresolvedExpression caseValue = (ctx.expression() == null) ? null : visit(ctx.expression());
        List<When> whenStatements = ctx.caseFuncAlternative().stream().map(when -> (When) visit(when)).collect(Collectors.toList());
        UnresolvedExpression elseStatement = (ctx.elseArg == null) ? null : visit(ctx.elseArg);
        return new Case(caseValue, whenStatements, elseStatement);
    }

    /*
      windowFunctionCall ： windowFunctionClause
     */

    /**
     * windowFunctionClause : function=windowFunction overClause
     * windowFunction : scalarWindowFunction | aggregateWindowFunction
     * scalarWindowFunction : functionName=(ROW_NUMBER | RANK | DENSE_RANK)  LR_BRACKET functionArgs? RR_BRACKET
     * functionName : ('ROW_NUMBER' | 'RANK' | 'DENSE_RANK')
     * LR_BRACKET : '('
     * functionArgs? : functionArg (COMMA functionArg)*?
     * RR_BRACKET :  ')'
     * functionArg  : expression
     * overClause : OVER LR_BRACKET partitionByClause? orderByClause? RR_BRACKET
     * OVER : 'OVER'
     * LR_BRACKET : '('
     * partitionByClause :  PARTITION BY expression (COMMA expression)*
     * PARTITION : 'PARTITION'
     * BY : 'BY'
     * expression :  notExpression | andExpression  | orExpression  | predicateExpression
     * COMMA : ','
     * RR_BRACKET :  ')'
     * orderByClause :  ORDER BY orderByElement (COMMA orderByElement)*
     * aggregateWindowFunction : aggregateFunction
     * aggregateFunction : functionName=aggregationFunctionName LR_BRACKET functionArg RR_BRACKET #regularAggregateFunctionCall | COUNT LR_BRACKET STAR RR_BRACKET
     */
    @Override
    public UnresolvedExpression visitWindowFunctionClause(WindowFunctionClauseContext ctx) {
        OverClauseContext overClause = ctx.overClause();
        //partitionByClause :  PARTITION BY expression (COMMA expression)*
        List<UnresolvedExpression> partitionByList = Collections.emptyList();
        if (overClause.partitionByClause() != null) {
            partitionByList = overClause.partitionByClause()
                    .expression()
                    .stream()
                    .map(this::visit)
                    .collect(Collectors.toList());
        }
        //orderByElement  : expression order=(ASC | DESC)? (NULLS (FIRST | LAST))?
        List<Pair<SortOption, UnresolvedExpression>> sortList = Collections.emptyList();
        if (overClause.orderByClause() != null) {
            sortList = overClause.orderByClause()
                    .orderByElement()
                    .stream()
                    .map(item -> ImmutablePair.of(
                            createSortOption(item), visit(item.expression())))
                    .collect(Collectors.toList());
        }
        //windowFunction : scalarWindowFunction | aggregateWindowFunction
        return new WindowFunction(visit(ctx.function), partitionByList, sortList);
    }

    /**
     * scalarWindowFunction : functionName=(ROW_NUMBER | RANK | DENSE_RANK)  LR_BRACKET functionArgs? RR_BRACKET
     * functionName : ('ROW_NUMBER' | 'RANK' | 'DENSE_RANK')
     * LR_BRACKET : '('
     * functionArgs? : functionArg (COMMA functionArg)*?
     * RR_BRACKET :  ')'
     * functionArg  : expression
     */
    @Override
    public UnresolvedExpression visitScalarWindowFunction(ScalarWindowFunctionContext ctx) {
        return visitFunction(ctx.functionName.getText(), ctx.functionArgs());
    }

    /**
     * caseFuncAlternative : WHEN condition=functionArg THEN consequent=functionArg
     * WHEN :  'WHEN'
     * condition=functionArg : expression
     * THEN : 'THEN'
     * consequent=functionArg : expression
     */
    @Override
    public UnresolvedExpression visitCaseFuncAlternative(CaseFuncAlternativeContext ctx) {
        return new When(visit(ctx.condition), visit(ctx.consequent));
    }

    /*
        aggregateFunction : regularAggregateFunctionCall ｜ countStarFunctionCall
        regularAggregateFunctionCall : functionName=aggregationFunctionName LR_BRACKET functionArg RR_BRACKET
        countStarFunctionCall : COUNT LR_BRACKET STAR RR_BRACKET
    */

    /**
     * regularAggregateFunctionCall : functionName=aggregationFunctionName LR_BRACKET functionArg RR_BRACKET
     * functionName : aggregationFunctionName LR_BRACKET functionArg RR_BRACKET
     * aggregationFunctionName : AVG('AVG') | COUNT('COUNT') | SUM('SUM') | MIN('MIN') | MAX('MAX')
     * LR_BRACKET : '('
     * functionArg : expression(notExpression | andExpression  | orExpression  | predicateExpression)
     * RR_BRACKET : ')'
     */
    @Override
    public UnresolvedExpression visitRegularAggregateFunctionCall(
            RegularAggregateFunctionCallContext ctx) {
        return new AggregateFunction(
                ctx.functionName.getText(),
                visitFunctionArg(ctx.functionArg()));
    }

    /**
     * countStarFunctionCall :  COUNT LR_BRACKET STAR RR_BRACKET
     * COUNT :  'COUNT'
     * LR_BRACKET : '('
     * STAR : '*'
     * RR_BRACKET : ')'
     */
    @Override
    public UnresolvedExpression visitCountStarFunctionCall(CountStarFunctionCallContext ctx) {
        return new AggregateFunction("COUNT", AllFields.of());
    }


    /**
     * dataTypeFunctionCall : CAST '(' expression AS convertedDataType ')'
     * CAST : 'CAST'
     * expression : notExpression | andExpression | orExpression | predicateExpression
     * AS : 'AS'
     * convertedDataType : typeName='DATE' ｜typeName='TIME' ｜ typeName='TIMESTAMP' ｜typeName='INT' | typeName='DOUBLE' ｜typeName='LONG' ｜typeName='FLOAT' ｜ typeName='STRING' ｜ typeName='BOOLEAN'
     */
    @Override
    public UnresolvedExpression visitDataTypeFunctionCall(
            DataTypeFunctionCallContext ctx) {
        return new Cast(visit(ctx.expression()), visit(ctx.convertedDataType()));
    }

    /**
     * convertedDataType : typeName='DATE' ｜typeName='TIME' ｜ typeName='TIMESTAMP' ｜typeName='INT' | typeName='DOUBLE' ｜typeName='LONG' ｜typeName='FLOAT' ｜ typeName='STRING' ｜ typeName='BOOLEAN'
     */
    @Override
    public UnresolvedExpression visitConvertedDataType(
            ConvertedDataTypeContext ctx) {
        return AstDSL.stringLiteral(ctx.getText());
    }

     /*
      expression : notExpression | andExpression | orExpression | predicateExpression
      notExpression : NOT expression
      andExpression : left=expression AND right=expression
      orExpression : left=expression OR right=expression
      predicateExpression : predicate
     */

    /**
     * andExpression : left=expression AND right=expression
     * left=expression : NOT expression | left=expression AND right=expression | left=expression OR right=expressio | predicate
     * AND : 'AND'
     * right=expression : NOT expression | left=expression AND right=expression | left=expression OR right=expressio | predicate
     */
    @Override
    public UnresolvedExpression visitAndExpression(AndExpressionContext ctx) {
        return new And(visit(ctx.left), visit(ctx.right));
    }

    /**
     * orExpression : left=expression OR right=expression
     * left=expression : NOT expression | left=expression AND right=expression | left=expression OR right=expressio | predicate
     * OR : 'OR'
     * right=expression : NOT expression | left=expression AND right=expression | left=expression OR right=expressio | predicate
     */
    @Override
    public UnresolvedExpression visitOrExpression(OrExpressionContext ctx) {
        return new Or(visit(ctx.left), visit(ctx.right));
    }

    /**
     * notExpression : NOT expression
     * NOT : 'NOT'
     * expression : NOT expression | left=expression AND right=expression | left=expression OR right=expressio | predicate
     */
    @Override
    public UnresolvedExpression visitNotExpression(NotExpressionContext ctx) {
        return new Not(visit(ctx.expression()));
    }


    /*
      predicate : expressionAtomPredicate ｜binaryComparisonPredicate ｜isNullPredicate ｜likePredicate ｜regexpPredicate
      expressionAtomPredicate ：expressionAtom
      binaryComparisonPredicate : left=predicate comparisonOperator right=predicate
      isNullPredicate : predicate IS nullNotnull
      likePredicate : left=predicate NOT? LIKE right=predicate
      regexpPredicate : left=predicate REGEXP right=predicate
     */

    /**
     * binaryComparisonPredicate : left=predicate comparisonOperator right=predicate
     * left=predicate :  expressionAtomPredicate ｜binaryComparisonPredicate ｜isNullPredicate ｜likePredicate ｜regexpPredicate
     * comparisonOperator : '=' | '>' | '<' | '<' '=' | '>' '=' | '!' '='
     * right=predicate:  expressionAtomPredicate ｜binaryComparisonPredicate ｜isNullPredicate ｜likePredicate ｜regexpPredicate
     */
    @Override
    public UnresolvedExpression visitBinaryComparisonPredicate(BinaryComparisonPredicateContext ctx) {
        String functionName = ctx.comparisonOperator().getText();
        return new Function(functionName, Arrays.asList(visit(ctx.left), visit(ctx.right)));
    }

    /**
     * isNullPredicate : predicate IS nullNotnull
     * predicate : expressionAtomPredicate ｜binaryComparisonPredicate ｜isNullPredicate ｜likePredicate ｜regexpPredicate
     * IS : 'IS'
     * nullNotnull :  (NOT : 'NOT')?(NULL_LITERAL : 'NULL')
     */
    @Override
    public UnresolvedExpression visitIsNullPredicate(IsNullPredicateContext ctx) {
        return new Function(ctx.nullNotnull().NOT() == null ? "is null" : "is not null", Collections.singletonList(visit(ctx.predicate())));
    }

    /**
     * likePredicate : left=predicate NOT? LIKE right=predicate
     * left=predicate : expressionAtomPredicate ｜binaryComparisonPredicate ｜isNullPredicate ｜likePredicate ｜regexpPredicate
     * NOT? LIKE : (NOT : 'NOT')? (LIKE : 'LIKE')
     * left=predicate : expressionAtomPredicate ｜binaryComparisonPredicate ｜isNullPredicate ｜likePredicate ｜regexpPredicate
     */
    @Override
    public UnresolvedExpression visitLikePredicate(LikePredicateContext ctx) {
        return new Function(ctx.NOT() == null ? "like" : "not like", Arrays.asList(visit(ctx.left), visit(ctx.right)));
    }

    /**
     * regexpPredicate : left=predicate REGEXP right=predicate
     * left=predicate : expressionAtomPredicate ｜binaryComparisonPredicate ｜isNullPredicate ｜likePredicate ｜regexpPredicate
     * REGEXP : 'REGEXP'
     * left=predicate : expressionAtomPredicate ｜binaryComparisonPredicate ｜isNullPredicate ｜likePredicate ｜regexpPredicate
     */
    @Override
    public UnresolvedExpression visitRegexpPredicate(RegexpPredicateContext ctx) {
        return new Function("regexp", Arrays.asList(visit(ctx.left), visit(ctx.right)));
    }

    /*
      expressionAtom  : constantExpressionAtom | fullColumnNameExpressionAtom  | functionCallExpressionAtom  | nestedExpressionAtom  | mathExpressionAtom
      constantExpressionAtom(defined type) ：nullLiteral(null)  |  intervalLiteral (interval) |  datetimeLiteral (datetime) [dateLiteral | timeLiteral | timestampLiteral] |  booleanLiteral(boolean) |  sign? realLiteral(signedReal) | sign? decimalLiteral(signedDecimal) | stringLiteral(string)
      fullColumnNameExpressionAtom ：columnName
      functionCallExpressionAtom ：functionCall
      nestedExpressionAtom ：LR_BRACKET expression RR_BRACKET
      mathExpressionAtom ：left=expressionAtom mathOperator right=expressionAtom
     */

    /*
      constant(defined type) :  null | interval | datetime |  boolean |  signedReal | signedDecimal | string
      null : nullLiteral
      interval : intervalLiteral
      datetime : datetimeLiteral
      boolean : booleanLiteral
      signedReal :  sign? realLiteral
      signedDecimal : sign? decimalLiteral
      string : stringLiteral
     */

    /**
     * null : nullLiteral
     * nullLiteral : 'NULL'
     */
    @Override
    public UnresolvedExpression visitNullLiteral(NullLiteralContext ctx) {
        return AstDSL.nullLiteral();
    }

    /**
     * interval : intervalLiteral
     * intervalLiteral : INTERVAL expression intervalUnit
     * INTERVAL : 'INTERVAL'
     * expression : NOT expression | left=expression AND right=expression | left=expression OR right=expression | predicate
     * intervalUnit : MICROSECOND | SECOND | MINUTE | HOUR | DAY | WEEK | MONTH | QUARTER | YEAR | SECOND_MICROSECOND
     * | MINUTE_MICROSECOND | MINUTE_SECOND | HOUR_MICROSECOND | HOUR_SECOND | HOUR_MINUTE | DAY_MICROSECOND
     * | DAY_SECOND | DAY_MINUTE | DAY_HOUR | YEAR_MONTH
     */
    @Override
    public UnresolvedExpression visitIntervalLiteral(IntervalLiteralContext ctx) {
        return new Interval(visit(ctx.expression()), IntervalUnit.of(ctx.intervalUnit().getText()));
    }

    /**
     * datetime : (datetimeLiteral : dateLiteral | timeLiteral | timestampLiteral)
     * dateLiteral : DATE date=stringLiteral
     * DATE : 'DATE'
     * date=stringLiteral : STRING_LITERAL | DOUBLE_QUOTE_ID
     * STRING_LITERAL : (SQUOTA_STRING : '\'' ('\\'. | '\'\'' | ~('\'' | '\\'))* '\'')
     * DOUBLE_QUOTE_ID : (DOUBLE_QUOTE_ID: (DQUOTA_STRING :  '"' ( '\\'. | '""' | ~('"'| '\\') )* '"'))
     */
    @Override
    public UnresolvedExpression visitDateLiteral(DateLiteralContext ctx) {
        return AstDSL.dateLiteral(StringUtils.unquoteText(ctx.date.getText()));
    }

    /**
     * datetime : (datetimeLiteral : dateLiteral | timeLiteral | timestampLiteral)
     * timeLiteral : TIME time=stringLiteral
     * TIME : 'TIME'
     * time=stringLiteral : STRING_LITERAL | DOUBLE_QUOTE_ID
     * STRING_LITERAL : (SQUOTA_STRING : '\'' ('\\'. | '\'\'' | ~('\'' | '\\'))* '\'')
     * DOUBLE_QUOTE_ID : (DOUBLE_QUOTE_ID: (DQUOTA_STRING :  '"' ( '\\'. | '""' | ~('"'| '\\') )* '"'))
     */
    @Override
    public UnresolvedExpression visitTimeLiteral(TimeLiteralContext ctx) {
        return AstDSL.timeLiteral(StringUtils.unquoteText(ctx.time.getText()));
    }

    /**
     * datetime : (datetimeLiteral : dateLiteral | timeLiteral | timestampLiteral)
     * timeLiteral : TIME time=stringLiteral
     * TIME : 'TIME'
     * time=stringLiteral : STRING_LITERAL | DOUBLE_QUOTE_ID
     * STRING_LITERAL : (SQUOTA_STRING : '\'' ('\\'. | '\'\'' | ~('\'' | '\\'))* '\'')
     * DOUBLE_QUOTE_ID : (DOUBLE_QUOTE_ID: (DQUOTA_STRING :  '"' ( '\\'. | '""' | ~('"'| '\\') )* '"'))
     */
    @Override
    public UnresolvedExpression visitTimestampLiteral(TimestampLiteralContext ctx) {
        return AstDSL.timestampLiteral(StringUtils.unquoteText(ctx.timestamp.getText()));
    }

    /**
     * booleanLiteral : TRUE | FALSE
     * TRUE : 'TRUE'
     * FALSE :   'FALSE'
     */
    @Override
    public UnresolvedExpression visitBoolean(BooleanContext ctx) {
        return AstDSL.booleanLiteral(Boolean.parseBoolean(ctx.getText()));
    }

    /**
     * signedDecimal :  sign? realLiteral
     * sign? :  (PLUS : '+') | (MINUS : '-')?
     * realLiteral : REAL_LITERAL
     * REAL_LITERAL :  (DEC_DIGIT+)? '.' DEC_DIGIT+
     * | DEC_DIGIT+ '.' EXPONENT_NUM_PART
     * | (DEC_DIGIT+)? '.' (DEC_DIGIT+ EXPONENT_NUM_PART)
     * | DEC_DIGIT+ EXPONENT_NUM_PART
     * (DEC_DIGIT+)? '.' DEC_DIGIT+ : ([0-9]+) ? '.'[0-9]+
     * DEC_DIGIT+ '.' EXPONENT_NUM_PART :  [0-9]+'.' 'E' [-+]? [0-9]+
     * (DEC_DIGIT+)? '.' (DEC_DIGIT+ EXPONENT_NUM_PART) : ([0-9]+) ? '.'([0-9]+'E' [-+]? [0-9]+)?
     * DEC_DIGIT+ EXPONENT_NUM_PART : [0-9]+'E' [-+]? [0-9]+
     */
    @Override
    public UnresolvedExpression visitSignedReal(SignedRealContext ctx) {
        return AstDSL.doubleLiteral(Double.parseDouble(ctx.getText()));
    }

    /**
     * signedDecimal : sign? decimalLiteral
     * sign? : (PLUS : '+') | (MINUS : '-')?
     * decimalLiteral : DECIMAL_LITERAL | ZERO_DECIMAL | ONE_DECIMAL | TWO_DECIMAL
     * DECIMAL_LITERAL : (DEC_DIGIT+ : [0-9]+)
     * ZERO_DECIMAL : '0'
     * ONE_DECIMAL : '1'
     * TWO_DECIMAL : '2'
     */
    @Override
    public UnresolvedExpression visitSignedDecimal(SignedDecimalContext ctx) {
        return AstDSL.intLiteral(Integer.valueOf(ctx.getText()));
    }

    /**
     * stringLiteral : STRING_LITERAL| DOUBLE_QUOTE_ID
     * STRING_LITERAL : SQUOTA_STRING
     * SQUOTA_STRING : '\'' ('\\'. | '\'\'' | ~('\'' | '\\'))* '\''
     * DOUBLE_QUOTE_ID : DQUOTA_STRING
     * DQUOTA_STRING : '"' ( '\\'. | '""' | ~('"'| '\\') )* '"'
     */
    @Override
    public UnresolvedExpression visitString(StringContext ctx) {
        return AstDSL.stringLiteral(StringUtils.unquoteText(ctx.getText()));
    }

    /**
     * nestedExpressionAtom :   LR_BRACKET expression RR_BRACKET
     * LR_BRACKET : '('
     * expression : NOT expression | left=expression AND right=expression | left=expression OR right=expressio | predicate
     * RR_BRACKET :  ')'
     */
    @Override
    public UnresolvedExpression visitNestedExpressionAtom(NestedExpressionAtomContext ctx) {
        return visit(ctx.expression());
    }

    /**
     * mathExpressionAtom : left=expressionAtom mathOperator right=expressionAtom
     * left=expressionAtom :  constantExpressionAtom | fullColumnNameExpressionAtom  | functionCallExpressionAtom  | nestedExpressionAtom  | mathExpressionAtom
     * mathOperator :   (PLUS : '+') | (MINUS : '-') | (STAR : '*') | (DIVIDE :  '/') | (MODULE : '%')
     * right=expressionAtom :  constantExpressionAtom | fullColumnNameExpressionAtom  | functionCallExpressionAtom  | nestedExpressionAtom  | mathExpressionAtom
     */
    @Override
    public UnresolvedExpression visitMathExpressionAtom(MathExpressionAtomContext ctx) {
        return new Function(ctx.mathOperator().getText(), Arrays.asList(visit(ctx.left), visit(ctx.right)));
    }

    /**
     * columnName : qualifiedName
     */
    @Override
    public UnresolvedExpression visitColumnName(ColumnNameContext ctx) {
        return visit(ctx.qualifiedName());
    }


    /**
     * qualifiedName : ident (DOT ident)*
     * ident : DOT? ID | BACKTICK_QUOTE_ID | keywordsCanBeId
     * DOT : '.'
     * ID : ID_LITERAL
     * ID_LITERAL : [@*A-Z]+?[*A-Z_\-0-9]*
     * BACKTICK_QUOTE_ID : BQUOTA_STRING
     * BQUOTA_STRING : '`' ( '\\'. | '``' | ~('`'|'\\'))* '`'
     * keywordsCanBeId : (FULL : 'FULL')｜(FIELD : 'FIELD')｜(D : 'D')｜(T : 'T')｜(TS : 'TS') // OD SQL and ODBC special | (COUNT : 'COUNT') | (SUM : 'SUM') | (AVG : 'AVG') | (MAX : 'MAX') | (MIN : 'MIN') | (TIMESTAMP : 'TIMESTAMP') | (DATE : 'DATE')| (TIME : 'TIME') | (DAYOFWEEK : 'DAYOFWEEK') | (FIRST : 'FIRST') | (LAST : 'LAST')
     */
    @Override
    public UnresolvedExpression visitQualifiedName(QualifiedNameContext ctx) {
        return visitIdentifiers(ctx.ident());
    }


    /**
     * visitQualifiedName 具体实际操作类 创建QualifiedName 实体类
     *
     * @param identifiers List<IdentContext>
     * @return QualifiedName
     */
    private QualifiedName visitIdentifiers(List<IdentContext> identifiers) {
        return new QualifiedName(
                identifiers.stream()
                        .map(RuleContext::getText)
                        .map(StringUtils::unquoteIdentifier)
                        .collect(Collectors.toList())
        );
    }
}
