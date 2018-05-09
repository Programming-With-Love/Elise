package site.zido.elise.pipeline;

import site.zido.elise.ResultItem;
import site.zido.elise.Task;

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
