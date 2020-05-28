package site.zido.elise.scheduler;

import site.zido.elise.Operator;
import site.zido.elise.http.Request;
import site.zido.elise.task.Task;
import site.zido.elise.task.api.ResponseHandler;

/**
 * 在内存中实现的单机调度器
 */
public class InMemoryScheduler extends AbstractScheduler {
    @Override
    protected void pushWhenNoDuplicate(Task task, Request request) {

    }

    @Override
    public Operator of(ResponseHandler handler) {
        return null;
    }

    @Override
    public void cancel(boolean ifRunning) {

    }
}
