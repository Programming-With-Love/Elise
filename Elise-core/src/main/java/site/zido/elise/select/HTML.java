package site.zido.elise.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import site.zido.elise.utils.ValidateUtils;

import java.util.List;

public class HTML implements ElementSelectable {
    private String text;
    private String url;
    private transient Document document;

    public HTML() {

    }

    public HTML(String text, String url) {
        this.text = text;
        this.url = url;
        getDocument();
    }

    public Document getDocument() {
        if (document == null) {
            document = Jsoup.parse(text, url);
        }
        return document;
    }

    @Override
    public List<Fragment> select(ElementSelector selector) {
        return selector.select(getDocument());
    }

    @Override
    public List<String> select(Selector selector) {
        return selector.select(text);
    }

    @Override
    public boolean match(Selector selector) {
        return !ValidateUtils.isEmpty(selector.select(text));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
