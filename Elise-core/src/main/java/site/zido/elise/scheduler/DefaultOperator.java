package site.zido.elise.scheduler;

import site.zido.elise.Operator;
import site.zido.elise.events.SingleEventListener;
import site.zido.elise.events.SingleListenerContainer;
import site.zido.elise.http.Request;
import site.zido.elise.http.RequestBuilder;
import site.zido.elise.processor.ResultItem;
import site.zido.elise.processor.Saver;
import site.zido.elise.task.Task;
import site.zido.elise.utils.Asserts;

import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * default operator.
 *
 * @author zido
 */
public class DefaultOperator implements Operator, SingleListenerContainer.RecyclingCallback {
    private final Task task;
    private final AbstractScheduler scheduler;
    private final SingleListenerContainer container;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    /**
     * Instantiates a new Default operator.
     *
     * @param task      the task
     * @param scheduler the scheduler
     */
    public DefaultOperator(Task task, AbstractScheduler scheduler) {
        Asserts.notNull(task);
        Asserts.notNull(scheduler);
        this.scheduler = scheduler;
        this.task = task;
        container = new SingleListenerContainer(task);
        container.setCallback(this);
        scheduler.addEventListener(container);

    }

    @Override
    public Operator cancel(boolean ifRunning) {
        scheduler.cancel(task, ifRunning);
        return this;
    }

    @Override
    public Operator pause() {
        scheduler.pause(task);
        return this;
    }

    @Override
    public Operator recover() {
        scheduler.recover(task);
        return this;
    }

    @Override
    public Iterator<ResultItem> block() throws InterruptedException {
        lock.lock();
        try {
            condition.await();
        } finally {
            lock.unlock();
        }
        Saver saver = scheduler.getResponseProcessor().getSaver();
        return new Iterator<ResultItem>() {
            private ResultItem current;
            @Override
            public boolean hasNext() {
                return saver.hasNext(task,current);
            }

            @Override
            public ResultItem next() {
                return (current = saver.next(task,current));
            }
        };
    }

    @Override
    public Operator execute(String... url) {
        for (String s : url) {
            scheduler.pushRequest(task, RequestBuilder.get(s).build());
        }
        return this;
    }

    @Override
    public Operator execute(Request request) {
        scheduler.pushRequest(task, request);
        return this;
    }

    @Override
    public Operator addEventListener(SingleEventListener listener) {
        container.addListener(listener);
        return this;
    }

    @Override
    public void onRecycling() {
        lock.lock();
        try {
            scheduler.removeEventListener(container);
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
