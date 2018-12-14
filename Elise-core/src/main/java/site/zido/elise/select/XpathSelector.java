package site.zido.elise.select;

import com.virjar.sipsoup.exception.XpathSyntaxErrorException;
import com.virjar.sipsoup.model.XpathEvaluator;
import com.virjar.sipsoup.parse.XpathParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import site.zido.elise.processor.ResponseContextHolder;
import site.zido.elise.task.api.Source;
import site.zido.elise.task.model.Action;
import site.zido.elise.utils.Safe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type X path selector.
 *
 * @author zido
 */
public class XpathSelector implements Selector {
    @Override
    public List<Object> selectObj(ResponseContextHolder response, Object partition, Action action) throws SelectorMatchException {
        Object[] extras = action.getExtras();
        String express = Safe.getStrFromArray(extras, 0);
        if ("".equals(express)) {
            throw new SelectorMatchException(String.format("the action: [%s] need a xpath string express but get %s", action.getToken(), extras[0]));
        }
        XpathEvaluator evaluator;
        try {
            evaluator = XpathParser.compile(express);
        } catch (XpathSyntaxErrorException e) {
            throw new SelectorMatchException(e);
        }
        Document document = null;
        if (Source.matchSource(action.getSource(), Source.PARTITION)) {
            List<Object> results = new ArrayList<>();
            if (partition instanceof Document) {
                document = (Document) partition;
            } else if (partition instanceof List) {
                for (Object str : (List) partition) {
                    if (str instanceof Node) {
                        Elements elements = evaluator.evaluateToElements((Element) str);
                        results.addAll(elements);
                    }
                }
                return results;
            }
        } else if (Source.matchSource(action.getSource(), Source.BODY, Source.HTML)) {
            document = response.getDocument();
        }
        if (document == null) {
            return null;
        }
        return evaluator.evaluateToElement(document).stream().map(element -> (Object) element).collect(Collectors.toList());
    }
}
