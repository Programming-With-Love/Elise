package site.zido.elise.select;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Regex selector.
 *
 * @author zido
 */
public class RegexSelector implements Selector {
    private int group;
    private Pattern pattern;

    /**
     * Instantiates a new Regex selector.
     */
    public RegexSelector() {

    }

    /**
     * Instantiates a new Regex selector.
     *
     * @param regex the regex
     */
    public RegexSelector(String regex) {
        this(regex, 0, 0);
    }

    /**
     * Instantiates a new Regex selector.
     *
     * @param regex the regex
     * @param group the group
     */
    public RegexSelector(String regex, int group) {
        this(regex, 0, group);
    }

    /**
     * Instantiates a new Regex selector.
     *
     * @param regex the regex
     * @param group the group
     * @param flags the flags
     */
    public RegexSelector(String regex, int group, int flags) {
        pattern = Pattern.compile(regex, flags);
        this.group = group;
    }

    @Override
    public List<String> select(String text) {
        Matcher matcher = pattern.matcher(text);
        List<String> results = new ArrayList<>();
        while (matcher.find()) {
            results.add(matcher.group(group));
        }
        return results;
    }

    /**
     * Gets pattern.
     *
     * @return the pattern
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Sets pattern.
     *
     * @param pattern the pattern
     */
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Gets group.
     *
     * @return the group
     */
    public int getGroup() {
        return group;
    }

    /**
     * Sets group.
     *
     * @param group the group
     */
    public void setGroup(int group) {
        this.group = group;
    }
}
