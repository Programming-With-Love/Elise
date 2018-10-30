package site.zido.elise.select;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

public interface ElementSelector extends Selector {
    List<Fragment> select(Element element);

    List<Node> selectAsNode(Element element);

}
