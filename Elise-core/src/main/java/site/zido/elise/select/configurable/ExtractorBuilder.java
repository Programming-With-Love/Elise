package site.zido.elise.select.configurable;

import site.zido.elise.select.*;
import site.zido.elise.utils.IdWorker;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Extractor builder.
 *
 * @author zido
 */
public class ExtractorBuilder {
    private List<LinkSelector> targetUrlSelectors = new ArrayList<>();
    private List<LinkSelector> helpUrlSelectors = new ArrayList<>();
    private String name;
    private ElementSelector sourceSelector;
    private List<FieldExtractor> fieldExtractors = new ArrayList<>();

    /**
     * Instantiates a new Extractor builder.
     *
     * @param name the name
     */
    protected ExtractorBuilder(String name) {
        this.name = name;
    }

    /**
     * Instantiates a new Extractor builder.
     */
    protected ExtractorBuilder() {
        this.name = IdWorker.nextId() + "";
    }

    /**
     * Create extractor builder.
     *
     * @return the extractor builder
     */
    public static ExtractorBuilder create() {
        return new ExtractorBuilder();
    }

    /**
     * Create extractor builder.
     *
     * @param taskName the task name
     * @return the extractor builder
     */
    public static ExtractorBuilder create(String taskName) {
        return new ExtractorBuilder(taskName);
    }

    /**
     * Add target url extractor builder.
     *
     * @param selector the selector
     * @return the extractor builder
     */
    public ExtractorBuilder addTargetUrl(LinkSelector selector) {
        targetUrlSelectors.add(selector);
        return this;
    }

    /**
     * Add help url extractor builder.
     *
     * @param selector the selector
     * @return the extractor builder
     */
    public ExtractorBuilder addHelpUrl(LinkSelector selector) {
        helpUrlSelectors.add(selector);
        return this;
    }

    /**
     * Source extractor builder.
     *
     * @param sourceSelector the source selector
     * @return the extractor builder
     */
    public ExtractorBuilder source(ElementSelector sourceSelector) {
        this.sourceSelector = sourceSelector;
        return this;
    }

    /**
     * Add field extractor builder.
     *
     * @param field the field
     * @return the extractor builder
     */
    public ExtractorBuilder addField(FieldExtractor field) {
        this.fieldExtractors.add(field);
        return this;
    }

    /**
     * Build model extractor.
     *
     * @return the model extractor
     */
    public ModelExtractor build() {
        final ConfigurableModelExtractor extractor = new ConfigurableModelExtractor();
        if (sourceSelector == null) {
            sourceSelector = new NullElementSelector();
        }
        if (fieldExtractors.size() == 0) {
            final DefaultFieldExtractor fieldExtractor = new DefaultFieldExtractor();
            fieldExtractor.setName("html");
            fieldExtractor.setNullable(false);
            fieldExtractor.setSource(Source.RAW_HTML);
            fieldExtractor.setSelector(new NullSelector());
            fieldExtractors.add(fieldExtractor);
        }
        extractor.setFieldExtractors(fieldExtractors);
        extractor.setHelpUrlSelectors(helpUrlSelectors);
        extractor.setName(name);
        extractor.setSourceSelector(sourceSelector);
        extractor.setTargetUrlSelectors(targetUrlSelectors);
        return extractor;
    }
}
