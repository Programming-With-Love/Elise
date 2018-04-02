package com.hnqc.ironhand.spider.selector;

import org.jsoup.nodes.Element;

import java.util.List;

public interface ElementSelector {

    String select(Element element);

    List<String> selectList(Element element);
}
