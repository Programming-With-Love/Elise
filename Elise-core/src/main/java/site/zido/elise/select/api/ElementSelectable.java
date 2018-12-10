package site.zido.elise.select.api;

import site.zido.elise.select.CssSelector;
import site.zido.elise.select.ElementSelector;
import site.zido.elise.select.XpathSelector;

/**
 * The interface Element selectable.
 *
 * @author zido
 */
public interface ElementSelectable {
    /**
     * Partition element selectable.
     *
     * @param elementSelector the element selector
     * @return the element selectable
     */
    ElementSelectable partition(ElementSelector elementSelector);

    /**
     * Select element value.
     *
     * @param selector the selector
     * @return the element value
     */
    ElementValue select(ElementSelector selector);

    /**
     * Css element value.
     *
     * @param css the css
     * @return the element value
     */
    default ElementValue css(String css) {
        return select(new CssSelector(css));
    }

    /**
     * Xpath element value.
     *
     * @param xpath the xpath
     * @return the element value
     */
    default ElementValue xpath(String xpath) {
        return select(new XpathSelector(xpath));
    }
}
