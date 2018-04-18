package com.hnqc.ironhand.spider.distributed.pipeline;

import com.hnqc.ironhand.spider.ResultItem;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.distributed.configurable.*;
import com.hnqc.ironhand.spider.pipeline.CollectorPipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * page model 集合处理
 *
 * @author zido
 * @date 2018/04/12
 */
public class PageModelCollectorPipeline<T> implements CollectorPipeline<T> {

    private final CollectorPageModelPipeline<T> pipeline;

    private ConfigurableModelExtractor extractor;

    private Transfer transfer = new DefaultTransfer();

    public PageModelCollectorPipeline(ConfigurableModelExtractor extractor, CollectorPageModelPipeline<T> pipeline) {
        this.extractor = extractor;
        this.pipeline = pipeline;
    }

    public PageModelCollectorPipeline(CollectorPageModelPipeline<T> pipeline) {
        this(null, pipeline);
    }

    /**
     * 默认转换器，仅转换map->map(其实相当于什么事都没做)，只是喂了兼容以及增加可扩展性
     *
     * @author zido
     * @date 2018/04/12
     */
    public static class DefaultTransfer implements Transfer<Object> {
        @Override
        public Map<String, Object> toMap(Object t) {
            if (Map.class.isAssignableFrom(t.getClass())) {
                return (Map<String, Object>) t;
            }
            return null;
        }

        @Override
        public Object toObj(Map<String, Object> map) {
            return map;
        }
    }

    @Override
    public List<T> getCollection() {
        return pipeline.getCollected();
    }

    @Override
    public void process(ResultItem resultItem, Task task) {
        Object o = resultItem.get(extractor.getModelExtractor().getName());
        if (o != null) {
            if (extractor == null || !extractor.getModelExtractor().getMulti()) {
                pipeline.process((T) transfer.toObj(resultItem.getAll()), task);
            } else {
                ArrayList<ResultItem> list = (ArrayList<ResultItem>) o;
                for (ResultItem items : list) {
                    pipeline.process((T) transfer.toObj(items.getAll()), task);
                }
            }
        }
    }

    public PageModelCollectorPipeline<T> setTransfer(Transfer<T> transfer) {
        this.transfer = transfer;
        return this;
    }
}
