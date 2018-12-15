package site.zido.elise.select;

import org.jsoup.nodes.Document;
import site.zido.elise.processor.ResponseContextHolder;
import site.zido.elise.task.api.Source;
import site.zido.elise.task.model.Action;
import site.zido.elise.utils.Safe;
import site.zido.elise.utils.ValidateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * link selector
 *
 * @author zido
 */
public class LinkSelectHandler implements SelectHandler {
    @Override
    public List<Object> select(ResponseContextHolder response, Object partition, Action action) throws SelectorMatchException {
        Object[] extras = action.getExtras();
        String express = Safe.getStrFromArray(extras, 0);
        if ("".equals(express)) {
            throw new SelectorMatchException(String.format("the action: [%s] need a string express but get %s", action.getToken(), extras[0]));
        }
        Pattern pattern = Pattern.compile(express);
        Document document = null;
        if (Source.matchSource(action.getSource(), Source.PARTITION)) {
            List<Object> results = new ArrayList<>();
            if (partition instanceof Document) {
                document = (Document) partition;
            } else if (partition instanceof List) {
                for (Object str : (List) partition) {
                    if (str instanceof String) {
                        if (pattern.matcher((CharSequence) str).find()) {
                            results.add(str);
                        }
                    }
                }
                return results;
            }
        } else if (Source.matchSource(action.getSource(),Source.BODY,Source.HTML)) {
            document = response.getDocument();
        }
        if (document == null) {
            return null;
        }
        List<Object> results = new ArrayList<>();
        for (int i = 1; i < extras.length; i++) {
            if (!(extras[i] instanceof String)) {
                throw new SelectorMatchException(String.format("the action: [%s] need param like [a:href] but get %s", action.getToken(), extras[i]));
            }
            String linkProp = (String) extras[i];
            String[] split = linkProp.split(":");
            if (split.length != 2) {
                throw new SelectorMatchException(String.format("the action: [%s] need param like [a:href] but get %s", action.getToken(), linkProp));
            }
            String href;
            if (!ValidateUtils.isEmpty(response.getUrl())) {
                href = document.attr("abs:" + split[0]);
            } else {
                href = document.attr(split[1]);
            }
            if (pattern.matcher(href).find()) {
                results.add(href);
            }
        }
        return results;
    }
}
