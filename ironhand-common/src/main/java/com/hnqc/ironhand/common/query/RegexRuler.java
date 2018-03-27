package com.hnqc.ironhand.common.query;

import java.util.regex.Pattern;

public class RegexRuler implements Ruler {
    private Pattern pattern;

    @Override
    public void init(String origin) {
        this.pattern = Pattern.compile(origin);
    }

    @Override
    public boolean match(String target) {
        return pattern.matcher(target).find();
    }
}
