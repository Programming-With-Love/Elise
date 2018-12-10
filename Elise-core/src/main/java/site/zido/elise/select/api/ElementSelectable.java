package site.zido.elise.select.api;

import site.zido.elise.select.CssSelector;
import site.zido.elise.select.ElementSelector;
import site.zido.elise.select.XpathSelector;

public interface ElementSelectable {
    ElementSelectable partition(ElementSelector elementSelector);

    ElementValue select(ElementSelector selector);

    default ElementValue css(String css) {
        return select(new CssSelector(css));
    }

    default ElementValue xpath(String xpath) {
        return select(new XpathSelector(xpath));
    }
}
