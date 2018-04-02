package com.hnqc.ironhand.spider.pipeline;

import com.hnqc.ironhand.spider.ResultItems;
import com.hnqc.ironhand.spider.Task;

import java.util.ArrayList;
import java.util.List;

public class ResultItemsCollectorPipeline implements CollectorPipeline<ResultItems> {
    private List<ResultItems> collector = new ArrayList<>();

    @Override
    public List<ResultItems> getCollection() {
        return collector;
    }

    @Override
    public synchronized void process(ResultItems resultItems, Task task) {
        collector.add(resultItems);
    }
}
