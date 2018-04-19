package com.hnqc.ironhand.spider.pipeline;

import com.hnqc.ironhand.spider.Task;

/**
 * Implements PageModelPipeline to persistent your page model.
 *
 * @author zido
 * @date 2018/04/12
 */
public interface PageModelPipeline<T> {

    /**
     * 处理model
     *
     * @param t    model
     * @param task 任务
     */
    void process(T t, Task task);

}
