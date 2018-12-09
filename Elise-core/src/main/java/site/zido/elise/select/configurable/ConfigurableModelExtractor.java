package site.zido.elise.select.configurable;

import site.zido.elise.select.AbstractModelExtractor;
import site.zido.elise.select.LinkSelector;
import site.zido.elise.select.Selector;

import java.util.List;

/**
 * configurable DefaultResponse model extractor
 *
 * @author zido
 */
public class ConfigurableModelExtractor extends AbstractModelExtractor {
    private List<LinkSelector> targetUrlSelectors;
    private List<LinkSelector> helpUrlSelectors;
    private String name;
    private Selector sourceSelector;
    private List<FieldExtractor> fieldExtractors;

    /**
     * Instantiates a new Configurable model extractor.
     */
    public ConfigurableModelExtractor() {
    }

    @Override
    public List<LinkSelector> getTargetUrlSelectors() {
        return targetUrlSelectors;
    }

    public void setTargetUrlSelectors(List<LinkSelector> targetUrlSelectors) {
        this.targetUrlSelectors = targetUrlSelectors;
    }

    @Override
    public List<LinkSelector> getHelpUrlSelectors() {
        return helpUrlSelectors;
    }

    @Override
    protected List<FieldExtractor> getFieldExtractors() {
        return fieldExtractors;
    }

    public void setHelpUrlSelectors(List<LinkSelector> helpUrlSelectors) {
        this.helpUrlSelectors = helpUrlSelectors;
    }

    @Override
    public String getName() {
        return name;
    }

    public ConfigurableModelExtractor setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Selector getSourceSelector() {
        return sourceSelector;
    }

    public void setSourceSelector(Selector sourceSelector) {
        this.sourceSelector = sourceSelector;
    }

    public void setFieldExtractors(List<FieldExtractor> fieldExtractors) {
        this.fieldExtractors = fieldExtractors;
    }


}
