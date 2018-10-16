package site.zido.elise.select;

import site.zido.elise.selector.ElementSelector;

public interface ElementSelectable extends Selectable {
    String select(ElementSelector selector);
}
