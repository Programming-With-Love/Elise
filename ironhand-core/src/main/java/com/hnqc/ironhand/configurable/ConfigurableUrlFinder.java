package com.hnqc.ironhand.configurable;

/**
 * 配置url发现规则
 *
 * @author zido
 * @date 2018/04/12
 */
public class ConfigurableUrlFinder {

    public enum Type {
        /**
         * 目标url抓取类型，目前仅支持regex抓取，后续可能会自定义规则
         */
        REGEX
    }

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
}
