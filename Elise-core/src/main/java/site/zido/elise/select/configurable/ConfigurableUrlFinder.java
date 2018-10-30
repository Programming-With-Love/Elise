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

    public ConfigurableUrlFinder() {
    }

    public ConfigurableUrlFinder(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public ConfigurableUrlFinder setValue(String value) {
        this.value = value;
        return this;
    }

    public Type getType() {
        return type;
    }

    public ConfigurableUrlFinder setType(Type type) {
        this.type = type;
        return this;
    }

    public String getSourceRegion() {
        return sourceRegion;
    }

    public ConfigurableUrlFinder setSourceRegion(String sourceRegion) {
        this.sourceRegion = sourceRegion;
        return this;
    }

    public List<LinkProperty> getLinkProperties() {
        return linkProperties;
    }

    public ConfigurableUrlFinder setLinkProperties(List<LinkProperty> linkProperties) {
        this.linkProperties = linkProperties;
        return this;
    }

    public ConfigurableUrlFinder addLinkProperty(LinkProperty... property) {
        this.linkProperties.addAll(Arrays.asList(property));
        return this;
    }

    public enum Type {
        /**
         * 目标url抓取类型，目前仅支持regex抓取，后续可能会自定义规则
         */
        REGEX
    }
}
