package site.zido.elise.select;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

public interface ElementSelector {
    List<Node> select(Element element);
}
