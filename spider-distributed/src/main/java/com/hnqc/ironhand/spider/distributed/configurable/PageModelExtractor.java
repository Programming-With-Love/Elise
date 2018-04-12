package com.hnqc.ironhand.spider.distributed.configurable;

import com.hnqc.ironhand.spider.selector.Selector;
import com.hnqc.ironhand.spider.selector.Selectors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * PageModelExtractor
 *
 * @author zido
 * @date 2018/04/12
 */
public class PageModelExtractor {
    private List<Pattern> targetUrlPatterns = new ArrayList<>();

    private Selector targetUrlRegionSelector;

    private List<Pattern> helpUrlPatterns = new ArrayList<>();

    private Selector helpUrlRegionSelector;

    private Extractor modelExtractor;

    public PageModelExtractor(DefRootExtractor modelExtractor) {
        init(modelExtractor);
    }

    protected void init(DefRootExtractor defRootExtractor) {
        ConfigurableUrlFinder targetUrlFinder = defRootExtractor.getTargetUrl();
        if (targetUrlFinder == null) {
            //TODO init page
        } else {
            ConfigurableUrlFinder.Type type = targetUrlFinder.getType();

        }


        ExpressionType type = defRootExtractor.getType();
        Selector selector;
        switch (type) {
            //根路径仅支持xpath
            case XPATH:
            default:
                selector = Selectors.xpath(defRootExtractor.getValue());
        }

        modelExtractor = new Extractor(defRootExtractor.getName(),
                selector,
                defRootExtractor.getSource(),
                //根路径不能为空，即使配置也无效
                false,
                defRootExtractor.getMulti());


    }
}
