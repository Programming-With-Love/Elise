package com.hnqc.ironhand.selector;

import com.hnqc.ironhand.utils.ValidateUtils;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * css选择器
 *
 * @author zido
 * @date 2018/05/12
 */
public class CssSelector extends AbstractElementSelector {
    public static final String INNER_HTML = "innerHtml";
    public static final String TEXT = "text";
    public static final String ALL_TEXT = "allText";
    
    private String selectorText;

    private String attrName;

    public CssSelector(String selectorText) {
        this.selectorText = selectorText;
    }

    public CssSelector(String selectorText, String attrName) {
        this.selectorText = selectorText;
        this.attrName = attrName;
    }

    private String getValue(Element element) {
        if (attrName == null) {
            return element.outerHtml();
        } else if (INNER_HTML.equalsIgnoreCase(attrName)) {
            return element.html();
        } else if (TEXT.equalsIgnoreCase(attrName)) {
            return getText(element);
        } else if (ALL_TEXT.equalsIgnoreCase(attrName)) {
            return element.text();
        } else {
            return element.attr(attrName);
        }
    }

    private String getText(Element element) {
        StringBuilder sb = new StringBuilder();
        for (Node node : element.childNodes()) {
            if (node instanceof TextNode) {
                TextNode text = (TextNode) node;
                sb.append(text.text());
            }
        }
        return sb.toString();
    }

    @Override
    public Element selectElement(Element element) {
        Elements elements = element.select(selectorText);
        if (!ValidateUtils.isEmpty(elements)) {
            return elements.get(0);
        }
        return null;
    }

    @Override
    public List<Element> selectElements(Element element) {
        return element.select(selectorText);
    }

    @Override
    public boolean isText() {
        return attrName != null;
    }

    @Override
    public String select(Element element) {
        List<Element> elements = selectElements(element);
        if (ValidateUtils.isEmpty(elements)) {
            return null;
        }
        return getValue(elements.get(0));
    }

    @Override
    public List<String> selectList(Element doc) {
        List<String> result = new ArrayList<>();
        List<Element> elements = selectElements(doc);
        if (!ValidateUtils.isEmpty(elements)) {
            for (Element element : elements) {
                String value = getValue(element);
                if (value != null) {
                    result.add(value);
                }
            }
        }
        return result;
    }
}
