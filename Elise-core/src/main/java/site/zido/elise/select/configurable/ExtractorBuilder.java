package site.zido.elise.select.configurable;

import site.zido.elise.select.LinkSelector;
import site.zido.elise.select.NullElementSelector;
import site.zido.elise.select.Selector;
import site.zido.elise.utils.IdWorker;

import java.util.ArrayList;
import java.util.List;

public class ExtractorBuilder {
    private List<LinkSelector> targetUrlSelectors = new ArrayList<>();
    private List<LinkSelector> helpUrlSelectors = new ArrayList<>();
    private String name;
    private Selector sourceSelector;
    private List<FieldExtractor> fieldExtractors = new ArrayList<>();

    protected ExtractorBuilder(String name) {
        this.name = name;
    }

    protected ExtractorBuilder() {
        this.name = IdWorker.nextId() + "";
    }

    public static ExtractorBuilder create() {
        return new ExtractorBuilder();
    }

    public static ExtractorBuilder create(String taskName) {
        return new ExtractorBuilder(taskName);
    }

    public ExtractorBuilder addTargetUrl(LinkSelector selector) {
        targetUrlSelectors.add(selector);
        return this;
    }

    public ExtractorBuilder addHelpUrl(LinkSelector selector) {
        helpUrlSelectors.add(selector);
        return this;
    }

    public ExtractorBuilder source(Selector sourceSelector) {
        this.sourceSelector = sourceSelector;
        return this;
    }

    public ExtractorBuilder addField(FieldExtractor field) {
        this.fieldExtractors.add(field);
        return this;
    }

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
            fieldExtractor.setSelector(new NullElementSelector());
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
