package com.hnqc.ironhand.selector;

import org.jsoup.nodes.Element;

import java.util.Collections;
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
        return Collections.singletonList(element);
    }

    @Override
    public boolean isText() {
        return false;
    }

    @Override
    public String select(Element element) {
        return element.html();
    }

    @Override
    public List<String> selectList(Element element) {
        return Collections.singletonList(element.html());
    }

    @Override
    public List<String> selectList(String text) {
        return Collections.singletonList(text);
    }
}
