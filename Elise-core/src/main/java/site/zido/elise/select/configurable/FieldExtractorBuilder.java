package site.zido.elise.select.configurable;

import site.zido.elise.select.CssSelector;
import site.zido.elise.select.RegexSelector;
import site.zido.elise.select.Selector;
import site.zido.elise.select.XpathSelector;
import site.zido.elise.utils.Asserts;

public class FieldExtractorBuilder {
    /**
     * 字段名
     */
    private String name;
    /**
     * 是否允许空 ,默认不允许为空
     */
    private Boolean nullable = false;

    /**
     * 选择抽取范围
     */
    private Source source;

    /**
     * 抓取器
     */
    private Selector selector;

    protected FieldExtractorBuilder(String name) {
        this.name = name;
    }

    public static FieldExtractorBuilder create(String name) {
        Asserts.hasLength(name);
        return new FieldExtractorBuilder(name);
    }

    public FieldExtractorBuilder from(Source source) {
        this.source = source;
        return this;
    }

    public FieldExtractorBuilder xpath(String xpathExpress) {
        this.selector = new XpathSelector(xpathExpress);
        return this;
    }

    public FieldExtractorBuilder css(String cssExpress) {
        this.selector = new CssSelector(cssExpress);
        return this;
    }

    public FieldExtractorBuilder regex(String regex) {
        this.selector = new RegexSelector(regex);
        return this;
    }

    public FieldExtractorBuilder regex(String regex, int group) {
        this.selector = new RegexSelector(regex, group);
        return this;
    }

    public FieldExtractorBuilder regex(String regex, int group, int flags) {
        this.selector = new RegexSelector(regex, group, flags);
        return this;
    }

    public FieldExtractorBuilder setNullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public FieldExtractor build() {
        if (source == null) {
            source = Source.REGION;
        }
        if (selector == null) {
            selector = new XpathSelector("");
        }
        final DefaultFieldExtractor fieldExtractor = new DefaultFieldExtractor();
        fieldExtractor.setName(name);
        fieldExtractor.setSelector(selector);
        fieldExtractor.setNullable(nullable);
        fieldExtractor.setSource(source);
        return fieldExtractor;
    }
}
