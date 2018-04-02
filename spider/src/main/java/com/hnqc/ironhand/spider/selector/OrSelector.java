package com.hnqc.ironhand.spider.selector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrSelector implements Selector {
    private List<Selector> selectors = new ArrayList<Selector>();

    public OrSelector(Selector... selectors) {
        Collections.addAll(this.selectors, selectors);
    }

    public OrSelector(List<Selector> selectors) {
        this.selectors = selectors;
    }

    @Override
    public String select(String text) {
        for (Selector selector : selectors) {
            String result = selector.select(text);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public List<String> selectList(String text) {
        List<String> results = new ArrayList<>();
        for (Selector selector : selectors) {
            List<String> strings = selector.selectList(text);
            results.addAll(strings);
        }
        return results;
    }
}
