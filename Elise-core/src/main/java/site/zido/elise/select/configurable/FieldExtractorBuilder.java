package site.zido.elise.select.configurable;

import site.zido.elise.select.CssSelector;
import site.zido.elise.select.RegexSelector;
import site.zido.elise.select.Selector;
import site.zido.elise.select.XpathSelector;
import site.zido.elise.utils.Asserts;

/**
 * The type Field extractor builder.
 *
 * @author zido
 */
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

    /**
     * Instantiates a new Field extractor builder.
     *
     * @param name the name
     */
    protected FieldExtractorBuilder(String name) {
        this.name = name;
    }

    /**
     * Create field extractor builder.
     *
     * @param name the name
     * @return the field extractor builder
     */
    public static FieldExtractorBuilder create(String name) {
        Asserts.hasLength(name);
        return new FieldExtractorBuilder(name);
    }

    /**
     * From field extractor builder.
     *
     * @param source the source
     * @return the field extractor builder
     */
    public FieldExtractorBuilder from(Source source) {
        this.source = source;
        return this;
    }

    /**
     * Xpath field extractor builder.
     *
     * @param xpathExpress the xpath express
     * @return the field extractor builder
     */
    public FieldExtractorBuilder xpath(String xpathExpress) {
        this.selector = new XpathSelector(xpathExpress);
        return this;
    }

    /**
     * Css field extractor builder.
     *
     * @param cssExpress the css express
     * @return the field extractor builder
     */
    public FieldExtractorBuilder css(String cssExpress) {
        this.selector = new CssSelector(cssExpress);
        return this;
    }

    /**
     * Regex field extractor builder.
     *
     * @param regex the regex
     * @return the field extractor builder
     */
    public FieldExtractorBuilder regex(String regex) {
        this.selector = new RegexSelector(regex);
        return this;
    }

    /**
     * Regex field extractor builder.
     *
     * @param regex the regex
     * @param group the group
     * @return the field extractor builder
     */
    public FieldExtractorBuilder regex(String regex, int group) {
        this.selector = new RegexSelector(regex, group);
        return this;
    }

    /**
     * Regex field extractor builder.
     *
     * @param regex the regex
     * @param group the group
     * @param flags the flags
     * @return the field extractor builder
     */
    public FieldExtractorBuilder regex(String regex, int group, int flags) {
        this.selector = new RegexSelector(regex, group, flags);
        return this;
    }

    /**
     * Sets nullable.
     *
     * @param nullable the nullable
     * @return the nullable
     */
    public FieldExtractorBuilder setNullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    /**
     * Build field extractor.
     *
     * @return the field extractor
     */
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
