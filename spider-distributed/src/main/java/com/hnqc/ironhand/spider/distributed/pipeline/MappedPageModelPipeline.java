package com.hnqc.ironhand.spider.distributed.pipeline;

import com.hnqc.ironhand.spider.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * map集合 page model处理类
 *
 * @author zido
 * @date 2018/04/12
 */
public class MappedPageModelPipeline implements CollectorPageModelPipeline<Map<String, Object>> {
    private List<Map<String, Object>> collected = new ArrayList<>();

    @Override
    public List<Map<String, Object>> getCollected() {
        return collected;
    }

    @Override
    public void process(Map<String, Object> t, Task task) {
        collected.add(t);
    }
}
