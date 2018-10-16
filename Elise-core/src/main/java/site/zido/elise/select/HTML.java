package site.zido.elise.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import site.zido.elise.selector.ElementSelector;
import site.zido.elise.selector.Selector;

public class HTML implements ElementSelectable {
    private String text;
    private transient Document document;

    public HTML(String text) {
        this.text = text;
        document();
    }

    public Document document() {
        if (document == null) {
            document = Jsoup.parse(text);
        }
        return document;
    }

    @Override
    public String select(ElementSelector selector) {
        return selector.select(document());
    }

    @Override
    public String select(Selector selector) {
        return selector.select(text);
    }

    @Override
    public boolean match(Selector selector) {
        return selector.select(text) != null;
    }

}
