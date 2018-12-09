package site.zido.elise.select.matcher;

import site.zido.elise.select.Matcher;

/**
 * The type Site matcher.
 *
 * @author zido
 */
public class SiteMatcher implements Matcher {
    /**
     * a character can match any single character
     */
    private static final char SINGLE_MATCH_CHAR = '?';
    /**
     * a character can match any number of characters
     */
    private static final char MORE_MATCH_CHAR = '*';
    private char[] express;

    /**
     * Instantiates a new Site matcher.
     *
     * @param express the express
     */
    public SiteMatcher(String express) {
        this.express = express.toCharArray();
    }

    @Override
    public boolean matches(Object target) {
        if (!(target instanceof String)) {
            return false;
        }
        char[] origin = ((String) target).toCharArray();
        return match(origin, express, 0, 0);
    }

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
}
