package site.zido.elise.selector;

import org.jsoup.nodes.Element;
import site.zido.elise.configurable.ConfigurableUrlFinder;
import site.zido.elise.utils.ValidateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * UrlFinderSelector
 *
 * @author zido
 */
public class UrlFinderSelector extends AbstractElementSelector {
    private static final String EMPTY_URL_PATTERN = "https?://.*";
    private Selector targetSelector;
    private Selector regionSelector;
    private List<LinkProperty> linkProperties;

    public UrlFinderSelector(ConfigurableUrlFinder urlFinder) {
        this(urlFinder.getValue(), urlFinder.getType(), urlFinder.getSourceRegion());
        this.setLinkProperties(urlFinder.getLinkProperties());
    }

    public UrlFinderSelector(String target) {
        this(target, null, null);
    }

    public UrlFinderSelector(String target, ConfigurableUrlFinder.Type type, String sourceRegion) {
        String pattern = target;
        if (pattern == null) {
            pattern = EMPTY_URL_PATTERN;
        }
        if (type == null) {
            type = ConfigurableUrlFinder.Type.REGEX;
        }

        switch (type) {
            case REGEX:
            default:
                this.targetSelector = new RegexSelector(pattern);
        }
        if (sourceRegion != null) {
            this.regionSelector = new XpathSelector(sourceRegion);
        } else {
            this.regionSelector = new NullElementSelector();
        }

    }

    @Override
    public String select(String text) {
        List<String> regions = regionSelector.selectList(text);
        if (ValidateUtils.isEmpty(regions)) {
            return null;
        }
        for (String region : regions) {
            List<String> results = selectList(region);
            if (!ValidateUtils.isEmpty(results)) {
                return results.get(0);
            }
        }
        return null;
    }

    @Override
    public List<String> selectList(String text) {
        List<String> regions = regionSelector.selectList(text);
        if (ValidateUtils.isEmpty(regions)) {
            return null;
        }
        List<String> results = new ArrayList<>();
        for (String region : regions) {
            List<String> childResults = targetSelector.selectList(region);
            if (childResults != null) {
                results.addAll(childResults);
            }
        }
        return results;
    }

    @Override
    public Element selectElement(Element element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Element> selectElements(Element element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isText() {
        return true;
    }

    @Override
    public String select(Element element) {
        if (regionSelector instanceof AbstractElementSelector) {
            AbstractElementSelector elementSelector = (AbstractElementSelector) regionSelector;
            List<String> regions = elementSelector.selectList(element);
            if (ValidateUtils.isEmpty(regions)) {
                return null;
            }
            for (String region : regions) {
                List<String> results = selectList(region);
                if (!ValidateUtils.isEmpty(results)) {
                    return results.get(0);
                }
            }
            return null;
        } else {
            return select(element.toString());
        }
    }

    @Override
    public List<String> selectList(Element element) {
        List<Element> regions;
        if (regionSelector instanceof AbstractElementSelector) {
            AbstractElementSelector elementSelector = (AbstractElementSelector) regionSelector;
            regions = elementSelector.selectElements(element);
            return new HtmlNode(regions).links(getLinkProperties()).selectList(targetSelector).all();
        } else {
            return selectList(element.toString());
        }
    }

    public List<LinkProperty> getLinkProperties() {
        return linkProperties;
    }

    public UrlFinderSelector setLinkProperties(List<LinkProperty> linkProperties) {
        this.linkProperties = linkProperties;
        return this;
    }
}
