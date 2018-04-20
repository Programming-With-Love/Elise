package com.hnqc.ironhand.scheduler;

import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;

/**
 * duplication processor,It is an extension of the scheduler{@link Scheduler}.
 * Implementing this interface allows Task Scheduler to handle duplicate data
 *
 * @author zido
 * @date 2018/04/20
 */
public interface DuplicationProcessor {

    /**
     * Determine if this request is duplicate
     *
     * @param request request
     * @param task    task
     * @return true/false
     */
    boolean isDuplicate(Request request, Task task);

    /**
     * Reset all non-repeating sets of this task so that the task can send the same request as before
     *
     * @param task This is the task ID that needs to be cleared this time.
     *             It can be set according to its internal rules.
     *             try to ensure that this clearing is only relevant to the current task.
     */
    void resetDuplicateCheck(Task task);

    /**
     * Get the number of all requests provided under the current task
     *
     * @param task task
     * @return clientSize of all request under the current task
     */
    int getTotalRequestsCount(Task task);
}
