package com.hnqc.ironhand.selector;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapping a string as selectable plain text
 *
 * @author zido
 * @date 2018/04/19
 */
public class PlainText extends AbstractSelectable {
    protected List<String> sourceTexts;

    public PlainText() {

    }

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

    @Override
    public Selectable css(String selector) {
        throw new UnsupportedOperationException("$ not support plaintext");
    }

    @Override
    public Selectable css(String selector, String attrName) {
        throw new UnsupportedOperationException("$ can not apply to plain text");
    }

    @Override
    public Selectable links() {
        throw new UnsupportedOperationException("Links can not apply to plain text");
    }

    @Override
    public Selectable links(List<LinkProperty> choosers) {
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
    public List<String> getSourceTexts() {
        return sourceTexts;
    }
}
