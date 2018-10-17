package site.zido.elise.select;

import java.util.List;

public class PlainText implements Selectable {
    private String text;

    public PlainText(String text) {
        this.text = text;
    }

    @Override
    public List<String> select(Selector selector) {
        return selector.select(text);
    }

    @Override
    public boolean match(Selector selector) {
        return selector.select(text) != null;
    }
}
