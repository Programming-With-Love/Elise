package site.zido.elise.pipeline;

import site.zido.elise.Task;

/**
 * Implements PageModelPipeline to persistent your page model.
 *
 * @author zido
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
