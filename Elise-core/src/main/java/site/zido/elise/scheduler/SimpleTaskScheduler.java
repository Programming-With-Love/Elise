package site.zido.elise.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.Page;
import site.zido.elise.Request;
import site.zido.elise.Task;

import java.util.concurrent.*;

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
 */
public class SimpleTaskScheduler extends AbstractDuplicateRemovedScheduler implements MonitorableScheduler {
    /**
     * logger
     */
    private final static Logger logger = LoggerFactory.getLogger(SimpleTaskScheduler.class);

    protected LoadBalancer<DownloadListener> downloadListenerLoadBalancer;
    protected LoadBalancer<AnalyzerListener> analyzerListenerLoadBalancer;

    private int blockSize;

    protected final Executor rootExecutor;

    public SimpleTaskScheduler() {
        this(1);
    }

    public SimpleTaskScheduler(int blockSize) {
        this(blockSize, new HashSetDeduplicationProcessor());
    }

    public SimpleTaskScheduler(int blockSize, DuplicationProcessor duplicationProcessor) {
        super(duplicationProcessor);
        if (blockSize < 1) {
            throw new IllegalArgumentException("blockSize can't be less than 1");
        }
        this.analyzerListenerLoadBalancer = new SimpleLoadBalancer<>();
        this.downloadListenerLoadBalancer = new SimpleLoadBalancer<>();
        this.blockSize = blockSize;
        rootExecutor = new ThreadPoolExecutor(blockSize, blockSize, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(blockSize),new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    public void process(Task task, Request request, Page page) {
        AnalyzerListener next = analyzerListenerLoadBalancer.getNext();
        if (next == null) {
            throw new NullPointerException("no analyzer");
        }
        rootExecutor.execute(() -> next.onProcess(task, request, page));
    }

    @Override
    protected void pushWhenNoDuplicate(Request request, Task task) {
        DownloadListener next = downloadListenerLoadBalancer.getNext();
        if (next == null) {
            throw new NullPointerException("no downloader");
        }
        rootExecutor.execute(() -> next.onDownload(task, request));
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
        return blockSize;
    }

    @Override
    public void registerAnalyzer(TaskScheduler.AnalyzerListener listener) {
        analyzerListenerLoadBalancer.add(listener);
    }

    @Override
    public void registerDownloader(TaskScheduler.DownloadListener listener) {
        downloadListenerLoadBalancer.add(listener);
    }

    @Override
    public void removeAnalyzer(TaskScheduler.AnalyzerListener listener) {
        analyzerListenerLoadBalancer.remove(listener);
    }

    @Override
    public void removeDownloader(TaskScheduler.DownloadListener listener) {
        downloadListenerLoadBalancer.remove(listener);
    }

    public SimpleTaskScheduler setDownloadListenerLoadBalancer(LoadBalancer<DownloadListener> downloadListenerLoadBalancer) {
        this.downloadListenerLoadBalancer = downloadListenerLoadBalancer;
        return this;
    }

    public SimpleTaskScheduler setAnalyzerListenerLoadBalancer(LoadBalancer<AnalyzerListener> analyzerListenerLoadBalancer) {
        this.analyzerListenerLoadBalancer = analyzerListenerLoadBalancer;
        return this;
    }

}
