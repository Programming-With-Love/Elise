package com.hnqc.ironhand.scheduler;

import com.hnqc.ironhand.Page;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This message manager provides thread-level messaging that is implemented from {@link TaskScheduler}.
 * <p>
 * This class is thread-safe. In addition to the efficiency issues,
 * you can rest assured that other concerns can be used with confidence.
 * <p>
 * This is the default stand-alone task scheduling management,
 * is thread-level and very simple, follow-up may add a better scheduling management solution,
 * currently only provides a simple implementation.
 * <p>
 * it based on {@link ThreadPoolExecutor}
 * <p>
 * Even if the internal download listener and analysis listener are all null,
 * it can still be used as a sending client to send messages.
 *
 * @author zido
 * @date 2018/04/16
 */
public class SimpleTaskScheduler extends AbstractDuplicateRemovedScheduler implements MonitorableScheduler {
    private LoadBalancer<DownloadListener> downloadListenerLoadBalancer;
    private LoadBalancer<AnalyzerListener> analyzerListenerLoadBalancer;

    protected AtomicInteger stat = new AtomicInteger(STAT_INIT);

    protected final static int STAT_INIT = 0;

    protected final static int STAT_RUNNING = 1;

    protected final static int STAT_STOPPED = 2;

    private final ThreadPoolExecutor childExecutor;

    private Lock taskLock = new ReentrantLock();
    private Condition taskCondition = taskLock.newCondition();

    private int emptySleepTime = 30000;

    private final ThreadPoolExecutor rootExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }
    });
    /**
     * message queue
     */
    private final Queue<Keeper> QUEUE = new ConcurrentLinkedQueue<>();
    /**
     * logger
     */
    private final static Logger logger = LoggerFactory.getLogger(SimpleTaskScheduler.class);


    public SimpleTaskScheduler setPoolSize(int poolSize) {
        this.childExecutor.setCorePoolSize(poolSize);
        this.childExecutor.setMaximumPoolSize(poolSize);
        return this;
    }

    public SimpleTaskScheduler setDownloadListenerLoadBalancer(LoadBalancer<DownloadListener> downloadListenerLoadBalancer) {
        this.downloadListenerLoadBalancer = downloadListenerLoadBalancer;
        return this;
    }

    public SimpleTaskScheduler setAnalyzerListenerLoadBalancer(LoadBalancer<AnalyzerListener> analyzerListenerLoadBalancer) {
        this.analyzerListenerLoadBalancer = analyzerListenerLoadBalancer;
        return this;
    }

    public SimpleTaskScheduler() {
        this(new HashSetDeduplicationProcessor(), new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("thread child message manager");
                    return thread;
                }));
    }

    public SimpleTaskScheduler(DuplicationProcessor duplicationProcessor) {
        this(duplicationProcessor, new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("thread child message manager");
                    return thread;
                }));
    }

    public SimpleTaskScheduler(DuplicationProcessor duplicationProcessor, ThreadPoolExecutor childExecutor) {
        super(duplicationProcessor);
        this.childExecutor = childExecutor;
        this.analyzerListenerLoadBalancer = new SimpleLoadBalancer<>();
        this.downloadListenerLoadBalancer = new SimpleLoadBalancer<>();
    }

    private boolean checkRunningStat() {
        while (true) {
            int statNow = stat.get();
            if (statNow == STAT_RUNNING) {
                return true;
            }
            if (stat.compareAndSet(statNow, STAT_RUNNING)) {
                break;
            }
        }
        return false;
    }

    public SimpleTaskScheduler setEmptySleepTime(int emptySleepTime) {
        this.emptySleepTime = emptySleepTime;
        return this;
    }

    private static class Keeper {
        Task task;
        Request request;

        Keeper(Task task, Request request) {
            this.task = task;
            this.request = request;
        }
    }

    private static class PageKeeper extends Keeper {
        Page page;

        PageKeeper(Task task, Request request, Page page) {
            super(task, request);
            this.page = page;
        }
    }

    private void start() {
        if (checkRunningStat()) {
            return;
        }
        rootExecutor.execute(() -> {
            logger.debug("thread listening...");
            while (!Thread.currentThread().isInterrupted() && stat.get() == STAT_RUNNING) {
                Keeper poll = QUEUE.poll();
                if (poll != null) {
                    if (poll instanceof PageKeeper) {
                        AnalyzerListener next = analyzerListenerLoadBalancer.getNext();
                        if (next == null) {
                            QUEUE.offer(poll);
                            continue;
                        }
                        childExecutor.execute(() -> next.onProcess(poll.task, poll.request, ((PageKeeper) poll).page));
                    } else {
                        DownloadListener next = downloadListenerLoadBalancer.getNext();
                        if (next == null) {
                            QUEUE.offer(poll);
                            continue;
                        }
                        childExecutor.execute(() -> next.onDownload(poll.task, poll.request));
                    }
                } else {
                    waitTask();
                }
            }
        });
    }

    private void waitTask() {
        taskLock.lock();
        try {
            taskCondition.await(emptySleepTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.warn("wait task - interrupted, error {}", e);
        } finally {
            taskLock.unlock();
        }
    }

    private void signalTask() {
        try {
            taskLock.lock();
            taskCondition.signalAll();
        } finally {
            taskLock.unlock();
        }
    }

    public void stop() {
        if (stat.compareAndSet(STAT_RUNNING, STAT_STOPPED)) {
            logger.info("message listener stop success!");
        } else {
            logger.info("message listener stop fail!");
        }
    }

    @Override
    public void process(Task task, Request request, Page page) {
        QUEUE.offer(new PageKeeper(task, request, page));
        signalTask();
    }

    @Override
    protected void pushWhenNoDuplicate(Request request, Task task) {
        QUEUE.offer(new Keeper(task, request));
        signalTask();
    }

    @Override
    public int downloaderSize() {
        return downloadListenerLoadBalancer.size();
    }

    @Override
    public int analyzerSize() {
        return analyzerListenerLoadBalancer.size();
    }

    @Override
    public int blockSize() {
        return QUEUE.size();
    }

    @Override
    public void registerAnalyzer(TaskScheduler.AnalyzerListener listener) {
        analyzerListenerLoadBalancer.add(listener);
        start();
    }

    @Override
    public void registerDownloader(TaskScheduler.DownloadListener listener) {
        downloadListenerLoadBalancer.add(listener);
        start();
    }

    @Override
    public void removeAnalyzer(TaskScheduler.AnalyzerListener listener) {
        analyzerListenerLoadBalancer.remove(listener);
    }

    @Override
    public void removeDownloader(TaskScheduler.DownloadListener listener) {
        downloadListenerLoadBalancer.remove(listener);
    }
}
