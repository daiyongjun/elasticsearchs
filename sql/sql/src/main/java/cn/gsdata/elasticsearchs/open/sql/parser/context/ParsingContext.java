package cn.gsdata.elasticsearchs.open.sql.parser.context;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * SQL解析上下文，内建QuerySpecification队列
 *
 * @author daiyongjun
 */
public class ParsingContext {
    /**
     * 代理模式，代理Deque<QuerySpecification> 并分别增加push(推) peek(返回开头元素) pop(返回开头元素)等方法。
     */
    private final Deque<QuerySpecification> contexts = new ArrayDeque<>();

    /**
     * 将参数推到队列的末尾
     */
    public void push() {
        contexts.push(new QuerySpecification());
    }

    /**
     * 返回队列开头的元素，不删除元素
     *
     * @return QuerySpecification
     */
    public QuerySpecification peek() {
        return contexts.peek();
    }

    /**
     * 从队列的开头移除并返回元素
     *
     * @return QuerySpecification
     */
    public QuerySpecification pop() {
        return contexts.pop();
    }

}


