package site.zido.elise.configurable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.Page;
import site.zido.elise.ResultItem;
import site.zido.elise.extractor.ModelExtractor;
import site.zido.elise.selector.Selector;
import site.zido.elise.selector.UrlFinderSelector;
import site.zido.elise.utils.ValidateUtils;

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

    private static final String HTTP_LABEL = "http";
    private static Logger logger = LoggerFactory.getLogger(ConfigurableModelExtractor.class);
    private List<UrlFinderSelector> targetUrlSelectors = new ArrayList<>();
    private List<UrlFinderSelector> helpUrlSelectors = new ArrayList<>();
    private DefRootExtractor defRootExtractor;

    /**
     * construct by {@link DefRootExtractor}
     *
     * @param defRootExtractor def root extractor
     */
    public ConfigurableModelExtractor(DefRootExtractor defRootExtractor) {
        this.defRootExtractor = defRootExtractor;
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
    public List<ResultItem> extract(Page page) {
        if (targetUrlSelectors.stream().noneMatch(urlFinderSelector -> urlFinderSelector.select(page.getUrl()) != null)) {
            return new ArrayList<>();
        }
        List<ResultItem> results = new ArrayList<>();
        Selector selector = defRootExtractor.compileSelector();
        //get region
        List<String> list = selector.selectList(page.getRawText());
        for (String html : list) {
            Map<String, List<String>> item = processSingle(page, html);
            if (item != null) {
                ResultItem resultItem = new ResultItem();
                for (String s : item.keySet()) {
                    resultItem.put(s, item.get(s));
                }
                results.add(resultItem);
            }
        }
        return results;
    }

    @Override
    public List<String> extractLinks(Page page) {
        List<String> links;

        if (ValidateUtils.isEmpty(helpUrlSelectors)) {
            return new ArrayList<>(0);
        } else {
            links = new ArrayList<>();
            //TODO can't select relative path
            for (UrlFinderSelector selector : helpUrlSelectors) {
                links.addAll(selector.selectList(page.getRawText()));
            }
            //processing link
            links = links.stream().map(link -> {
                link = link.replace("&amp;", "&");
                if (link.startsWith(HTTP_LABEL)) {
                    //已经是绝对路径的，不再处理
                    return link;
                }
                try {
                    return new URL(new URL(page.getUrl()), link).toString();
                } catch (MalformedURLException e) {
                    logger.error("An error occurred while processing the link,base:[{}],spec:[{}]", page.getUrl(), link);
                }
                return link;
            }).collect(Collectors.toList());
        }
        return links;
    }

    private Map<String, List<String>> processSingle(Page page, String html) {
        Map<String, List<String>> map = new HashMap<>(defRootExtractor.getChildren().size());
        for (DefExtractor fieldExtractor : defRootExtractor.getChildren()) {
            List<String> results = processField(fieldExtractor, page, html);
            if (ValidateUtils.isEmpty(results) && !fieldExtractor.getNullable()) {
                return null;
            }
            map.put(fieldExtractor.getName(), results);
        }
        return map;
    }

    private List<String> processField(DefExtractor fieldExtractor, Page page, String html) {
        List<String> value;
        Selector selector = fieldExtractor.compileSelector();
        switch (fieldExtractor.getSource()) {
            case RAW_HTML:
                value = selector.selectList(page.getRawText());
                break;
            case URL:
                value = selector.selectList(page.getUrl());
                break;
            case RAW_TEXT:
                value = selector.selectList(page.getRawText());
                break;
            case REGION:
            default:
                value = selector.selectList(html);
        }
        return value;
    }

    public DefRootExtractor getDefRootExtractor() {
        return defRootExtractor;
    }
}
