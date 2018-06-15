package site.zido.elise.selector;

import java.util.List;

/**
 * 选择接口
 *
 * @author zido
 */
public interface Selectable {
    /**
     * xpath选择器
     *
     * @param xpath xpath路径
     * @return 匹配出的内容 selectable
     */
    Selectable xpath(String xpath);

    /**
     * Css selectable.
     *
     * @param selector the selector
     * @return the selectable
     */
    Selectable css(String selector);

    /**
     * Css selectable.
     *
     * @param selector the selector
     * @param attrName the attr name
     * @return the selectable
     */
    Selectable css(String selector, String attrName);

    /**
     * Links selectable.
     *
     * @return the selectable
     */
    Selectable links();

    /**
     * Links selectable.
     *
     * @param choosers the choosers
     * @return the selectable
     */
    Selectable links(List<LinkProperty> choosers);

    /**
     * Regex selectable.
     *
     * @param regex the regex
     * @return the selectable
     */
    Selectable regex(String regex);

    /**
     * Regex selectable.
     *
     * @param regex the regex
     * @param group the group
     * @return the selectable
     */
    Selectable regex(String regex, int group);

    /**
     * toString
     *
     * @return str
     */
    @Override
    String toString();

    /**
     * Get string.
     *
     * @return the string
     */
    String get();

    /**
     * Match boolean.
     *
     * @return the boolean
     */
    boolean match();

    /**
     * All list.
     *
     * @return the list
     */
    List<String> all();

    /**
     * Select selectable.
     *
     * @param selector the selector
     * @return the selectable
     */
    Selectable select(Selector selector);

    /**
     * Select list selectable.
     *
     * @param selector the selector
     * @return the selectable
     */
    Selectable selectList(Selector selector);

    /**
     * Nodes list.
     *
     * @return the list
     */
    List<Selectable> nodes();
}
