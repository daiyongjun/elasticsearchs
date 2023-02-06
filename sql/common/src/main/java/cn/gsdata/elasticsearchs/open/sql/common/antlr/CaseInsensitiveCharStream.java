package cn.gsdata.elasticsearchs.open.sql.common.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Interval;

/**
 * 基于代理模式增强CharStream，将小写的char 转化为大写的Char
 *
 * @author daiyongjun
 */
public class CaseInsensitiveCharStream implements CharStream {
    /**
     * 需要代理CharStream实例
     */
    private final CharStream charStream;

    public CaseInsensitiveCharStream(String sql) {
        this.charStream = CharStreams.fromString(sql);
    }

    @Override
    public String getText(Interval interval) {
        return charStream.getText(interval);
    }

    @Override
    public void consume() {
        charStream.consume();
    }

    @Override
    public int LA(int i) {
        int c = charStream.LA(i);
        if (c <= 0) {
            return c;
        }
        return Character.toUpperCase(c);
    }

    @Override
    public int mark() {
        return charStream.mark();
    }

    @Override
    public void release(int marker) {
        charStream.release(marker);
    }

    @Override
    public int index() {
        return charStream.index();
    }

    @Override
    public void seek(int index) {
        charStream.seek(index);
    }

    @Override
    public int size() {
        return charStream.size();
    }

    @Override
    public String getSourceName() {
        return charStream.getSourceName();
    }
}
