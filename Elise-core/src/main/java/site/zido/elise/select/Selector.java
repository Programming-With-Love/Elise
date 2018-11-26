package site.zido.elise.select;

import java.util.List;

/**
 * The interface Selector.
 *
 * @author zido
 */
public interface Selector {
    /**
     * Select list.
     *
     * @param text the text
     * @return the list
     */
    List<Fragment> select(String text);
}
