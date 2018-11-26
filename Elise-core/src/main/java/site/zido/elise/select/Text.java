package site.zido.elise.select;

import java.util.List;

/**
 * The type Text.
 *
 * @author zido
 */
public class Text implements Selectable {
    private String text;

    /**
     * Instantiates a new Text.
     *
     * @param text the text
     */
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
