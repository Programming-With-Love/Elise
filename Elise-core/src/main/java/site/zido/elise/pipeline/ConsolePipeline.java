package site.zido.elise.pipeline;

import site.zido.elise.ResultItem;
import site.zido.elise.Task;

import java.util.List;
import java.util.Map;

/**
 * The type Console pipeline.
 *
 * @author zido
 */
public class ConsolePipeline implements Pipeline {
    @Override
    public void process(ResultItem resultItem, Task task) {
        System.out.println("get page: " + resultItem.getRequest().getUrl());

        Map<String, List<String>> all = resultItem.getAll();
        for (Map.Entry<String, List<String>> entry : all.entrySet()) {
            System.out.println(entry.getKey() + ":\t" + entry.getValue());
        }
    }
}
