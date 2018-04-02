package com.hnqc.ironhand.spider.selector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsElementSelector implements Selector, ElementSelector {
    public String select(String text) {
        if (text != null)
            return select(Jsoup.parse(text));
        return null;
    }

    @Override
    public List<String> selectList(String text) {
        if (text != null) {
            return selectList(Jsoup.parse(text));
        } else {
            return new ArrayList<>();
        }
    }

    public Element selectElement(String text) {
        if (text != null) {
            return selectElement(Jsoup.parse(text));
        }
        return null;
    }

    public List<Element> selectElements(String text) {
        if (text != null) {
            return selectElements(Jsoup.parse(text));
        }
        return new ArrayList<>();
    }

    public abstract Element selectElement(Element element);

    public abstract List<Element> selectElements(Element element);

    public abstract boolean hasAttribute();
}
