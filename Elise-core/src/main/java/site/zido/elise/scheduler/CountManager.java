package site.zido.elise.scheduler;

import site.zido.elise.task.Task;

import java.util.EventListener;

/**
 * The interface Count manager.
 *
 * @author zido
 */
public interface CountManager {
    /**
     * Count int.
     *
     * @param task the task
     * @return the int
     */
    int count(Task task);

    int incr(Task task,int num);

    void release(Task task);
}
