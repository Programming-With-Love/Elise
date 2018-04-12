package com.hnqc.ironhand.spider.selector;

import org.jsoup.nodes.Element;

import java.util.List;

/**
 * 原样返回，无动作selector
 *
 * @author zido
 * @date 2018/04/12
 */
public class NullElementSelector extends AbstractElementSelector {
    @Override
    public Element selectElement(Element element) {
        return element;
    }

    @Override
    public List<Element> selectElements(Element element) {
        return element.children();
    }

    @Override
    public boolean hasAttribute() {
        return false;
    }

    @Override
    public String select(Element element) {
        return null;
    }

    @Override
    public List<String> selectList(Element element) {
        return null;
    }
}
