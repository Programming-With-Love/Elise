package com.hnqc.ironhand.spider.configurable;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.ResultItem;
import com.hnqc.ironhand.spider.selector.UrlFinderSelector;
import com.hnqc.ironhand.spider.extractor.ModelExtractor;
import com.hnqc.ironhand.spider.selector.Selector;
import com.hnqc.ironhand.spider.selector.Selectors;
import com.hnqc.ironhand.spider.utils.ValidateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hnqc.ironhand.spider.selector.Selectors.css;
import static com.hnqc.ironhand.spider.selector.Selectors.regex;

/**
 * configurable Page model extractor
 *
 * @author zido
 * @date 2018/04/12
 */
public class ConfigurableModelExtractor implements ModelExtractor {

    private List<UrlFinderSelector> targetUrlSelectors = new ArrayList<>();

    private List<UrlFinderSelector> helpUrlSelectors = new ArrayList<>();

    private Extractor modelExtractor;

    private List<Extractor> fieldExtractors;

    private DefRootExtractor defRootExtractor;

    /**
     * 初始化基本数据
     *
     * @param defRootExtractor 抓取器描述
     */
    public ConfigurableModelExtractor(DefRootExtractor defRootExtractor) {
        this.defRootExtractor = defRootExtractor;
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
        modelExtractor = new Extractor(defRootExtractor.getName(),
                Selectors.xpath(defRootExtractor.getValue()),
                defRootExtractor.getSource(),
                defRootExtractor.getNullable(),
                defRootExtractor.getMulti());
        this.fieldExtractors = defRootExtractor.getChildren().stream().map(defExtractor -> {
            Extractor.Source source = defExtractor.getSource();
            Selector selector = defExtractor.compileSelector();
            return new Extractor(defExtractor.getName(),
                    selector,
                    source,
                    defExtractor.getNullable(),
                    defExtractor.getMulti());
        }).collect(Collectors.toList());

    }

    @Override
    public ResultItem extract(Page page) {
        ResultItem resultItem = new ResultItem();
        if (multi()) {
            List<Map<String, Object>> list = extractPageForList(page);
            if (list == null || list.size() == 0) {
                resultItem.setSkip(true);
            }
            {
                resultItem.put(modelExtractor.getName(), list);
            }
        } else {
            Map<String, Object> obj = extractPageItem(page);
            if (obj == null || obj.size() == 0) {
                resultItem.setSkip(true);
            }
            resultItem.put(modelExtractor.getName(), obj);
        }
        return resultItem;
    }

    @Override
    public List<String> extractLinks(Page page) {
        List<String> links;
        List<UrlFinderSelector> selectors = getHelpUrlSelectors();
        if (selectors == null) {
            links = page.getHtml().links().all();
        } else {
            links = new ArrayList<>();
            for (UrlFinderSelector selector : selectors) {
                links.addAll(page.getHtml().selectList(selector).all());
            }
        }
        return links;
    }

    /**
     * 抓取页面内容，如果未抓取到/结果不匹配，返回null。
     *
     * @param page page
     * @return result map
     */
    public Map<String, Object> extractPageItem(Page page) {
        //不是目标链接直接返回
        if (targetUrlSelectors
                .stream()
                .noneMatch(urlFinderSelector ->
                        page.getUrl()
                                .select(urlFinderSelector)
                                .match())) {
            return null;
        }
        String html = modelExtractor.getSelector().select(page.getRawText());
        return processSingle(page, html);
    }

    /**
     * 抓取页面内容，如果未抓取到/结果不匹配，返回empty list。
     *
     * @param page page
     */
    public List<Map<String, Object>> extractPageForList(Page page) {
        //不是目标链接直接返回
        if (targetUrlSelectors.stream().noneMatch(urlFinderSelector -> page.getUrl()
                .select(urlFinderSelector).match())) {
            return null;
        }
        List<Map<String, Object>> results = new ArrayList<>();
        List<String> list = modelExtractor.getSelector().selectList(page.getRawText());
        for (String html : list) {
            Map<String, Object> result = processSingle(page, html);
            if (result != null) {
                results.add(result);
            }
        }
        return results;
    }

    private Map<String, Object> processSingle(Page page, String html) {
        Map<String, Object> map = new HashMap<>(fieldExtractors.size());
        for (Extractor fieldExtractor : fieldExtractors) {
            if (!fieldExtractor.getMulti()) {
                String result = processField(fieldExtractor, page, html);
                if (result == null && !fieldExtractor.getNullable()) {
                    return null;
                }
                map.put(fieldExtractor.getName(), result);
            } else {
                List<String> results = processFieldForList(fieldExtractor, page, html);
                if (ValidateUtils.isEmpty(results) && !fieldExtractor.getNullable()) {
                    return null;
                }
                map.put(fieldExtractor.getName(), results);
            }
        }
        return map;
    }

    private String processField(Extractor fieldExtractor, Page page, String html) {
        String value;
        switch (fieldExtractor.getSource()) {
            case RAW_HTML:
                value = page.getHtml().selectDocument(fieldExtractor.getSelector());
                break;
            case URL:
                value = fieldExtractor.getSelector().select(page.getUrl().toString());
                break;
            case RAW_TEXT:
                value = fieldExtractor.getSelector().select(page.getRawText());
                break;
            case SELECTED_HTML:
            default:
                value = fieldExtractor.getSelector().select(html);
        }
        return value;
    }

    private List<String> processFieldForList(Extractor fieldExtractor, Page page, String html) {
        List<String> value;
        switch (fieldExtractor.getSource()) {
            case RAW_HTML:
                value = page.getHtml().selectDocumentForList(fieldExtractor.getSelector());
                break;
            case URL:
                value = fieldExtractor.getSelector().selectList(page.getUrl().toString());
                break;
            case RAW_TEXT:
                value = fieldExtractor.getSelector().selectList(page.getRawText());
                break;
            case SELECTED_HTML:
            default:
                value = fieldExtractor.getSelector().selectList(html);
        }
        return value;
    }

    public boolean multi() {
        return modelExtractor.getMulti();
    }

    public List<UrlFinderSelector> getTargetUrlSelectors() {
        return targetUrlSelectors;
    }

    public void setTargetUrlSelectors(List<UrlFinderSelector> targetUrlSelectors) {
        this.targetUrlSelectors = targetUrlSelectors;
    }

    public List<UrlFinderSelector> getHelpUrlSelectors() {
        return helpUrlSelectors;
    }

    public void setHelpUrlSelectors(List<UrlFinderSelector> helpUrlSelectors) {
        this.helpUrlSelectors = helpUrlSelectors;
    }

    public Extractor getModelExtractor() {
        return modelExtractor;
    }

    public void setModelExtractor(Extractor modelExtractor) {
        this.modelExtractor = modelExtractor;
    }

    public List<Extractor> getFieldExtractors() {
        return fieldExtractors;
    }

    public void setFieldExtractors(List<Extractor> fieldExtractors) {
        this.fieldExtractors = fieldExtractors;
    }

    public DefRootExtractor getDefRootExtractor() {
        return defRootExtractor;
    }
}
