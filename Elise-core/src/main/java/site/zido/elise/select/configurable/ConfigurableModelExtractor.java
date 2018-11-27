package site.zido.elise.select.configurable;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.ResultItem;
import site.zido.elise.http.Response;
import site.zido.elise.select.*;
import site.zido.elise.utils.ValidateUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * configurable DefaultResponse model extractor
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
    public List<ResultItem> extract(Response response) {
        if (targetUrlSelectors.stream().noneMatch(linkSelector -> linkSelector.select(response.getUrl().toString()) != null)) {
            return new ArrayList<>();
        }
        List<ResultItem> results = new ArrayList<>();
        Selectable body = response.getBody();
        Selector selector = defRootExtractor.compileSelector();
        if (body instanceof ElementSelectable && selector instanceof ElementSelector) {
            List<Node> list = ((ElementSelectable) body).selectAsNode((ElementSelector) selector);
            //select from region
            for (Node node : list) {
                Map<String, List<Fragment>> item = processSingle(response, node);
                toResults(results, item);
            }
        } else {
            List<Fragment> list = body.select(selector);
            //get region
            for (Fragment fragment : list) {
                String html = fragment.toString();
                Map<String, List<Fragment>> item = processSingle(response, html);
                toResults(results, item);
            }
        }
        return results;
    }

    private void toResults(List<ResultItem> results, Map<String, List<Fragment>> item) {
        if (item != null) {
            ResultItem resultItem = new ResultItem();
            for (String s : item.keySet()) {
                resultItem.put(s, item.get(s));
            }
            results.add(resultItem);
        }
    }

    @Override
    public Set<String> extractLinks(Response response) {
        Set<String> links;

        if (ValidateUtils.isEmpty(helpUrlSelectors)) {
            return new HashSet<>(0);
        } else {
            links = new HashSet<>();
            for (LinkSelector selector : helpUrlSelectors) {
                List<Fragment> list = response.getBody().select(selector);
                for (Fragment fragment : list) {
                    String link = fragment.text().replaceAll("#.*$", "");
                    links.add(link);
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
                    return new URL(new URL(response.getUrl().toString()), link).toString();
                } catch (MalformedURLException e) {
                    logger.error("An error occurred while processing the link,base:[{}],spec:[{}]", response.getUrl().toString(), link);
                }
                return link;
            }).collect(Collectors.toSet());
        }
        return links;
    }

    private Map<String, List<Fragment>> processSingle(Response response, Object html) {
        Map<String, List<Fragment>> map = new HashMap<>(defRootExtractor.getChildren().size());
        for (DefExtractor fieldExtractor : defRootExtractor.getChildren()) {
            List<Fragment> results = processField(fieldExtractor, response, html);
            if (ValidateUtils.isEmpty(results) && !fieldExtractor.getNullable()) {
                return null;
            }
            map.put(fieldExtractor.getName(), results);
        }
        return map;
    }

    private List<Fragment> processField(DefExtractor fieldExtractor, Response response, Object html) {
        List<Fragment> value;
        Selector selector = fieldExtractor.compileSelector();
        switch (fieldExtractor.getSource()) {
            case RAW_HTML:
                value = response.getBody().select(selector);
                break;
            case URL:
                value = selector.select(response.getUrl().toString());
                break;
            case RAW_TEXT:
                value = response.getBody().select(selector);
                break;
            case REGION:
            default:
                if (html instanceof Node) {
                    if (selector instanceof ElementSelector) {
                        value = ((ElementSelector) selector).select((Element) html);
                    } else {
                        value = selector.select(((Node) html).outerHtml());
                    }
                } else {
                    value = selector.select((String) html);
                }

        }
        return value;
    }

    /**
     * Gets def root extractor.
     *
     * @return the def root extractor
     */
    public DefRootExtractor getDefRootExtractor() {
        return defRootExtractor;
    }
}
