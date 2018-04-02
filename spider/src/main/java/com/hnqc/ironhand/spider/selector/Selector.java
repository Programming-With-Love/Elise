package com.hnqc.ironhand.spider.selector;

import java.util.List;

public interface Selector {

    String select(String text);

    List<String> selectList(String text);
}
