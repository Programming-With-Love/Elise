package site.zido.elise.scheduler;

import site.zido.elise.CrawlResult;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * the task future
 *
 * @author zido
 */
public class TaskFuture implements Future<CrawlResult> {

    private volatile CrawlResult result;
    private volatile boolean isCancelled;
    private volatile boolean done = false;
    private final Lock lock = new ReentrantLock();
    private final Condition resultCondition = lock.newCondition();
    private volatile AtomicBoolean waiting = new AtomicBoolean(false);
    private final long taskId;

    public TaskFuture(long taskId) {
        this.taskId = taskId;
    }

    @Override
    public CrawlResult get() throws InterruptedException, ExecutionException {
        if (!waiting.compareAndSet(false, true)) {
            throw new ExecutionException(new Exception("more than one thread to wait the result"));
        }
        resultCondition.await();
        return this.result;
    }

    @Override
    public CrawlResult get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException {
        if (!waiting.compareAndSet(false, true)) {
            throw new ExecutionException(new Exception("more than one thread to wait the result"));
        }
        resultCondition.await(timeout, unit);
        return this.result;
    }

    public void set(CrawlResult result) {
        this.result = result;
        resultCondition.signalAll();
    }

    public void isCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return true;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void done() {
        this.done = true;
    }

    @Override
    public boolean isDone() {
        return this.done;
    }

}
