package site.zido.elise.select.api;

import site.zido.elise.select.Selector;

/**
 * The interface Selectable.
 *
 * @author zido
 */
public interface Selectable {
    Selectable select(Selector selector);
}
