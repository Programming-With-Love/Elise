package site.zido.elise.selector;

import org.jsoup.nodes.Element;

import java.util.List;

/**
 * 能够查询dom节点的selector
 *
 * @author zido
 */
public interface ElementSelector {

    /**
     * Select string.
     *
     * @param element the element
     * @return the string
     */
    String select(Element element);

    /**
     * Select list list.
     *
     * @param element the element
     * @return the list
     */
    List<String> selectList(Element element);
}
