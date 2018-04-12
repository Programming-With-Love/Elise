package com.hnqc.ironhand.spider.selector;

import java.util.List;

public interface Selectable {
    Selectable xpath(String xpath);

    Selectable css(String selector);

    Selectable css(String selector, String attrName);

    Selectable links();

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
