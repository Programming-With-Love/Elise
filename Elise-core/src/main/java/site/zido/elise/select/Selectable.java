package site.zido.elise.select;

import java.util.List;

/**
 * The interface Selectable.
 *
 * @author zido
 */
public interface Selectable extends Matchable {
    /**
     * Select list.
     *
     * @param selector the selector
     * @return the list
     */
    List<Fragment> select(Selector selector);
}
