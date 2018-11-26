package site.zido.elise.select;

import org.jsoup.nodes.Node;

import java.util.List;

/**
 * The interface Element selectable.
 *
 * @author zido
 */
public interface ElementSelectable extends Selectable {
    /**
     * Select list.
     *
     * @param selector the selector
     * @return the list
     */
    List<Fragment> select(ElementSelector selector);

    /**
     * Select as node list.
     *
     * @param selector the selector
     * @return the list
     */
    List<Node> selectAsNode(ElementSelector selector);
}
