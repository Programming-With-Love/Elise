package site.zido.elise.select;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import site.zido.elise.select.configurable.Type;
import site.zido.elise.utils.ValidateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * link selector
 *
 * @author zido
 */
public class LinkSelector extends AbstractElementSelector {
    private static final String EMPTY_URL_PATTERN = "https?://.*";
    private transient Selector targetSelector;
    private transient Selector regionSelector;
    private List<LinkProperty> linkProperties = new ArrayList<>();
    private String target;
    private Type type;
    private String sourceRegion;
    private transient AtomicBoolean needCompile = new AtomicBoolean(true);

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
    public LinkSelector(String target, Type type, String sourceRegion) {
        this.target = target;
        this.type = type;
        this.sourceRegion = sourceRegion;
        linkProperties.add(new LinkProperty("a", "href"));
    }

    private void compile() {
        if (!needCompile.compareAndSet(true, false)) {
            return;
        }
        String pattern = target;
        if (pattern == null) {
            pattern = EMPTY_URL_PATTERN;
        }
        if (type == null) {
            type = Type.REGEX;
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

    /**
     * Gets link properties.
     *
     * @return the link properties
     */
    public List<LinkProperty> getLinkProperties() {
        return linkProperties;
    }

    @Override
    public List<Node> selectAsNode(Element element) {
        compile();
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
