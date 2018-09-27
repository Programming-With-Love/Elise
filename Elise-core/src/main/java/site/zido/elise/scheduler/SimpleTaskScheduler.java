package site.zido.elise.scheduler;

import site.zido.elise.Page;
import site.zido.elise.Request;
import site.zido.elise.task.DefaultMemoryTaskManager;
import site.zido.elise.task.TaskManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This message manager provides thread-level messaging that is implemented from {@link TaskScheduler}.
 *
 * @author zido
 */
public class SimpleTaskScheduler extends AbstractDuplicateRemovedScheduler implements MonitorableScheduler {

    protected LoadBalancer<DownloadListener> downloadListenerLoadBalancer;
    protected LoadBalancer<AnalyzerListener> analyzerListenerLoadBalancer;

    private int blockSize;

    protected final ExecutorService rootExecutor;

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
        rootExecutor = new ThreadPoolExecutor(blockSize, blockSize, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(blockSize), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    public void process(long taskId, Request request, Page page) {
        AnalyzerListener next = analyzerListenerLoadBalancer.getNext();
        if (next == null) {
            throw new NullPointerException("no analyzer");
        }
        rootExecutor.execute(() -> next.onProcess(taskId, request, page));
    }

    @Override
    protected void pushWhenNoDuplicate(Request request, long taskId) {
        DownloadListener next = downloadListenerLoadBalancer.getNext();
        if (next == null) {
            throw new NullPointerException("no downloader");
        }
        rootExecutor.execute(() -> next.onDownload(taskId, request));
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
