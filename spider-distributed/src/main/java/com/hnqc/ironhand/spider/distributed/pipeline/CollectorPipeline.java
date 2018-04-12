package com.hnqc.ironhand.spider.distributed.pipeline;

import com.hnqc.ironhand.spider.distributed.configurable.DefExtractor;
import com.hnqc.ironhand.spider.pipeline.Pipeline;

import java.util.List;

/**
 * 处理集合的Pipeline{@link DefExtractor#multi}
 *
 * @author zido
 * @date 2018/04/12
 */
public interface CollectorPipeline<T> extends Pipeline {
    /**
     * Get all results collected.
     *
     * @return collected results
     */
    public List<T> getCollected();
}
