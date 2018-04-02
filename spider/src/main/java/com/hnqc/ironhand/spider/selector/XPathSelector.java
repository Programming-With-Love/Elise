package com.hnqc.ironhand.spider.selector;

import com.hnqc.ironhand.spider.utils.ValidateUtils;
import org.jsoup.nodes.Element;
import us.codecraft.xsoup.XPathEvaluator;
import us.codecraft.xsoup.Xsoup;

import java.util.List;

public class XPathSelector extends AbsElementSelector {
    private XPathEvaluator xPathEvaluator;

    public XPathSelector(XPathEvaluator xPathEvaluator) {
        this.xPathEvaluator = xPathEvaluator;
    }

    public XPathSelector(String xpathStr) {
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
    public boolean hasAttribute() {
        return xPathEvaluator.hasAttribute();
    }

    @Override
    public String select(Element element) {
        return xPathEvaluator.evaluate(element).get();
    }

    @Override
    public List<String> selectList(Element element) {
        return null;
    }
}
