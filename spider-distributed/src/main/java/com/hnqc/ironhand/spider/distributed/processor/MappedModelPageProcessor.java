package com.hnqc.ironhand.spider.distributed.processor;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Site;
import com.hnqc.ironhand.spider.distributed.configurable.Extractor;
import com.hnqc.ironhand.spider.distributed.configurable.PageModelExtractor;
import com.hnqc.ironhand.spider.distributed.selector.UrlFinderSelector;
import com.hnqc.ironhand.spider.processor.PageProcessor;
import com.hnqc.ironhand.spider.selector.Selectable;
import com.hnqc.ironhand.spider.selector.Selector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模型处理器
 *
 * @author zido
 * @date 2018/04/12
 */
public class MappedModelPageProcessor implements PageProcessor {
    private Site site;
    private PageModelExtractor extractor;
    private boolean extractLinks = true;

    public MappedModelPageProcessor(Site site, PageModelExtractor extractor) {
        this.site = site;
        this.extractor = extractor;
    }

    protected Object transfer(Object obj) {
        return obj;
    }

    @Override
    public void process(Page page) {
        if (extractLinks) {
            extractLinks(page, extractor.getHelpUrlSelectors());
            extractLinks(page, extractor.getTargetUrlSelectors());
        }
        Object result = extractor.extractPage(page);
        if (result == null) {
            return;
        }
        if ((result instanceof List && ((List) result).size() == 0)) {
            return;
        }
        page.putField(extractor.getModelExtractor().getName(), transfer(result));
        if (page.getResultItems().getAll().size() == 0) {
            page.getResultItems().setSkip(true);
        }
    }

    private void extractLinks(Page page, List<UrlFinderSelector> selectors) {
        List<String> links;
        if (selectors == null) {
            links = page.getHtml().links().all();
        } else {
            links = new ArrayList<>();
            for (UrlFinderSelector selector : selectors) {
                links.addAll(page.getHtml().selectList(selector).all());
            }
        }
        page.addTargetRequests(links);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void setExtractLinks(boolean extractLinks) {
        this.extractLinks = extractLinks;
    }
}
