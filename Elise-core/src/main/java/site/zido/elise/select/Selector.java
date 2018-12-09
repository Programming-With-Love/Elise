package site.zido.elise.select;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

/**
 * The interface Selector.
 *
 * @author zido
 */
public interface Selector {
    /**
     * Select list.
     *
     * @param text the text
     * @return the list
     */
    List<Fragment> select(String text);

    /**
     * Select list.
     *
     * @param element the element
     * @return the list
     */
    List<Fragment> select(Element element);

    /**
     * Select as node list.
     *
     * @param element the element
     * @return the list
     */
    List<Node> selectAsNode(Element element);
}
