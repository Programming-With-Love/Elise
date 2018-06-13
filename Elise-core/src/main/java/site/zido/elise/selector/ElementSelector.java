package site.zido.elise.selector;

import org.jsoup.nodes.Element;

import java.util.List;

/**
 * 能够查询dom节点的selector
 *
 * @author zido
 */
public interface ElementSelector {

    String select(Element element);

    List<String> selectList(Element element);
}
