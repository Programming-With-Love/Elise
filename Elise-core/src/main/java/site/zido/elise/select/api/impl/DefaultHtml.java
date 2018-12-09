package site.zido.elise.select.api.impl;

import org.jsoup.nodes.Document;
import site.zido.elise.select.api.body.Html;

public class DefaultHtml extends DefaultXml implements Html {
    public static Html EMPTY_HTML = new DefaultHtml("");

    public DefaultHtml(Document document) {
        super(document);

    }

    public DefaultHtml(String html) {
        super(html);
    }

}
