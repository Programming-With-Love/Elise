package com.hnqc.ironhand.spider.distributed.processor;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Site;
import com.hnqc.ironhand.spider.distributed.configurable.Extractor;
import com.hnqc.ironhand.spider.processor.PageProcessor;
import com.hnqc.ironhand.spider.selector.Selector;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AbstractModelPageProcessor
 *
 * @author zido
 * @date 2018/04/12
 */
public abstract class AbstractModelPageProcessor implements PageProcessor {
    private Site site;
    private List<Extractor> extractors;
    private boolean extractLinks = true;

    public AbstractModelPageProcessor(Site site, List<Extractor> extractors) {
        this.site = site;
        this.extractors = extractors;
    }

    @Override
    public void process(Page page) {
        for (Extractor extractor : extractors) {
            if (extractLinks) {
                //TODO 完善model page processor
//                extractLinks(page, extractor.getHelpUrlRegionSelector(), pageModelExtractor.getHelpUrlPatterns());
//                extractLinks(page, pageModelExtractor.getTargetUrlRegionSelector(), pageModelExtractor.getTargetUrlPatterns());
            }
        }
    }

    private void extractLinks(Page page, Selector urlRegionSelector, List<Pattern> urlPatterns) {
        List<String> links;
        if (urlRegionSelector == null) {
            links = page.getHtml().links().all();
        } else {
            links = page.getHtml().selectList(urlRegionSelector).links().all();
        }
        for (String link : links) {
            for (Pattern targetUrlPattern : urlPatterns) {
                Matcher matcher = targetUrlPattern.matcher(link);
                if (matcher.find()) {
                    page.addTargetRequest(new Request(matcher.group(0)));
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
