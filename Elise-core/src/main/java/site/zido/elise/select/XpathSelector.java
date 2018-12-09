package site.zido.elise.select;

import com.virjar.sipsoup.exception.XpathSyntaxErrorException;
import com.virjar.sipsoup.model.XpathEvaluator;
import com.virjar.sipsoup.parse.XpathParser;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The type X path selector.
 *
 * @author zido
 */
public class XpathSelector extends AbstractElementSelector {

    private String xpathExpress;
    private transient AtomicBoolean needCompile = new AtomicBoolean(true);
    private transient XpathEvaluator evaluator;

    /**
     * Instantiates a new X path selector.
     *
     * @param xpathExpress the xpath express
     */
    public XpathSelector(String xpathExpress) {
        this.xpathExpress = xpathExpress;
    }

    private void compile() {
        if (needCompile.compareAndSet(true, false)) {
            try {
                evaluator = XpathParser.compile(xpathExpress);
            } catch (XpathSyntaxErrorException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<Node> selectAsNode(Element element) {
        compile();
        return new ArrayList<>(evaluator.evaluateToElement(element));
    }

}
