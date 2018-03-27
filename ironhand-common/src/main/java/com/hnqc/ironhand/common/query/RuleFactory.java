package com.hnqc.ironhand.common.query;

import com.hnqc.ironhand.common.constants.Rule;

public class RuleFactory {
    public static Ruler createRuler(Rule rule) {
        switch (rule) {
            case EQUALS:
                return new EqualsRuler();
            case SCRIPT:
                return new LikeRuler();
            case REGEX:
                return new RegexRuler();
            default:
                return new EqualsRuler();
        }
    }
}
