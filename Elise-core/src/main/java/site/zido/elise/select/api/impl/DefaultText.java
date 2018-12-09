package site.zido.elise.select.api.impl;

import site.zido.elise.select.Matcher;
import site.zido.elise.select.Selector;
import site.zido.elise.select.api.MatchResult;
import site.zido.elise.select.api.SelectResult;
import site.zido.elise.select.api.Text;

public class DefaultText implements Text {
    protected String text;

    public DefaultText(String string) {
        this.text = string;
    }

    @Override
    public SelectResult as(String fieldName) {
        return null;
    }

    @Override
    public Text saveText() {
        return null;
    }

    @Override
    public SelectResult nullable(boolean nullable) {
        return null;
    }

    @Override
    public Text region(Selector selector) {
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
    public SelectResult select(Selector selector) {
        return null;
    }

    @Override
    public MatchResult matches(Matcher matcher) {
        return null;
    }
}
