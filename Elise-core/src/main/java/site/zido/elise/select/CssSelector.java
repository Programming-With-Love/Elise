package site.zido.elise.select;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Css selector.
 *
 * @author zido
 */
public class CssSelector implements ElementSelector {
    private String cssExpress;

    /**
     * Instantiates a new Css selector.
     *
     * @param cssExpress the css express
     */
    public CssSelector(String cssExpress) {
        this.cssExpress = cssExpress;
    }

    @Override
    public List<Node> select(Element element) {
        Elements elements = element.select(cssExpress);
        return new ArrayList<>(elements);
    }
}
