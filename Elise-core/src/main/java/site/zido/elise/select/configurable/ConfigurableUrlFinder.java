package site.zido.elise.select.configurable;

import site.zido.elise.select.LinkProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 配置url发现规则
 *
 * @author zido
 */
public class ConfigurableUrlFinder {

    /**
     * 匹配值，目前仅支持regex
     */
    private String value;
    /**
     * 匹配类型，目前仅支持regex
     */
    private Type type = Type.REGEX;
    /**
     * 规定url的选择范围，目前仅支持xpath
     */
    private String sourceRegion;
    private List<LinkProperty> linkProperties = new ArrayList<>();

    /**
     * Instantiates a new Configurable url finder.
     */
    public ConfigurableUrlFinder() {
    }

    /**
     * Instantiates a new Configurable url finder.
     *
     * @param value the value
     */
    public ConfigurableUrlFinder(String value) {
        this.value = value;
        this.linkProperties = new ArrayList<>();
        this.linkProperties.add(new LinkProperty("a", "href"));
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     * @return the value
     */
    public ConfigurableUrlFinder setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     * @return the type
     */
    public ConfigurableUrlFinder setType(Type type) {
        this.type = type;
        return this;
    }

    /**
     * Gets source region.
     *
     * @return the source region
     */
    public String getSourceRegion() {
        return sourceRegion;
    }

    /**
     * Sets source region.
     *
     * @param sourceRegion the source region
     * @return the source region
     */
    public ConfigurableUrlFinder setSourceRegion(String sourceRegion) {
        this.sourceRegion = sourceRegion;
        return this;
    }

    /**
     * Gets link properties.
     *
     * @return the link properties
     */
    public List<LinkProperty> getLinkProperties() {
        return linkProperties;
    }

    /**
     * Sets link properties.
     *
     * @param linkProperties the link properties
     * @return the link properties
     */
    public ConfigurableUrlFinder setLinkProperties(List<LinkProperty> linkProperties) {
        this.linkProperties = linkProperties;
        return this;
    }

    /**
     * Add link property configurable url finder.
     *
     * @param property the property
     * @return the configurable url finder
     */
    public ConfigurableUrlFinder addLinkProperty(LinkProperty... property) {
        this.linkProperties.addAll(Arrays.asList(property));
        return this;
    }

}
