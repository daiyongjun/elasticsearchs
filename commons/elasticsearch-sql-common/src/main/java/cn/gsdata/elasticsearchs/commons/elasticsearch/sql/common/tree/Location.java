package cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common.tree;

/**
 * 当前信息
 *
 * @author daiyongjun
 */
public class Location {
    private final int line;
    private final int charPositionInLine;
    public static final Location EMPTY = new Location(-1, -2);

    public Location(int line, int charPositionInLine) {
        this.line = line;
        this.charPositionInLine = charPositionInLine;
    }

    public int getLineNumber() {
        return line;
    }

    public int getColumnNumber() {
        return charPositionInLine + 1;
    }
}
