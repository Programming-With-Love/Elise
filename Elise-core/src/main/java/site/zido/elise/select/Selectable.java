package site.zido.elise.select;

import site.zido.elise.selector.Selector;

import java.util.List;

public interface Selectable {
    String select(Selector selector);

    boolean match(Selector selector);

}
