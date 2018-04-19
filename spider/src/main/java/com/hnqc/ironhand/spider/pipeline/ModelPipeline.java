package com.hnqc.ironhand.spider.pipeline;

import com.hnqc.ironhand.spider.ResultItem;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.configurable.Extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * pipeline汇总
 *
 * @author zido
 * @date 2018/04/12
 */
public class ModelPipeline implements Pipeline {
    /**
     *
     */
    private Map<String, PageModelPipeline> pageModelPipelines = new HashMap<>();
    private Map<String, Extractor> extractorMap = new HashMap<>();

    public ModelPipeline putPageModelPipeline(String name,
                                              PageModelPipeline pageModelPipeline,
                                              Extractor extractor) {
        pageModelPipelines.put(name, pageModelPipeline);
        extractorMap.put(name, extractor);
        return this;
    }

    public ModelPipeline putPageModelPipeline(String name,
                                              PageModelPipeline pageModelPipeline) {
        pageModelPipelines.put(name, pageModelPipeline);
        return this;
    }

    @Override
    public void process(ResultItem resultItem, Task task) {
        for (Map.Entry<String, PageModelPipeline> pipelineEntry : pageModelPipelines.entrySet()) {
            Object o = resultItem.get(pipelineEntry.getKey());
            if (o != null) {
                Extractor extractor = extractorMap.get(pipelineEntry.getKey());
                PageModelPipeline pageModelPipeline = pipelineEntry.getValue();
                if (extractor == null || !extractor.getMulti()) {
                    pageModelPipeline.process(o, task);
                } else {
                    ArrayList<Object> list = (ArrayList<Object>) o;
                    for (Object items : list) {
                        pageModelPipeline.process(o, task);
                    }
                }
            }

        }
    }
}
