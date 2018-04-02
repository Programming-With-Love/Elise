package com.hnqc.ironhand.spider.selector;

import java.util.List;

public interface Selectable {
    Selectable xpath(String xpath);

    Selectable $(String selector);

    Selectable $(String selector, String attrName);

    Selectable css(String selector);

    Selectable css(String selector, String attrName);

    Selectable links();

    Selectable regex(String regex);

    Selectable regex(String regex, int group);

    String toString();

    String get();

    boolean match();

    List<String> all();

    Selectable select(Selector selector);

    Selectable selectList(Selector selector);

    List<Selectable> nodes();
}
