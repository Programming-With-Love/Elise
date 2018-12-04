package site.zido.elise.scheduler;

import site.zido.elise.Operator;
import site.zido.elise.events.SingleEventListener;
import site.zido.elise.events.SingleListenerContainer;
import site.zido.elise.http.impl.DefaultRequest;
import site.zido.elise.task.Task;
import site.zido.elise.utils.Asserts;

public class DefaultOperator implements Operator {
    private final Task task;
    private final AbstractScheduler scheduler;

    public DefaultOperator(Task task, AbstractScheduler scheduler) {
        Asserts.notNull(task);
        Asserts.notNull(scheduler);
        this.scheduler = scheduler;
        this.task = task;
    }

    @Override
    public void cancel(boolean ifRunning) {
        scheduler.cancel(task, ifRunning);
    }

    @Override
    public boolean pause() {
        return scheduler.pause(task);
    }

    @Override
    public void recover() {
        scheduler.recover(task);
    }

    @Override
    public Operator addUrl(String... url) {
        for (String s : url) {
            scheduler.pushRequest(task, new DefaultRequest(s));
        }
        return this;
    }

    @Override
    public void addEventListener(SingleEventListener listener) {
        //TODO add single event listener
    }
}
