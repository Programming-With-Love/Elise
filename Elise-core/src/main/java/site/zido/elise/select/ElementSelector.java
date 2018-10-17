package site.zido.elise.select;

import org.jsoup.nodes.Element;

import java.util.List;

public interface ElementSelector {
    List<Fragment> select(Element element);
}
