package site.zido.elise.select;

import site.zido.elise.processor.ResponseContextHolder;
import site.zido.elise.task.api.Source;
import site.zido.elise.task.model.Action;
import site.zido.elise.utils.Safe;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Regex selector.
 *
 * @author zido
 */
public class RegexSelectHandler implements SelectHandler {
    @Override
    public List<Object> select(ResponseContextHolder response, Object partition, Action action) throws SelectorMatchException {
        String source = action.getSource();
        Object[] extras = action.getExtras();
        String regex = Safe.getStrFromArray(extras, 0);
        if ("".equalsIgnoreCase(regex)) {
            throw new SelectorMatchException(String.format("the action: [%s] need regex express but get %s", action.getToken(), regex));
        }
        int flags = Safe.getIntFromArray(extras, 1, 0);
        int group = Safe.getIntFromArray(extras, 2, 0);
        Pattern pattern = Pattern.compile(regex, flags);
        if (Source.matchSource(source, Source.BODY, Source.HTML, Source.TEXT)) {
            Matcher matcher = pattern.matcher(response.getHtml());
            List<Object> results = new LinkedList<>();
            while (matcher.find()) {
                results.add(matcher.group(group));
            }
            return results;
        } else if (Source.matchSource(source, Source.URL)) {
            Matcher matcher = pattern.matcher(response.getUrl());
            List<Object> results = new LinkedList<>();
            if (matcher.find()) {
                results.add(matcher.group(group));
            }
            return results;
        }
        return null;
    }
}
