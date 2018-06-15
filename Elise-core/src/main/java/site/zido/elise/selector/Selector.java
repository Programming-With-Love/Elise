package site.zido.elise.selector;

import java.util.List;

/**
 * 查询普通文本的selector
 *
 * @author zido
 */
public interface Selector {

    /**
     * Select string.
     *
     * @param text the text
     * @return the string
     */
    String select(String text);

    /**
     * Select list list.
     *
     * @param text the text
     * @return the list
     */
    List<String> selectList(String text);
}
