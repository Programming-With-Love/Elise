package site.zido.elise.scheduler;

import site.zido.elise.Task;

import java.util.EventListener;

public interface CountManager {
    int count(Task task);

    void incr(Task task, int num, CountListener listener);

    default void incr(Task task, int num) {
        incr(task, num, null);
    }

    @FunctionalInterface
    interface CountListener extends EventListener {
        void result(int num);
    }
}
