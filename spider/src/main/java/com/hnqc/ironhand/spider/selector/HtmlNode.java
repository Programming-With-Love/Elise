package com.hnqc.ironhand.spider.selector;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class HtmlNode extends AbsSelectable {
    private final List<Element> elements;

    public HtmlNode() {
        elements = null;
    }

    public HtmlNode(List<Element> elements) {
        this.elements = elements;
    }

    protected List<Element> getElements() {
        return elements;
    }

    protected Selectable selectElements(AbsElementSelector elementSelector) {
        ListIterator<Element> iterator = getElements().listIterator();
        if (!elementSelector.hasAttribute()) {
            List<Element> resultElements = new ArrayList<>();
            while (iterator.hasNext()) {
                Element element = checkElementAndConvert(iterator);
                List<Element> selectElements = elementSelector.selectElements(element);
                resultElements.addAll(selectElements);
            }
            return new HtmlNode(resultElements);
        } else {
            List<String> resultStrings = new ArrayList<>();
            while (iterator.hasNext()) {
                Element element = checkElementAndConvert(iterator);
                List<String> selectList = elementSelector.selectList(element);
                resultStrings.addAll(selectList);
            }
            return new PlainText(resultStrings);
        }
    }

    private Element checkElementAndConvert(ListIterator<Element> elementIterator) {
        Element element = elementIterator.next();
        if (!(element instanceof Document)) {
            Document root = new Document(element.ownerDocument().baseUri());
            Element clone = element.clone();
            root.appendChild(clone);
            elementIterator.set(root);
            return root;
        }
        return element;
    }

    @Override
    protected List<String> getSourceTexts() {
        List<String> sourceTexts = new ArrayList<String>(getElements().size());
        for (Element element : getElements()) {
            sourceTexts.add(element.toString());
        }
        return sourceTexts;
    }

    @Override
    public Selectable xpath(String xpath) {
        XPathSelector xPathSelector = new XPathSelector(xpath);
        return selectElements(xPathSelector);
    }

    @Override
    public Selectable select(Selector selector) {
        return selectList(selector);
    }

    @Override
    public Selectable selectList(Selector selector) {
        if (selector instanceof AbsElementSelector) {
            return selectElements((AbsElementSelector) selector);
        }
        return selectList(selector, getSourceTexts());
    }

    @Override
    public Selectable $(String selector) {
        CssSelector cssSelector = new CssSelector(selector);
        return selectElements(cssSelector);
    }

    @Override
    public Selectable $(String selector, String attrName) {
        CssSelector cssSelector = new CssSelector(selector, attrName);
        return selectElements(cssSelector);
    }

    @Override
    public Selectable links() {
        return selectElements(new LinkSelector());
    }

    @Override
    public List<Selectable> nodes() {
        List<Selectable> selectables = new ArrayList<>();
        for (Element element : getElements()) {
            List<Element> childElements = new ArrayList<>(1);
            childElements.add(element);
            selectables.add(new HtmlNode(childElements));
        }
        return selectables;
    }
}
