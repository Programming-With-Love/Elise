package site.zido.elise.select;

import site.zido.elise.selector.Selector;

public class PlainText implements Selectable {
    private String text;

    public PlainText(String text) {
        this.text = text;
    }

    @Override
    public String select(Selector selector) {
        return selector.select(text);
    }

    @Override
    public boolean match(Selector selector) {
        return selector.select(text) != null;
    }
}
