package site.zido.elise.selector;

import site.zido.elise.utils.ValidateUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 链接选择器
 *
 * @author zido
 * @date 2018/56/13
 */
public class LinkSelector extends AbstractElementSelector {
    private static final String LIN_TARGET_TAG = "a";
    private static final String ATTR_WITH_BASE = "abs:";
    private static final String ATTR = "href";

    private List<LinkProperty> choosers = new ArrayList<>();

    public LinkSelector(List<LinkProperty> choosers) {
        this.choosers.add(new LinkProperty(LIN_TARGET_TAG, ATTR));
        if (choosers != null) {
            this.choosers.addAll(choosers);
        }
    }

    public LinkSelector() {
        this(null);
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
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> selectList(Element element) {
        List<String> links = new ArrayList<>(20);
        for (LinkProperty chooser : choosers) {
            Elements elements = element.select(chooser.getTag());
            for (Element element0 : elements) {
                if (!ValidateUtils.isEmpty(element0.baseUri())) {
                    links.add(element0.attr(ATTR_WITH_BASE + chooser.getAttr()));
                } else {
                    links.add(element0.attr(chooser.getAttr()));
                }
            }
        }
        return links;
    }
}
