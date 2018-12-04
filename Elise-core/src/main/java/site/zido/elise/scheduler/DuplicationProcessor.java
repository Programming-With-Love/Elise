package site.zido.elise.scheduler;

import site.zido.elise.task.Task;
import site.zido.elise.http.Request;

/**
 * duplication processor,It is an extension of the scheduler{@link TaskScheduler}.
 * Implementing this interface allows Task Scheduler to handle duplicate data
 *
 * @author zido
 */
public interface DuplicationProcessor {

    /**
     * Determine if this request is duplicate
     *
     * @param task    the task
     * @param request request
     * @return true /false
     */
    boolean isDuplicate(Task task, Request request);

    /**
     * Reset all non-repeating sets of this task so that the task can send the same request as before
     *
     * @param task This is the task ID that needs to be cleared this time.             It can be set according to its internal rules.             try to ensure that this clearing is only relevant to the current task.
     */
    void resetDuplicateCheck(Task task);

    /**
     * Get the number of all requests provided under the current task
     *
     * @param task task
     * @return downloaderSize of all request under the current task
     */
    int getTotalRequestsCount(Task task);
}
