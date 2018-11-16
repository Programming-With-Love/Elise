package site.zido.elise.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.Page;
import site.zido.elise.Request;
import site.zido.elise.Task;
import site.zido.elise.utils.ModuleNamedDefaultThreadFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This default task scheduler provides thread-level task scheduling that is implemented from {@link TaskScheduler}.
 *
 * @author zido
 */
public class DefaultTaskScheduler extends ConfigurableScheduler implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTaskScheduler.class);
    private final ThreadPoolExecutor executor;
    private final LinkedBlockingQueue<Seed> queue = new LinkedBlockingQueue<>();
    private final ExecutorService rootExecutor = Executors.newFixedThreadPool(1, new ModuleNamedDefaultThreadFactory("task queue processor"));
    private final AtomicBoolean RUNNING = new AtomicBoolean(false);

    public DefaultTaskScheduler() {
        this(Runtime.getRuntime().availableProcessors() * 2);
    }

    public DefaultTaskScheduler(int threadNum) {
        this.executor = new ThreadPoolExecutor(threadNum,
                threadNum,
                1,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(),
                new ModuleNamedDefaultThreadFactory("default task scheduler"),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public void preStart() {
        if (RUNNING.compareAndSet(false, true)) {
            rootExecutor.execute(this);
        }
    }

    public void processPage(Task task, Request request, Page page) {
        preStart();
        queue.offer(new Seed(task, request, page));
    }

    @Override
    protected void pushWhenNoDuplicate(Task task, Request request) {
        preStart();
        queue.offer(new Seed(task, request));
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Seed seed;
            try {
                seed = queue.poll(1, TimeUnit.SECONDS);
                if (seed == null) {
                    continue;
                }
            } catch (InterruptedException e) {
                LOGGER.debug("received cancel signal");
                Thread.currentThread().interrupt();
                break;
            }
            Task task = seed.getTask();
            Page pollPage = seed.getPage();
            Request request = seed.getRequest();
            if (pollPage == null) {
                try {
                    executor.execute(() -> {
                        Page page = super.onDownload(task, request);
                        processPage(task, request, page);
                    });
                } catch (RejectedExecutionException e) {
                    if (executor.isShutdown()) {
                        LOGGER.info(Thread.currentThread().getName() + " is shutdown");
                    } else {
                        LOGGER.error("too more request");
                    }
                }
            } else {
                try {
                    executor.execute(() -> super.onProcess(task, request, pollPage));
                } catch (RejectedExecutionException e) {
                    if (executor.isShutdown()) {
                        LOGGER.info(Thread.currentThread().getName() + " is shutdown");
                    } else {
                        LOGGER.error("too more page");
                    }
                }
            }
        }
        //clear interrupted state
        Thread.interrupted();
        //other thread can end the waiting state
        while (!executor.isTerminated() && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) {
            }
        }
        //reset interrupted
        Thread.currentThread().interrupt();
        super.onCancel();
    }

    @Override
    public void cancel(boolean ifRunning) {
        rootExecutor.shutdownNow();
        if (ifRunning) {
            executor.shutdown();
        } else {
            executor.shutdownNow();
        }
    }
}
