package com.hnqc.ironhand.spider.scheduler;

import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;

/**
 * Scheduler
 *
 * @author zido
 * @date 2018/04/18
 */
public interface Scheduler {
    /**
     * push new request to scheduler.
     *
     * @param request request
     * @param task    task
     */
    void push(Request request, Task task);

    /**
     * poll a request.
     *
     * @param task task
     * @return request
     */
    Request poll(Task task);
}
