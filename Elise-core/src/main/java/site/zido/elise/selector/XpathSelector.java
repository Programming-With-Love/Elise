package site.zido.elise.selector;

import org.jsoup.nodes.Element;
import site.zido.elise.utils.ValidateUtils;
import us.codecraft.xsoup.XPathEvaluator;
import us.codecraft.xsoup.Xsoup;

import java.util.List;

/**
 * xpath选择器，基于xsoup
 *
 * @author zido
 */
public class XpathSelector extends AbstractElementSelector {
    private XPathEvaluator xPathEvaluator;

    public XpathSelector(XPathEvaluator xPathEvaluator) {
        this.xPathEvaluator = xPathEvaluator;
    }

    public XpathSelector(String xpathStr) {
        this.xPathEvaluator = Xsoup.compile(xpathStr);
    }

    @Override
    public Element selectElement(Element element) {
        List<Element> elements = selectElements(element);
        if (!ValidateUtils.isEmpty(elements)) {
            return elements.get(0);
        }
        return null;
    }

    @Override
    public List<Element> selectElements(Element element) {
        return xPathEvaluator.evaluate(element).getElements();
    }

    @Override
    public boolean isText() {
        return xPathEvaluator.hasAttribute();
    }

    @Override
    public String select(Element element) {
        return xPathEvaluator.evaluate(element).get();
    }

    @Override
    public List<String> selectList(Element element) {
        return xPathEvaluator.evaluate(element).list();
    }
}
