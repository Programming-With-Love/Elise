package site.zido.elise.scheduler;

import site.zido.elise.task.Task;

public interface OperationalTaskScheduler extends TaskScheduler {
    /**
     * Cancel.
     *
     * @param ifRunning the if running
     */
    void cancel(boolean ifRunning);

    /**
     * Cancel boolean.
     *
     * @param task      the task
     * @param ifRunning the if running
     * @return the boolean
     */
    boolean cancel(Task task, boolean ifRunning);

    /**
     * Pause boolean.
     *
     * @param task the task
     * @return the boolean
     */
    void pause(Task task);

    /**
     * Recover.
     *
     * @param task the task
     */
    void recover(Task task);
}
