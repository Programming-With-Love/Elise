package site.zido.elise.pipeline;

import site.zido.elise.ResultItem;
import site.zido.elise.Task;
import site.zido.elise.configurable.DefExtractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * pipeline汇总
 *
 * @author zido
 */
public class ModelPipeline implements Pipeline {
    /**
     *
     */
    private Map<String, PageModelPipeline<Object>> pageModelPipelines = new HashMap<>();
    private Map<String, DefExtractor> extractorMap = new HashMap<>();

    public ModelPipeline putPageModelPipeline(String name,
                                              PageModelPipeline<Object> pageModelPipeline,
                                              DefExtractor extractor) {
        pageModelPipelines.put(name, pageModelPipeline);
        extractorMap.put(name, extractor);
        return this;
    }

    public ModelPipeline putPageModelPipeline(String name,
                                              PageModelPipeline<Object> pageModelPipeline) {
        pageModelPipelines.put(name, pageModelPipeline);
        return this;
    }

    @Override
    public void process(ResultItem resultItem, Task task) {
        for (Map.Entry<String, PageModelPipeline<Object>> pipelineEntry : pageModelPipelines.entrySet()) {
            Object o = resultItem.get(pipelineEntry.getKey());
            if (o != null) {
                DefExtractor extractor = extractorMap.get(pipelineEntry.getKey());
                PageModelPipeline<Object> pageModelPipeline = pipelineEntry.getValue();
                if (extractor == null || !extractor.getMulti()) {
                    pageModelPipeline.process(o, task);
                } else {
                    ArrayList<Object> list = (ArrayList<Object>) o;
                    for (Object items : list) {
                        pageModelPipeline.process(items, task);
                    }
                }
            }

        }
    }
}
