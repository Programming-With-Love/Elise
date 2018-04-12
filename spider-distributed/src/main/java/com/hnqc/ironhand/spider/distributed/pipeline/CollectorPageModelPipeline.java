package com.hnqc.ironhand.spider.distributed.pipeline;

import com.hnqc.ironhand.spider.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 集合page model处理器
 *
 * @author zido
 * @date 2018/44/12
 */
public interface CollectorPageModelPipeline<T> extends PageModelPipeline<T> {

    /**
     * 获取集合
     *
     * @return
     */
    List<T> getCollected();
}
