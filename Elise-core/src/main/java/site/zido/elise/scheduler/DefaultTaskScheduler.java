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
public class DefaultTaskScheduler extends AbstractScheduler implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTaskScheduler.class);
    private final ThreadPoolExecutor executor;
    private final LinkedBlockingQueue<Seed> queue = new LinkedBlockingQueue<>();
    private final ExecutorService rootExecutor = Executors.newFixedThreadPool(1, new ModuleNamedDefaultThreadFactory("task queue processor"));
    private final AtomicBoolean RUNNING = new AtomicBoolean(false);

    public DefaultTaskScheduler() {
        this(Runtime.getRuntime().availableProcessors() * 2, new HashSetDeduplicationProcessor());
    }

    public DefaultTaskScheduler(int threadNum) {
        this(threadNum, new HashSetDeduplicationProcessor());
    }

    public DefaultTaskScheduler(int threadNum, DuplicationProcessor duplicationProcessor) {
        super(duplicationProcessor);
        this.executor = new ThreadPoolExecutor(threadNum,
                threadNum,
                1,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(),
                new ModuleNamedDefaultThreadFactory("default task scheduler"),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public void preStart() {
        if (!RUNNING.compareAndSet(false, true)) {
            return;
        }
        rootExecutor.execute(this);
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
                seed = queue.poll(1, TimeUnit.MINUTES);
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
                executor.execute(() -> {
                    Page page = super.onDownload(task, request);
                    processPage(task, request, page);
                });
            } else {
                executor.execute(() -> {
                    super.onProcess(task, request, pollPage);
                });
            }
        }
    }
}
