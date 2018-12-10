package site.zido.elise.select.matcher;

import site.zido.elise.select.RegexSelector;

public class RegexMatcher implements Matcher {
    private RegexSelector regexSelector;

    public RegexMatcher() {
    }

    public RegexMatcher(String regex) {
        this(regex, 0);
    }

    public RegexMatcher(String regex, int group) {
        this(regex, group, 0);
    }

    public RegexMatcher(String regex, int group, int flags) {
        this.regexSelector = new RegexSelector(regex, group, flags);
    }

    @Override
    public boolean matches(Object target) {
        if (target == null) {
            return false;
        }
        String mod;
        if (target.getClass().isPrimitive()) {
            mod = target.toString();
        } else if (target instanceof String) {
            mod = (String) target;
        } else {
            return false;
        }
        return regexSelector.select(mod) != null;
    }
}
