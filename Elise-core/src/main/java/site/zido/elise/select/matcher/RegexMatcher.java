package site.zido.elise.select.matcher;

import site.zido.elise.select.RegexSelector;

/**
 * The type Regex matcher.
 *
 * @author zido
 */
public class RegexMatcher implements Matcher {
    private RegexSelector regexSelector;

    /**
     * Instantiates a new Regex matcher.
     */
    public RegexMatcher() {
    }

    /**
     * Instantiates a new Regex matcher.
     *
     * @param regex the regex
     */
    public RegexMatcher(String regex) {
        this(regex, 0);
    }

    /**
     * Instantiates a new Regex matcher.
     *
     * @param regex the regex
     * @param group the group
     */
    public RegexMatcher(String regex, int group) {
        this(regex, group, 0);
    }

    /**
     * Instantiates a new Regex matcher.
     *
     * @param regex the regex
     * @param group the group
     * @param flags the flags
     */
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
