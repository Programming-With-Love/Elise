package site.zido.elise.configurable;

import site.zido.elise.Page;
import site.zido.elise.ResultItem;
import site.zido.elise.extractor.ModelExtractor;
import site.zido.elise.selector.Selector;
import site.zido.elise.selector.Selectors;
import site.zido.elise.selector.UrlFinderSelector;
import site.zido.elise.utils.ValidateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * configurable Page model extractor
 *
 * @author zido
 */
public class ConfigurableModelExtractor implements ModelExtractor {

    private List<UrlFinderSelector> targetUrlSelectors = new ArrayList<>();

    private List<UrlFinderSelector> helpUrlSelectors = new ArrayList<>();

    private Selector selector;

    private DefRootExtractor defRootExtractor;

    private static final String PT = "http";

    private static Logger logger = LoggerFactory.getLogger(ConfigurableModelExtractor.class);

    /**
     * construct by {@link DefRootExtractor}
     *
     * @param defRootExtractor def root extractor
     */
    public ConfigurableModelExtractor(DefRootExtractor defRootExtractor) {
        this.defRootExtractor = defRootExtractor;
        selector = defRootExtractor.compileSelector();
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

    @Override
    public ResultItem extract(Page page) {
        ResultItem resultItem = new ResultItem();
        if (multi()) {
            List<Map<String, Object>> list = extractPageForList(page);
            if (list == null || list.size() == 0) {
                resultItem.setSkip(true);
            }
            resultItem.put(defRootExtractor.getName(), list);
        } else {
            Map<String, Object> obj = extractPageItem(page);
            if (obj == null || obj.size() == 0) {
                resultItem.setSkip(true);
            }
            resultItem.put(defRootExtractor.getName(), obj);
        }
        return resultItem;
    }

    @Override
    public List<String> extractLinks(Page page) {
        List<String> links;

        if (ValidateUtils.isEmpty(helpUrlSelectors)) {
            return new ArrayList<>(0);
        } else {
            links = new ArrayList<>();
            for (UrlFinderSelector selector : helpUrlSelectors) {
                links.addAll(page.html().selectList(selector).all());
            }
            //兜底链接处理
            links = links.stream().map(link -> {
                link = link.replace("&amp;", "&");
                if (link.startsWith(PT)) {
                    //已经是绝对路径的，不再处理
                    return link;
                }
                try {
                    return new URL(new URL(page.getUrl().toString()), link).toString();
                } catch (MalformedURLException e) {
                    logger.error("兜底链接处理失败,base:[{}],spec:[{}]", page.getUrl().toString(), link);
                }
                return link;
            }).collect(Collectors.toList());
        }
        return links;
    }

    /**
     * 抓取页面内容，如果未抓取到/结果不匹配，返回null。
     *
     * @param page page
     * @return result map
     */
    private Map<String, Object> extractPageItem(Page page) {
        //不是目标链接直接返回
        if (targetUrlSelectors
                .stream()
                .noneMatch(urlFinderSelector ->
                        page.getUrl()
                                .select(urlFinderSelector)
                                .match())) {
            return null;
        }
        String html = selector.select(page.getRawText());
        return processSingle(page, html);
    }

    /**
     * 抓取页面内容，如果未抓取到/结果不匹配，返回empty list。
     *
     * @param page page
     */
    private List<Map<String, Object>> extractPageForList(Page page) {
        //不是目标链接直接返回
        if (targetUrlSelectors.stream().noneMatch(urlFinderSelector -> page.getUrl()
                .select(urlFinderSelector).match())) {
            return null;
        }
        List<Map<String, Object>> results = new ArrayList<>();
        List<String> list = selector.selectList(page.getRawText());
        for (String html : list) {
            Map<String, Object> result = processSingle(page, html);
            if (result != null) {
                results.add(result);
            }
        }
        return results;
    }

    private Map<String, Object> processSingle(Page page, String html) {
        Map<String, Object> map = new HashMap<>(defRootExtractor.getChildren().size());
        for (DefExtractor fieldExtractor : defRootExtractor.getChildren()) {
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

    private String processField(DefExtractor fieldExtractor, Page page, String html) {
        String value;
        Selector selector = fieldExtractor.compileSelector();
        switch (fieldExtractor.getSource()) {
            case RAW_HTML:
                value = page.html().selectDocument(selector);
                break;
            case URL:
                value = page.getUrl().select(selector).toString();
                break;
            case RAW_TEXT:
                value = selector.select(page.getRawText());
                break;
            case SELECTED_HTML:
            default:
                value = selector.select(html);
        }
        return value;
    }

    private List<String> processFieldForList(DefExtractor fieldExtractor, Page page, String html) {
        List<String> value;
        switch (fieldExtractor.getSource()) {
            case RAW_HTML:
                value = page.html().selectDocumentForList(selector);
                break;
            case URL:
                value = page.getUrl().selectList(selector).all();
                break;
            case RAW_TEXT:
                value = selector.selectList(page.getRawText());
                break;
            case SELECTED_HTML:
            default:
                value = selector.selectList(html);
        }
        return value;
    }

    private boolean multi() {
        return defRootExtractor.getMulti();
    }

    public DefRootExtractor getDefRootExtractor() {
        return defRootExtractor;
    }
}
