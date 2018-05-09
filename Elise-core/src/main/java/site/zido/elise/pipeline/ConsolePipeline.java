package site.zido.elise.pipeline;

import site.zido.elise.ResultItem;
import site.zido.elise.Task;

import java.util.Map;

public class ConsolePipeline implements Pipeline {
    @Override
    public void process(ResultItem resultItem, Task task) {
        System.out.println("get page: " + resultItem.getRequest().getUrl());

        Map<String, Object> all = resultItem.getAll();
        for (Map.Entry<String, Object> entry : all.entrySet()) {
            System.out.println(entry.getKey() + ":\t" + entry.getValue());
        }
    }
}
