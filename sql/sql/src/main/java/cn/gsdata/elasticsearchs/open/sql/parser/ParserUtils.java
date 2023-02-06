package cn.gsdata.elasticsearchs.open.sql.parser;

import cn.gsdata.elasticsearchs.open.sql.core.ast.tree.Sort.*;
import gen.OpenDistroSQLParser.*;
import lombok.experimental.UtilityClass;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * 解析工具类
 *
 * @author daiyongjun
 */
@UtilityClass
public class ParserUtils {
    /**
     * 当前node节点的位置信息从Query语句中提取
     *
     * @param ctx         当前的node节点
     * @param queryString SQL Query语句
     * @return String  当前的node节点的value值
     */
    public static String getTextInQuery(ParserRuleContext ctx, String queryString) {
        Token start = ctx.getStart();
        Token stop = ctx.getStop();
        return queryString.substring(start.getStartIndex(), stop.getStopIndex() + 1);
    }

    /**
     * 基于Context创建SortOption实例
     */
    public static SortOption createSortOption(OrderByElementContext orderBy) {
        return new SortOption(
                createSortOrder(orderBy.order),
                createNullOrder(orderBy.FIRST(), orderBy.LAST()));
    }

    /**
     * 基于token ctx 创建SortOrder
     */
    public static SortOrder createSortOrder(Token ctx) {
        if (ctx == null) {
            return null;
        }
        return SortOrder.valueOf(ctx.getText().toUpperCase());
    }

    /**
     * 基于orderBy.FIRST() 和 基于orderBy.LAST()创建NullOrder实例
     */
    public static NullOrder createNullOrder(TerminalNode first, TerminalNode last) {
        if (first != null) {
            return NullOrder.NULL_FIRST;
        } else if (last != null) {
            return NullOrder.NULL_LAST;
        } else {
            return null;
        }
    }
}
