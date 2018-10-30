package site.zido.elise.select;

import java.util.List;

public class Text implements Selectable {
    private String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public List<Fragment> select(Selector selector) {
        return selector.select(text);
    }

    @Override
    public boolean match(Selector selector) {
        return selector.select(text) != null;
    }

    @Override
    public String toString() {
        return text;
    }
}
