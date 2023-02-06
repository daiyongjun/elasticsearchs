/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 包含在解析时可以在请求中找到的字段及其不同的变体，有些字段可能已经弃用了
 */
public class ParseField {


    private final String name;
    private final String[] deprecatedNames;
    private String allReplacedWith = null;
    private final String[] allNames;

    /**
     * 设置allNames
     *
     * @param name  此字段的主要名称
     * @param deprecatedNames   此字段的名称已被弃用，并且在使用严格匹配时将不被接受。
     */
    public ParseField(String name, String... deprecatedNames) {
        this.name = name;
        if (deprecatedNames == null || deprecatedNames.length == 0) {
            this.deprecatedNames = Strings.EMPTY_ARRAY;
        } else {
            final HashSet<String> set = new HashSet<>();
            Collections.addAll(set, deprecatedNames);
            int length = set.size();
            this.deprecatedNames = set.toArray(new String[length]);
        }
        Set<String> allNames = new HashSet<>();
        allNames.add(name);
        Collections.addAll(allNames, this.deprecatedNames);
        int length = allNames.size();
        this.allNames = allNames.toArray(new String[length]);
    }

    /**
     * @return 当前使用的字段
     */
    public String getPreferredName() {
        return name;
    }

    /**
     * @return 待替换的字段名称
     */
    public String getAllReplacedWith() {
        return allReplacedWith;
    }

    /**
     * @return 弃用字段的名称列表
     */
    public String[] getDeprecatedNames() {
        return deprecatedNames;
    }

    /**
     * @return  获取当前字段所有名称，包括已经弃用的名称
     */
    public String[] getAllNamesIncludedDeprecated() {
        return allNames;
    }

    /**
     * 定义一个弃用名称列表的 解析字段
     */
    public ParseField withDeprecation(String... deprecatedNames) {
        return new ParseField(this.name, deprecatedNames);
    }

    /**
     * @return 一个新的 ParseField ，其中所有字段名称均已弃用和替换
     */
    public ParseField withAllDeprecated(String allReplacedWith) {
        ParseField parseField = this.withDeprecation(getAllNamesIncludedDeprecated());
        parseField.allReplacedWith = allReplacedWith;
        return parseField;
    }

    /**
     *  ParseField 中所有有效字段名称，是否满足待验证字段名称
     *
     * @param fieldName 待验证的字段名称
     * @return 如果 <code>fieldName<code> 匹配此 {@link ParseField} 的任何可接受的名称，则为 true。
     */
    public boolean match(String fieldName) {
        Objects.requireNonNull(fieldName, "fieldName cannot be null");
        // if this parse field has not been completely deprecated then try to
        // match the preferred name
        if (allReplacedWith == null && fieldName.equals(name)) {
            return true;
        }
        // Now try to match against one of the deprecated names. Note that if
        // the parse field is entirely deprecated (allReplacedWith != null) all
        // fields will be in the deprecatedNames array
        String msg;
        for (String depName : deprecatedNames) {
            if (fieldName.equals(depName)) {
                msg = "Deprecated field [" + fieldName + "] used, expected [" + name + "] instead";
                if (allReplacedWith != null) {
                    // If the field is entirely deprecated then there is no
                    // preferred name so instead use the `allReplaceWith`
                    // message to indicate what should be used instead
                    msg = "Deprecated field [" + fieldName + "] used, replaced by [" + allReplacedWith + "]";
                    System.out.println(msg);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return getPreferredName();
    }

//    public static class CommonFields {
//        public static final ParseField FIELD = new ParseField("field");
//        public static final ParseField FIELDS = new ParseField("fields");
//        public static final ParseField FORMAT = new ParseField("format");
//        public static final ParseField MISSING = new ParseField("missing");
//        public static final ParseField TIME_ZONE = new ParseField("time_zone");
//    }
}
