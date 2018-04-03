package com.hnqc.ironhand.spider.selector;

import com.hnqc.ironhand.spider.utils.ValidateUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsSelectable implements Selectable {
    protected abstract List<String> getSourceTexts();

    @Override
    public Selectable css(String selector) {
        return $(selector);
    }

    @Override
    public Selectable css(String selector, String attrName) {
        return $(selector, attrName);
    }

    protected Selectable select(Selector selector, List<String> strings) {
        List<String> results = new ArrayList<>();
        for (String string : strings) {
            String select = selector.select(string);
            if (select != null) {
                results.add(select);
            }
        }
        return new PlainText(results);
    }

    protected Selectable selectList(Selector selector, List<String> strings) {
        List<String> results = new ArrayList<>();
        for (String string : strings) {
            List<String> result = selector.selectList(string);
            results.addAll(result);
        }
        return new PlainText(results);
    }

    @Override
    public List<String> all() {
        return getSourceTexts();
    }

    @Override
    public String get() {
        if (!ValidateUtils.isEmpty(all())) {
            return all().get(0);
        }
        return null;
    }

    @Override
    public Selectable select(Selector selector) {
        return select(selector, getSourceTexts());
    }

    @Override
    public Selectable selectList(Selector selector) {
        return selectList(selector, getSourceTexts());
    }

    public String getFirstSourceText() {
        List<String> sourceTexts = getSourceTexts();
        if (ValidateUtils.isEmpty(sourceTexts))
            return null;
        return sourceTexts.get(0);
    }

    @Override
    public Selectable regex(String regex) {
        RegexSelector regexSelector = new RegexSelector(regex);
        return selectList(regexSelector, getSourceTexts());
    }

    @Override
    public Selectable regex(String regex, int group) {
        RegexSelector regexSelector = new RegexSelector(regex, group);
        return selectList(regexSelector, getSourceTexts());
    }

    @Override
    public String toString() {
        return get();
    }

    @Override
    public boolean match() {
        return !ValidateUtils.isEmpty(getSourceTexts());
    }
}
