package com.hnqc.ironhand.common.pojo;

import com.hnqc.ironhand.common.constants.Rule;

public class UrlRule {
    private Rule rule;
    private String value;

    public UrlRule(Rule rule, String value) {
        this.rule = rule;
        this.value = value;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
