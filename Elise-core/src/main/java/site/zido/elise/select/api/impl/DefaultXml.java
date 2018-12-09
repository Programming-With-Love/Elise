package site.zido.elise.select.api.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import site.zido.elise.select.ElementSelector;
import site.zido.elise.select.LinkSelector;
import site.zido.elise.select.api.Matchable;
import site.zido.elise.select.api.RichableResult;
import site.zido.elise.select.api.body.Bytes;
import site.zido.elise.select.api.body.Html;
import site.zido.elise.select.api.body.Xml;
import site.zido.elise.select.matcher.ElementMatcher;
import site.zido.elise.utils.StringUtils;

public class DefaultXml extends DefaultText implements Xml {

    private Document document;

    public DefaultXml(Document document) {
        super(document.outerHtml());
        this.document = document;

    }

    public DefaultXml(String html) {
        super(html);
        if (StringUtils.hasLength(html)) {
            this.document = Jsoup.parse(html);
        }
    }

    @Override
    public Xml saveXml() {
        return null;
    }

    @Override
    public Xml select(ElementSelector selector) {
        return null;
    }

    @Override
    public Xml region(ElementSelector selector) {
        return null;
    }

    @Override
    public Xml matches(ElementMatcher elementMatcher) {
        return null;
    }

    @Override
    public Matchable filterLinks(LinkSelector selector) {
        return null;
    }

    @Override
    public RichableResult richText() {
        return null;
    }

    @Override
    public Html html() {
        return null;
    }

    @Override
    public Bytes bytes() {
        return null;
    }

    public Document getDocument() {
        return document;
    }
}
