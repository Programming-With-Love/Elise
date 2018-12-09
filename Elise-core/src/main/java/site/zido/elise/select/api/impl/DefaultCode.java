package site.zido.elise.select.api.impl;

import site.zido.elise.select.Matcher;
import site.zido.elise.select.Selector;
import site.zido.elise.select.api.Code;
import site.zido.elise.select.api.MatchResult;
import site.zido.elise.select.api.SelectResult;

public class DefaultCode implements Code {
    private Number number;

    public DefaultCode(Number number) {
        this.number = number;
    }

    @Override
    public Code saveAsString() {
        return null;
    }

    @Override
    public Code saveAsNumber() {
        return null;
    }

    @Override
    public MatchResult asTarget() {
        return null;
    }

    @Override
    public MatchResult asHelp() {
        return null;
    }

    @Override
    public MatchResult matches(Matcher matcher) {
        return null;
    }

    @Override
    public SelectResult as(String fieldName) {
        return null;
    }

    @Override
    public SelectResult saveText() {
        return null;
    }

    @Override
    public SelectResult select(Selector selector) {
        return null;
    }

    @Override
    public SelectResult nullable(boolean nullable) {
        return null;
    }
}
