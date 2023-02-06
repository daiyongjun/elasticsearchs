package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.xcontent;

import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.ElasticsearchException;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParseField;
import cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.ParsingException;

import java.io.IOException;
import java.util.*;

import static java.util.Collections.*;
import static java.util.Objects.requireNonNull;

/**
 * 正文字段解析注册器
 */
public class NamedXContentRegistry {

    public static final NamedXContentRegistry EMPTY = new NamedXContentRegistry(emptyList());

    private final Map<Class<?>, Map<String, Entry>> registry;

    /**
     * {@linkplain NamedXContentRegistry} 中的一个条目，包含对象的名称和可以解析它的解析器。
     */
    public static class Entry {
        // 此条目可以读取的类。
        public final Class<?> categoryClass;

        // {@link categoryClass} 中唯一的条目名称。
        public final ParseField name;

        // 解析条目类的解析器。
        private final ContextParser<Object, ?> parser;

        /**
         * ·
         * Entry的构造方法
         */
        public <T> Entry(Class<T> categoryClass, ParseField name, ContextParser<Object, ?> parser) {
            this.categoryClass = Objects.requireNonNull(categoryClass);
            this.name = Objects.requireNonNull(name);
            this.parser = Objects.requireNonNull(parser);
        }
    }

    /**
     * NamedXContentRegistry 构造方法
     */
    public NamedXContentRegistry(List<Entry> entries) {
        if (entries.isEmpty()) {
            registry = emptyMap();
            return;
        }
        entries = new ArrayList<>(entries);
        entries.sort((e1, e2) -> e1.categoryClass.getName().compareTo(e2.categoryClass.getName()));

        Map<Class<?>, Map<String, Entry>> registry = new HashMap<>();
        Map<String, Entry> parsers = new HashMap<>();
        Class<?> currentCategory = null;
        for (Entry entry : entries) {
            if (currentCategory != entry.categoryClass) {
                if (currentCategory != null) {
                    // we've seen the last of this category, put it into the big map
                    registry.put(currentCategory, unmodifiableMap(parsers));
                }
                parsers = new HashMap<>();
                currentCategory = entry.categoryClass;
            }

            for (String name : entry.name.getAllNamesIncludedDeprecated()) {
                Object old = parsers.put(name, entry);
                if (old != null) {
                    throw new IllegalArgumentException("NamedXContent [" + currentCategory.getName() + "][" + entry.name + "]" +
                            " is already registered for [" + old.getClass().getName() + "]," +
                            " cannot register [" + entry.parser.getClass().getName() + "]");
                }
            }
        }
        // handle the last category
        registry.put(currentCategory, unmodifiableMap(parsers));

        this.registry = unmodifiableMap(registry);
    }

    /**
     * 使用指定的超类Class和指定的名称，去程序执行之前预定义的的注册器中解析核心方法（fromXContent）将Xcontent转换成Pojo核心处理类
     */
    public <T, C> T parseNamedObject(Class<T> categoryClass, String name, XContentParser parser, C context) throws IOException {
        Map<String, Entry> parsers = registry.get(categoryClass);
        //如果没有找到(parser)解析器则抛出异常。
        if (parsers == null) {
            if (registry.isEmpty()) {
                // The "empty" registry will never work so we throw a better exception as a hint.
                throw new ElasticsearchException("namedObject is not supported for this parser");
            }
            throw new ElasticsearchException("Unknown namedObject category [" + categoryClass.getName() + "]");
        }
        //从注册解析器中获取命名对象指定的解析类
        Entry entry = parsers.get(name);
        if (entry == null) {
            throw new UnknownNamedObjectException(parser.getTokenLocation(), categoryClass, name);
        }
        if (!entry.name.match(name)) {
            throw new ParsingException(parser.getTokenLocation(),
                    "Unknown " + categoryClass.getSimpleName() + " [" + name + "]: Parser didn't match");
        }
        return categoryClass.cast(entry.parser.parse(parser, context));
    }

    /**
     * 构建UnknownNamedObjectException
     */
    public static class UnknownNamedObjectException extends ParsingException {
        //定义缺少解析器的类别类
        private final String categoryClass;
        //缺少解析器的名称
        private final String name;

        public UnknownNamedObjectException(XContentLocation contentLocation, Class<?> categoryClass,
                                           String name) {
            super(contentLocation, "Unknown " + categoryClass.getSimpleName() + " [" + name + "]");
            this.categoryClass = requireNonNull(categoryClass, "categoryClass is required").getName();
            this.name = requireNonNull(name, "name is required");
        }

        /**
         * 解析器的类别类
         */
        public String getCategoryClass() {
            return categoryClass;
        }

        /**
         * 解析器的名称。
         */
        public String getName() {
            return name;
        }
    }

}
