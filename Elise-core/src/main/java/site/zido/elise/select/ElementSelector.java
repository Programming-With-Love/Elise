package site.zido.elise.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import site.zido.elise.select.matcher.ElementMatcher;
import site.zido.elise.utils.ValidateUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The interface Element selector.
 *
 * @author zido
 */
public interface ElementSelector extends ElementMatcher {
    /**
     * Select list.
     *
     * @param element the element
     * @return the list
     */
    List<Node> select(Element element);

    /**
     * Select as str list.
     *
     * @param element the element
     * @return the list
     */
    default List<String> selectAsStr(Element element) {
        List<Node> select = select(element);
        return select.stream().map(Node::toString).collect(Collectors.toList());
    }

    @Override
    default boolean matches(Document target) {
        return !ValidateUtils.isEmpty(select(target));
    }
}
