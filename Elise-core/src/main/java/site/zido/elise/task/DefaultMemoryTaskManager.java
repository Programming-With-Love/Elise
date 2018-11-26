package site.zido.elise.task;

import site.zido.elise.Task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Default memory task manager.
 *
 * @author zido
 */
public class DefaultMemoryTaskManager extends AbstractNotRepeatTaskManager {
    private Map<Long, Task> taskMap = new ConcurrentHashMap<>(7);

    @Override
    public Task getTask(Long id) {
        return taskMap.get(id);
    }

    @Override
    public boolean updateTask(Task task) {
        taskMap.put(task.getId(), task);
        return true;
    }

    @Override
    protected boolean addNotRepeat(Task task) {
        taskMap.put(task.getId(), task);
        return true;
    }
}
