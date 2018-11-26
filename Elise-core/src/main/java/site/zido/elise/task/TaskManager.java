package site.zido.elise.task;

import site.zido.elise.Task;

/**
 * The interface Task manager.
 *
 * @author zido
 */
public interface TaskManager {
    /**
     * Gets task.
     *
     * @param id the id
     * @return the task
     */
    Task getTask(Long id);

    /**
     * Add task boolean.
     *
     * @param task the task
     * @return the boolean
     */
    boolean addTask(Task task);

    /**
     * Update task boolean.
     *
     * @param task the task
     * @return the boolean
     */
    boolean updateTask(Task task);

    /**
     * Last task task.
     *
     * @return the task
     */
    Task lastTask();
}
