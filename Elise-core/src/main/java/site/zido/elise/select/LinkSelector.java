package site.zido.elise.select;

import com.virjar.sipsoup.exception.XpathSyntaxErrorException;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import site.zido.elise.select.configurable.ConfigurableUrlFinder;
import site.zido.elise.utils.ValidateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * link selector
 *
 * @author zido
 */
public class LinkSelector extends AbstractElementSelector {
    private static final String EMPTY_URL_PATTERN = "https?://.*";
    private Selector targetSelector;
    private ElementSelector regionSelector;
    private List<LinkProperty> linkProperties;

    /**
     * Instantiates a new Link selector.
     *
     * @param urlFinder the url finder
     */
    public LinkSelector(ConfigurableUrlFinder urlFinder) {
        this(urlFinder.getValue(), urlFinder.getType(), urlFinder.getSourceRegion());
        this.setLinkProperties(urlFinder.getLinkProperties());
    }

    /**
     * Instantiates a new Link selector.
     *
     * @param target the target
     */
    public LinkSelector(String target) {
        this(target, null, null);
    }

    /**
     * Instantiates a new Link selector.
     *
     * @param target       the target
     * @param type         the type
     * @param sourceRegion the source region
     */
    public LinkSelector(String target, ConfigurableUrlFinder.Type type, String sourceRegion) {
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
            try {
                this.regionSelector = new XpathSelector(sourceRegion);
            } catch (XpathSyntaxErrorException e) {
                //TODO exception handle
                throw new RuntimeException(e);
            }
        } else {
            this.regionSelector = new NullElementSelector();
        }

    }

    /**
     * Gets link properties.
     *
     * @return the link properties
     */
    public List<LinkProperty> getLinkProperties() {
        return linkProperties;
    }

    /**
     * Sets link properties.
     *
     * @param linkProperties the link properties
     * @return the link properties
     */
    public LinkSelector setLinkProperties(List<LinkProperty> linkProperties) {
        this.linkProperties = linkProperties;
        return this;
    }

    @Override
    public List<Node> selectAsNode(Element element) {
        List<Node> regions = regionSelector.selectAsNode(element);
        if (ValidateUtils.isEmpty(regions)) {
            return Collections.emptyList();
        }
        List<Node> results = new ArrayList<>();
        for (Node region : regions) {
            for (LinkProperty linkProperty : linkProperties) {
                if (!(region instanceof Element)) {
                    continue;
                }
                Elements elements = ((Element) region).select(linkProperty.getTag());
                if (elements.isEmpty()) {
                    continue;
                }
                for (Element linkElement : elements) {
                    String href;
                    if (!ValidateUtils.isEmpty(linkElement.baseUri())) {
                        href = linkElement.attr("abs:" + linkProperty.getAttr());
                    } else {
                        href = linkElement.attr(linkProperty.getAttr());
                    }
                    if (!targetSelector.select(href).isEmpty()) {
                        results.add(new TextNode(href, linkElement.baseUri()));
                    }
                }
            }
        }
        return results;
    }
}
