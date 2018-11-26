package site.zido.elise.select;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

/**
 * The interface Element selector.
 *
 * @author zido
 */
public interface ElementSelector extends Selector {
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
