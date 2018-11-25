package site.zido.elise.select.matcher;

import site.zido.elise.select.Matcher;

public class SiteMatcher implements Matcher {
    private char[] express;

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
        if (express[expressIndex] == '*' && express[expressIndex] + 1 != express.length && originIndex == origin.length) {
            return false;
        }
        if (originIndex == origin.length) {
            return false;
        }
        if (express[expressIndex] == '?' || express[expressIndex] == origin[originIndex]) {
            return match(origin, express, originIndex + 1, expressIndex + 1);
        }
        if (express[expressIndex] == '*') {
            return match(origin, express, originIndex + 1, expressIndex) || match(origin, express, originIndex, expressIndex + 1);
        }
        return false;
    }
}
