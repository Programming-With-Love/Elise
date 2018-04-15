package com.hnqc.ironhand.spider.distributed.pipeline;

import com.hnqc.ironhand.spider.ResultItems;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.distributed.configurable.DefExtractor;
import com.hnqc.ironhand.spider.distributed.configurable.DefRootExtractor;
import com.hnqc.ironhand.spider.distributed.configurable.Extractor;
import com.hnqc.ironhand.spider.distributed.configurable.Transfer;
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

    private DefRootExtractor extractor;

    private Transfer transfer = new DefaultTransfer();

    public PageModelCollectorPipeline(DefRootExtractor extractor, CollectorPageModelPipeline<T> pipeline) {
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
    public void process(ResultItems resultItems, Task task) {
        Object o = resultItems.get(extractor.getName());
        if (o != null) {
            if (extractor == null || !extractor.getMulti()) {
                pipeline.process((T) transfer.toObj(resultItems.getAll()), task);
            } else {
                ArrayList<ResultItems> list = (ArrayList<ResultItems>) o;
                for (ResultItems items : list) {
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
