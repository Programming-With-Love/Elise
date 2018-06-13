package site.zido.elise.configurable;

import site.zido.elise.selector.Selector;

/**
 * 内容抓取器
 *
 * @author zido
 */
public class Extractor {
    /**
     * 抽取范围
     *
     * @author zido
     */
    public enum Source {
        /**
         * 从已选择的html内容中抽取
         */
        SELECTED_HTML,
        /**
         * 从原html中抽取
         */
        RAW_HTML,
        /**
         * 从url中抽取
         */
        URL,
        /**
         * 从文本中抽取
         */
        RAW_TEXT
    }

    private String name;

    private Selector selector;

    private final Source source;

    private final boolean nullable;

    private final boolean multi;

    public Extractor(String name, Selector selector, Source source, boolean nullable, boolean multi) {
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

    public boolean getNullable() {
        return nullable;
    }

    public boolean getMulti() {
        return multi;
    }

    public String getName() {
        return name;
    }
}
