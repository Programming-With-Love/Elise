package com.hnqc.ironhand.spider.configurable;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Site;
import com.hnqc.ironhand.spider.processor.PageProcessor;

import java.util.List;

public class ConfigurablePageProcessor implements PageProcessor {
    private Site site;

    private List<ExtractRule> extractRules;

    public ConfigurablePageProcessor(Site site, List<ExtractRule> extractRules) {
        this.site = site;
        this.extractRules = extractRules;
    }

    @Override
    public void process(Page page) {
        for (ExtractRule extractRule : extractRules) {
            if (extractRule.isMulti()) {
                List<String> results = page.getHtml().selectDocumentForList(extractRule.getSelector());
                if (extractRule.isNotNull() && results.size() == 0) {
                    page.setSkip(true);
                } else {
                    page.getResultItems().put(extractRule.getFieldName(), results);
                }
            } else {
                String result = page.getHtml().selectDocument(extractRule.getSelector());
                if (extractRule.isNotNull() && result == null) {
                    page.setSkip(true);
                } else {
                    page.getResultItems().put(extractRule.getFieldName(), result);
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public List<ExtractRule> getExtractRules() {
        return extractRules;
    }

    public void setExtractRules(List<ExtractRule> extractRules) {
        this.extractRules = extractRules;
    }
}
