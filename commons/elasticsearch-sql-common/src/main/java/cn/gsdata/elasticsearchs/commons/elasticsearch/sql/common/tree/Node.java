package cn.gsdata.elasticsearchs.commons.elasticsearch.sql.common.tree;

import java.util.List;

/**
 * 数据节点
 *
 * @author daiyongjun
 */
public abstract class Node<T extends Node<T>> {
    /**
     * 当前信息
     */
    private final Location location;
    /**
     * 子节点
     */
    List<T> children;

    public Node(Location location, List<T> children) {
        this.location = (location != null ? location : Location.EMPTY);
        this.children = children;
    }

    public Location location() {
        return location;
    }

    public List<T> children() {
        return children;
    }
}
