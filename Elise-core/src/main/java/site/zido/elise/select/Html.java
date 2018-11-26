package site.zido.elise.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import site.zido.elise.utils.ValidateUtils;

import java.io.Serializable;
import java.util.List;

/**
 * The type Html.
 *
 * @author zido
 */
public class Html implements ElementSelectable, Serializable {
    private static final long serialVersionUID = 3727961912145284879L;
    private String text;
    private String url;
    private transient Document document;

    /**
     * Instantiates a new Html.
     */
    public Html() {

    }

    /**
     * Instantiates a new Html.
     *
     * @param text the text
     * @param url  the url
     */
    public Html(String text, String url) {
        this.text = text;
        this.url = url;
        getDocument();
    }

    /**
     * Gets document.
     *
     * @return the document
     */
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
    public List<Node> selectAsNode(ElementSelector selector) {
        return selector.selectAsNode(getDocument());
    }

    @Override
    public List<Fragment> select(Selector selector) {
        if (selector instanceof ElementSelector) {
            return ((ElementSelector) selector).select(getDocument());
        }
        return selector.select(text);
    }

    @Override
    public boolean match(Selector selector) {
        return !ValidateUtils.isEmpty(selector.select(text));
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
