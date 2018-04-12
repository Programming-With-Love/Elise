package com.hnqc.ironhand.spider.distributed.configurable;

import com.hnqc.ironhand.spider.selector.Selector;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * Extractor
 *
 * @author zido
 * @date 2018/04/12
 */
public class Extractor {
    /**
     * 抽取范围枚举
     *
     * @author zido
     * @date 2018/30/12
     */
    public enum Source {
        /**
         * 从已选择的html内容中抽取
         */
        SELECTED_HTML,
        /**
         * 从原html中抽取
         */
        ROW_HTML,
        /**
         * 从url中抽取
         */
        URL,
        /**
         * 从文本中抽取（少用）
         */
        RWO_TEXT
    }

    private String name;

    private Selector selector;

    private final Source source;

    private final Boolean nullable;

    private final Boolean multi;

    public Extractor(String name, Selector selector, Source source, Boolean nullable, Boolean multi) {
        this.name = name;
        this.selector = selector;
        this.source = source;
        this.nullable = nullable;
        this.multi = multi;
    }

    public Selector getSelector() {
        return selector;
    }

    public Source getSource() {
        return source;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public Boolean getMulti() {
        return multi;
    }

    public String getName() {
        return name;
    }
}
