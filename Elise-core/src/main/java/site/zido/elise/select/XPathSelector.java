package site.zido.elise.select;

import com.virjar.sipsoup.exception.XpathSyntaxErrorException;
import com.virjar.sipsoup.model.XpathEvaluator;
import com.virjar.sipsoup.parse.XpathParser;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * The type X path selector.
 *
 * @author zido
 */
public class XPathSelector extends AbstractElementSelector {

    private XpathEvaluator evaluator;

    /**
     * Instantiates a new X path selector.
     *
     * @param xpathExpress the xpath express
     * @throws XpathSyntaxErrorException the xpath syntax error exception
     */
    public XPathSelector(String xpathExpress) throws XpathSyntaxErrorException {
        evaluator = XpathParser.compile(xpathExpress);
    }

    @Override
    public List<Node> selectAsNode(Element element) {
        return new ArrayList<>(evaluator.evaluateToElement(element));
    }

}
