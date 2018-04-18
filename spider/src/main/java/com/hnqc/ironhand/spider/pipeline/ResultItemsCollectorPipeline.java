package com.hnqc.ironhand.spider.pipeline;

import com.hnqc.ironhand.spider.ResultItem;
import com.hnqc.ironhand.spider.Task;

import java.util.ArrayList;
import java.util.List;

public class ResultItemsCollectorPipeline implements CollectorPipeline<ResultItem> {
    private List<ResultItem> collector = new ArrayList<>();

    @Override
    public List<ResultItem> getCollection() {
        return collector;
    }

    @Override
    public synchronized void process(ResultItem resultItem, Task task) {
        collector.add(resultItem);
    }
}
