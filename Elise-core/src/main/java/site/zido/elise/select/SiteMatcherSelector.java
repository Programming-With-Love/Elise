package site.zido.elise.select;

import site.zido.elise.processor.ResponseContextHolder;
import site.zido.elise.task.api.Source;
import site.zido.elise.task.model.Action;
import site.zido.elise.utils.Safe;

import java.util.Collections;
import java.util.List;

/**
 * The type Site matcher.
 *
 * @author zido
 */
public class SiteMatcherSelector implements Selector {
    /**
     * a character can match any single character
     */
    private static final char SINGLE_MATCH_CHAR = '?';
    /**
     * a character can match any number of characters
     */
    private static final char MORE_MATCH_CHAR = '*';
    private boolean match(char[] origin, char[] express, int originIndex, int expressIndex) {
        if (originIndex == origin.length && expressIndex == express.length) {
            return true;
        }
        if (expressIndex == express.length) {
            return false;
        }
        if (express[expressIndex] == MORE_MATCH_CHAR && express[expressIndex] + 1 != express.length && originIndex == origin.length) {
            return false;
        }
        if (originIndex == origin.length) {
            return false;
        }
        if (express[expressIndex] == SINGLE_MATCH_CHAR || express[expressIndex] == origin[originIndex]) {
            return match(origin, express, originIndex + 1, expressIndex + 1);
        }
        if (express[expressIndex] == MORE_MATCH_CHAR) {
            return match(origin, express, originIndex + 1, expressIndex) || match(origin, express, originIndex, expressIndex + 1);
        }
        return false;
    }

    @Override
    public List<Object> selectObj(ResponseContextHolder response, Object partition, Action action) throws SelectorMatchException {
        Object[] extras = action.getExtras();
        String express = Safe.getStrFromArray(extras, 0);
        if ("".equals(express)) {
            throw new SelectorMatchException(String.format("the action: [%s] need a string express like [site.zido.*] but get %s", action.getToken(), extras[0]));
        }
        String source = action.getSource();
        String target;
        if (Source.matchSource(source, Source.URL)) {
            target = response.getUrl();
        } else {
            throw new SelectorMatchException("match site just support response url");
        }
        if (match(target.toCharArray(), express.toCharArray(), 0, 0)) {
            return Collections.singletonList(target);
        }
        return null;
    }
}
