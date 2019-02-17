package site.zido.elise.scheduler;

import site.zido.elise.Operator;
import site.zido.elise.task.Task;

import java.util.List;

public interface SpiderContext {
    byte STATE_START = 0;
    byte STATE_PAUSE = 1;
    byte STATE_CANCEL = 2;
    byte STATE_CANCEL_NOW = 3;
    Operator init(Task task, AbstractScheduler scheduler);

    byte getState(Task task);

    Operator getOperator(Task task);

    List<Seed> getSeeds(Task task);

    void release(Task task);

    void setState(Task task, byte newState);

    void setState(byte oldState,byte newState);

    List<Task> getTasksByState(byte state);
}
