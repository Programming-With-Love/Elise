package site.zido.elise.select.configurable;

import org.jsoup.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.Page;
import site.zido.elise.ResultItem;
import site.zido.elise.select.*;
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
    private List<LinkSelector> targetUrlSelectors = new ArrayList<>();
    private List<LinkSelector> helpUrlSelectors = new ArrayList<>();
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
                LinkSelector linkSelector = new LinkSelector(configurableUrlFinder);
                this.targetUrlSelectors.add(linkSelector);
            }
        }
        List<ConfigurableUrlFinder> helpUrlFinder = defRootExtractor.getHelpUrl();
        if (!ValidateUtils.isEmpty(helpUrlFinder)) {
            for (ConfigurableUrlFinder configurableUrlFinder : helpUrlFinder) {
                LinkSelector linkSelector = new LinkSelector(configurableUrlFinder);
                this.helpUrlSelectors.add(linkSelector);
            }
        }
    }

    @Override
    public List<ResultItem> extract(Page page) {
        if (targetUrlSelectors.stream().noneMatch(linkSelector -> linkSelector.select(page.getUrl().toString()) != null)) {
            return new ArrayList<>();
        }
        List<ResultItem> results = new ArrayList<>();
        Selectable body = page.getBody();
        Selector selector = defRootExtractor.compileSelector();
        if (body instanceof ElementSelectable && selector instanceof ElementSelector) {
            List<Node> list = ((ElementSelectable) body).selectAsNode((ElementSelector) selector);
            for (Node node : list) {
                //TODO handle element select
            }
        } else {
            List<Fragment> list = body.select(selector);
            //get region
            for (Fragment fragment : list) {
                String html = fragment.toString();
                Map<String, List<Fragment>> item = processSingle(page, html);
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
        return new ArrayList<>();
    }

    @Override
    public List<String> extractLinks(Page page) {
        List<String> links;

        if (ValidateUtils.isEmpty(helpUrlSelectors)) {
            return new ArrayList<>(0);
        } else {
            links = new ArrayList<>();
            //TODO can't select relative path
            for (LinkSelector selector : helpUrlSelectors) {
                List<Fragment> list = page.getBody().select(selector);
                for (Fragment fragment : list) {
                    links.add(fragment.toString());
                }
            }
            //processing link
            links = links.stream().map(link -> {
                link = link.replace("&amp;", "&");
                if (link.startsWith(HTTP_LABEL)) {
                    //已经是绝对路径的，不再处理
                    return link;
                }
                try {
                    return new URL(new URL(page.getUrl().toString()), link).toString();
                } catch (MalformedURLException e) {
                    logger.error("An error occurred while processing the link,base:[{}],spec:[{}]", page.getUrl().toString(), link);
                }
                return link;
            }).collect(Collectors.toList());
        }
        return links;
    }

    private Map<String, List<Fragment>> processSingle(Page page, String html) {
        Map<String, List<Fragment>> map = new HashMap<>(defRootExtractor.getChildren().size());
        for (DefExtractor fieldExtractor : defRootExtractor.getChildren()) {
            List<Fragment> results = processField(fieldExtractor, page, html);
            if (ValidateUtils.isEmpty(results) && !fieldExtractor.getNullable()) {
                return null;
            }
            map.put(fieldExtractor.getName(), results);
        }
        return map;
    }

    private List<Fragment> processField(DefExtractor fieldExtractor, Page page, String html) {
        List<Fragment> value;
        Selector selector = fieldExtractor.compileSelector();
        switch (fieldExtractor.getSource()) {
            case RAW_HTML:
                value = page.getBody().select(selector);
                break;
            case URL:
                value = selector.select(page.getUrl().toString());
                break;
            case RAW_TEXT:
                value = page.getBody().select(selector);
                break;
            case REGION:
            default:
                value = selector.select(html);
        }
        return value;
    }

    public DefRootExtractor getDefRootExtractor() {
        return defRootExtractor;
    }
}
