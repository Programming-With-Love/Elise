package site.zido.elise.task;

import site.zido.elise.Task;

public interface TaskManager {
    Task getTask(Long id);

    boolean addTask(Task task);

    boolean updateTask(Task task);

    Task lastTask();
}
