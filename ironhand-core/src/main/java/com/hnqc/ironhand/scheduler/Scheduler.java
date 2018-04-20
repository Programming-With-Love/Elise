package com.hnqc.ironhand.scheduler;

import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;

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
     * @return true if push successful or false if push failed.
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
