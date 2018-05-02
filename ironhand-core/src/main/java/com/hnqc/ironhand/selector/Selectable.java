package com.hnqc.ironhand.selector;

import java.util.List;

/**
 * 选择接口
 *
 * @author zido
 * @date 2018/26/13
 */
public interface Selectable {
    /**
     * xpath选择器
     *
     * @param xpath xpath路径
     * @return 匹配出的内容
     */
    Selectable xpath(String xpath);

    Selectable css(String selector);

    Selectable css(String selector, String attrName);

    Selectable links();

    Selectable links(List<LinkProperty> choosers);

    Selectable regex(String regex);

    Selectable regex(String regex, int group);

    @Override
    String toString();

    String get();

    boolean match();

    List<String> all();

    Selectable select(Selector selector);

    Selectable selectList(Selector selector);

    List<Selectable> nodes();
}
