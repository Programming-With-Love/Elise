package com.hnqc.ironhand.selector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Html def
 *
 * @author zido
 * @date 2018/04/22
 */
public class Html extends HtmlNode {
    private Logger logger = LoggerFactory.getLogger(Html.class);

    private Document document;

    public Html(String text, String url) {
        try {
            this.document = Jsoup.parse(text, url);
        } catch (Exception e) {
            this.document = null;
            logger.warn("parse document err", e);
        }
    }

    public Html(String text) {
        try {
            this.document = Jsoup.parse(text);
        } catch (Exception e) {
            this.document = null;
            logger.warn("parse document err", e);
        }
    }

    public Html(Document document) {
        this.document = document;
    }

    public Document document() {
        return document;
    }

    @Override
    protected List<Element> getElements() {
        return Collections.singletonList(document());
    }

    public String selectDocument(Selector selector) {
        if (selector instanceof ElementSelector) {
            ElementSelector elementSelector = (ElementSelector) selector;
            return elementSelector.select(document());
        }
        return selector.select(getFirstSourceText());
    }

    public List<String> selectDocumentForList(Selector selector) {
        if (selector instanceof ElementSelector) {
            ElementSelector elementSelector = (ElementSelector) selector;
            return elementSelector.selectList(document());
        }
        return selector.selectList(getFirstSourceText());
    }
}
