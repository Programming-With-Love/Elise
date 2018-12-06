package site.zido.elise.scheduler;

import site.zido.elise.Operator;
import site.zido.elise.events.SingleEventListener;
import site.zido.elise.events.SingleListenerContainer;
import site.zido.elise.http.Http;
import site.zido.elise.http.Request;
import site.zido.elise.http.impl.DefaultRequest;
import site.zido.elise.task.Task;
import site.zido.elise.utils.Asserts;

import java.nio.charset.Charset;

/**
 * default operator.
 *
 * @author zido
 */
public class DefaultOperator implements Operator, SingleListenerContainer.RecyclingCallback {
    private final Task task;
    private final AbstractScheduler scheduler;
    private final SingleListenerContainer container;

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
        container = new SingleListenerContainer(task.getId());
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
    public Operator execute(String... url) {
        for (String s : url) {
            final DefaultRequest request = new DefaultRequest(s);
            request.setCharset(Charset.defaultCharset().name());
            request.setMethod(Http.Method.GET);
            scheduler.pushRequest(task, request);
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
        scheduler.removeEventListener(container);
    }
}
