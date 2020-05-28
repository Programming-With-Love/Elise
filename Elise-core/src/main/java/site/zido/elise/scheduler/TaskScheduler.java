package site.zido.elise.scheduler;

import site.zido.elise.http.Request;
import site.zido.elise.task.Task;

/**
 * 任务调度器接口，主要用于接收并执行任务
 *
 * @author zido
 */
public interface TaskScheduler {

    /**
     * 在任务下执行某个请求
     *
     * @param task    任务
     * @param request 请求
     */
    void pushRequest(Task task, Request request);
}
