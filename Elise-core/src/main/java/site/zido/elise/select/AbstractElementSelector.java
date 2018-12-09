package site.zido.elise.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Abstract element selector.
 *
 * @author zido
 */
public abstract class AbstractElementSelector implements Selector {
    @Override
    public List<Fragment> select(Element element) {
        List<Node> elements = selectAsNode(element);
        List<Fragment> results = new ArrayList<>();
        for (Node node : elements) {
            Fragment fragment = new Fragment();
            fragment.add(node);
            results.add(fragment);
        }
        return results;
    }

    @Override
    public List<Fragment> select(String text) {
        return select(Jsoup.parse(text));
    }
}
