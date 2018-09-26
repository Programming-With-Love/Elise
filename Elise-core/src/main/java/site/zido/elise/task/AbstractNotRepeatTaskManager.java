package site.zido.elise.task;

import site.zido.elise.Site;
import site.zido.elise.Task;

public abstract class AbstractNotRepeatTaskManager implements TaskManager {
    private Task lastTask = new Site().toTask();

    @Override
    public void addTask(Task task) {
        lastTask = task;
        if (getTask(task.getId()) != null) {
            return;
        }
        addNotRepeat(task);
    }

    /**
     * add task
     * @param task task
     */
    protected abstract void addNotRepeat(Task task);

    @Override
    public Task lastTask() {
        return lastTask;
    }
}
