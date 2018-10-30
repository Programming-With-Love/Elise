package site.zido.elise.select;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.Collections;
import java.util.List;

/**
 * 原样返回，无动作selector
 *
 * @author zido
 */
public class NullElementSelector extends AbstractElementSelector {
    @Override
    public List<Node> selectAsNode(Element element) {
        return Collections.singletonList(element);
    }
}
