package com.hnqc.ironhand.spider.distributed.configurable;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.distributed.selector.UrlFinderSelector;
import com.hnqc.ironhand.spider.selector.Selectable;
import com.hnqc.ironhand.spider.selector.Selector;
import com.hnqc.ironhand.spider.selector.Selectors;
import com.hnqc.ironhand.spider.utils.ValidateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * PageModelExtractor
 *
 * @author zido
 * @date 2018/04/12
 */
public class PageModelExtractor {

    private List<UrlFinderSelector> targetUrlSelectors = new ArrayList<>();
    private List<UrlFinderSelector> helpUrlSelectors = new ArrayList<>();

    private Extractor modelExtractor;

    public PageModelExtractor(DefRootExtractor rootExtractor) {
        init(rootExtractor);
    }

    protected void init(DefRootExtractor defRootExtractor) {
        //转化配置到具体类
        List<ConfigurableUrlFinder> targetUrlFinder = defRootExtractor.getTargetUrl();
        if (!ValidateUtils.isEmpty(targetUrlFinder)) {
            for (ConfigurableUrlFinder configurableUrlFinder : targetUrlFinder) {
                UrlFinderSelector urlFinderSelector = new UrlFinderSelector(configurableUrlFinder);
                this.targetUrlSelectors.add(urlFinderSelector);
            }
        }
        List<ConfigurableUrlFinder> helpUrlFinder = defRootExtractor.getHelpUrl();
        if (!ValidateUtils.isEmpty(helpUrlFinder)) {
            for (ConfigurableUrlFinder configurableUrlFinder : helpUrlFinder) {
                UrlFinderSelector urlFinderSelector = new UrlFinderSelector(configurableUrlFinder);
                this.helpUrlSelectors.add(urlFinderSelector);
            }
        }

    }

    /**
     * 抓取页面内容，如果未抓取到/结果不匹配，返回null。
     *
     * @param page page
     * @return result map
     */
    public Map<String, Object> extractPage(Page page) {
        //不是目标链接直接返回
        if (targetUrlSelectors.stream().noneMatch(urlFinderSelector -> page.getUrl().select(urlFinderSelector).match())) {
            return null;
        }
        return null;
    }
}
