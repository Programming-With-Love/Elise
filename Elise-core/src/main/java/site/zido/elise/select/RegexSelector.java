package site.zido.elise.select;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexSelector implements Selector {
    private int group;
    private Pattern pattern;

    public RegexSelector() {

    }

    public RegexSelector(String regex) {
        this(regex, 0, 0);
    }

    public RegexSelector(String regex, int group) {
        this(regex, 0, group);
    }

    public RegexSelector(String regex, int group, int flags) {
        pattern = Pattern.compile(regex, flags);
        this.group = group;
    }

    @Override
    public List<Fragment> select(String text) {
        Matcher matcher = pattern.matcher(text);
        List<Fragment> results = new ArrayList<>();
        while (matcher.find()) {
            results.add(new Fragment(matcher.group(group)));
        }
        return results;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getGroup() {
        return group;
    }
}
