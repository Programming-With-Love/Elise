package site.zido.elise.select;

import org.jsoup.nodes.Node;

import java.util.List;

public interface ElementSelectable extends Selectable {
    List<Fragment> select(ElementSelector selector);

    List<Node> selectAsNode(ElementSelector selector);
}
