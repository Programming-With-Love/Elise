package site.zido.elise.select;

import com.virjar.sipsoup.exception.XpathSyntaxErrorException;
import com.virjar.sipsoup.model.XpathEvaluator;
import com.virjar.sipsoup.parse.XpathParser;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.List;

public class XPathSelector extends AbstractElementSelector {

    private XpathEvaluator evaluator;

    public XPathSelector(String xpathExpress) throws XpathSyntaxErrorException {
        evaluator = XpathParser.compile(xpathExpress);
    }

    @Override
    public List<Node> selectAsNode(Element element) {
        return new ArrayList<>(evaluator.evaluateToElement(element));
    }

}
