package com.hnqc.ironhand.spider.distributed.configurable;

/**
 * DefRootExtractor
 *
 * @author zido
 * @date 2018/04/12
 */
public class DefRootExtractor extends DefExtractor {
    /**
     * 目标url
     */
    private ConfigurableUrlFinder targetUrl;

    /**
     * 辅助url
     */
    private ConfigurableUrlFinder helpUrl;

    /**
     * 选择抽取范围
     */
    private Extractor.Source source;

    public Extractor.Source getSource() {
        return source;
    }

    public DefExtractor setSource(Extractor.Source source) {
        this.source = source;
        return this;
    }

    public ConfigurableUrlFinder getTargetUrl() {
        return targetUrl;
    }

    public DefRootExtractor setTargetUrl(ConfigurableUrlFinder targetUrl) {
        this.targetUrl = targetUrl;
        return this;
    }

    public ConfigurableUrlFinder getHelpUrl() {
        return helpUrl;
    }

    public DefRootExtractor setHelpUrl(ConfigurableUrlFinder helpUrl) {
        this.helpUrl = helpUrl;
        return this;
    }
}
