package site.zido.elise.task;

import site.zido.elise.Task;

public interface TaskManager {
    Task getTask(Long id);

    void addTask(Task task);

    Task lastTask();
}
