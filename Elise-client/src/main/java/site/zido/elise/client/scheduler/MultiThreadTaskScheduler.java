package site.zido.elise.client.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.http.Request;
import site.zido.elise.http.Response;
import site.zido.elise.scheduler.AbstractScheduler;
import site.zido.elise.scheduler.Seed;
import site.zido.elise.scheduler.TaskScheduler;
import site.zido.elise.task.Task;
import site.zido.elise.utils.ModuleNamedDefaultThreadFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This multi-thread task scheduler provides thread-level task scheduling that is implemented from {@link TaskScheduler}.
 *
 * @author zido
 */
public class MultiThreadTaskScheduler extends AbstractScheduler implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiThreadTaskScheduler.class);
    private final ThreadPoolExecutor executor;
    private final LinkedBlockingQueue<Seed> queue = new LinkedBlockingQueue<>();
    private final ExecutorService rootExecutor = Executors.newFixedThreadPool(1, new ModuleNamedDefaultThreadFactory("task queue processor"));
    private final AtomicBoolean RUNNING = new AtomicBoolean(false);

    /**
     * Instantiates a new Default task scheduler.
     */
    public MultiThreadTaskScheduler() {
        this(Runtime.getRuntime().availableProcessors() * 2);
    }

    /**
     * Instantiates a new Default task scheduler.
     *
     * @param threadNum the thread num
     */
    public MultiThreadTaskScheduler(int threadNum) {
        this.executor = new ThreadPoolExecutor(threadNum,
                threadNum,
                1,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(),
                new ModuleNamedDefaultThreadFactory("default task scheduler"),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * Pre start.
     */
    private void preStart() {
        if (RUNNING.compareAndSet(false, true)) {
            rootExecutor.execute(this);
        }
    }

    /**
     * Process page.
     *
     * @param task     the task
     * @param request  the request
     * @param response the response
     */
    private void processPage(Task task, Request request, Response response) {
        preStart();
        queue.offer(new Seed(task, request, response));
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
            Response pollResponse = seed.getResponse();
            Request request = seed.getRequest();
            if (pollResponse == null) {
                try {
                    executor.execute(() -> {
                        Response response = super.onDownload(task, request);
                        processPage(task, request, response);
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
                    executor.execute(() -> super.onProcess(task, request, pollResponse));
                } catch (RejectedExecutionException e) {
                    if (executor.isShutdown()) {
                        LOGGER.info(Thread.currentThread().getName() + " is shutdown");
                    } else {
                        LOGGER.error("too more page");
                    }
                }
            }
        }
        LOGGER.debug("thread interrupted");
        //clear interrupted state
        final boolean interrupted = Thread.interrupted();
        if (interrupted) {
            //other thread can end the waiting state
            LOGGER.debug("try to wait child threads");
            while (!executor.isTerminated() && !Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignore) {
                }
            }
            //reset interrupted
            Thread.currentThread().interrupt();
        }
        super.onCancel();
        RUNNING.set(false);
    }

    @Override
    public void cancel(boolean ifRunning) {
        if (RUNNING.compareAndSet(true, false)) {
            rootExecutor.shutdownNow();
            if (ifRunning) {
                executor.shutdown();
            } else {
                executor.shutdownNow();
            }
        }
    }

}
