package com.hnqc.ironhand.spider.selector;

import java.util.ArrayList;
import java.util.List;

public class PlainText extends AbsSelectable {
    protected List<String> sourceTexts;

    public PlainText(List<String> sourceTexts) {
        this.sourceTexts = sourceTexts;
    }

    public PlainText(String text) {
        this.sourceTexts = new ArrayList<>();
        sourceTexts.add(text);
    }

    @Override
    public Selectable xpath(String xpath) {
        throw new UnsupportedOperationException("xpath not support plaintext");
    }

    public Selectable $(String selector) {
        throw new UnsupportedOperationException("$ not support plaintext");
    }

    @Override
    public Selectable $(String selector, String attrName) {
        throw new UnsupportedOperationException("$ can not apply to plain text");
    }

    @Override
    public Selectable links() {
        throw new UnsupportedOperationException("Links can not apply to plain text");
    }

    @Override
    public List<Selectable> nodes() {
        List<Selectable> nodes = new ArrayList<>();
        for (String s : getSourceTexts()) {
            nodes.add(new PlainText(s));
        }
        return nodes;
    }


    @Override
    protected List<String> getSourceTexts() {
        return sourceTexts;
    }
}
