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

    /**
     * Incr.
     *
     * @param task     the task
     * @param num      the num
     * @param listener the listener
     */
    void incr(Task task, int num, CountListener listener);

    /**
     * Incr.
     *
     * @param task the task
     * @param num  the num
     */
    default void incr(Task task, int num) {
        incr(task, num, null);
    }

    /**
     * The interface Count listener.
     *
     * @author zido
     */
    @FunctionalInterface
    interface CountListener extends EventListener {
        /**
         * Result.
         *
         * @param num the num
         */
        void result(int num);
    }
}
