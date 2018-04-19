package com.hnqc.ironhand.selector;

import com.hnqc.ironhand.utils.ValidateUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 链接选择器
 *
 * @author zido
 * @date 2018/56/13
 */
public class LinkSelector extends AbstractElementSelector {
    private static final String LIN_TARGET_TAG = "a";
    private static final String ATTR_WITH_BASE = "abs:href";
    private static final String ATTR = "href";

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
        Elements elements = element.select(LIN_TARGET_TAG);
        List<String> links = new ArrayList<>(elements.size());
        for (Element element0 : elements) {
            if (!ValidateUtils.isEmpty(element0.baseUri())) {
                links.add(element0.attr(ATTR_WITH_BASE));
            } else {
                links.add(element0.attr(ATTR));
            }
        }
        return links;
    }
}
