package site.zido.elise.select.api;

import site.zido.elise.select.Selector;

/**
 * The interface Selectable.
 *
 * @author zido
 */
public interface Selectable {
    /**
     * Select selectable.
     *
     * @param selector the selector
     * @return the selectable
     */
    Selectable select(Selector selector);
}
