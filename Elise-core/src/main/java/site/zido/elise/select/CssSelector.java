package site.zido.elise.select;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class CssSelector extends AbstractElementSelector {
    private String cssExpress;

    public CssSelector(String cssExpress) {
        this.cssExpress = cssExpress;
    }

    @Override
    public List<Node> selectAsNode(Element element) {
        Elements elements = element.select(cssExpress);
        List<Node> results = new ArrayList<>(elements);
        return results;
    }
}
