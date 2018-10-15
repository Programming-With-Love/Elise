package site.zido.elise.scheduler;

import site.zido.elise.CrawlResult;
import site.zido.elise.Page;
import site.zido.elise.Request;
import site.zido.elise.Task;
import site.zido.elise.utils.ModuleNamedDefaultThreadFactory;

import java.util.concurrent.*;

/**
 * This message manager provides thread-level messaging that is implemented from {@link TaskScheduler}.
 *
 * @author zido
 */
public class SimpleTaskScheduler extends AbstractDuplicateRemovedScheduler implements MonitorableScheduler {

    protected final ExecutorService rootExecutor;
    protected LoadBalancer<DownloadListener> downloadListenerLoadBalancer;
    protected LoadBalancer<AnalyzerListener> analyzerListenerLoadBalancer;
    private int blockSize;

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
        rootExecutor = new ThreadPoolExecutor(blockSize, blockSize, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(blockSize), new ModuleNamedDefaultThreadFactory("task scheduler"));
    }

    @Override
    public CrawlResult process(Task task, Request request, Page page) {
        AnalyzerListener next = analyzerListenerLoadBalancer.getNext();
        if (next == null) {
            throw new NullPointerException("no analyzer");
        }
        return next.onProcess(task, request, page);
    }

    @Override
    protected Future<CrawlResult> pushWhenNoDuplicate(Task task, Request request) {
        return rootExecutor.submit(() -> {
            DownloadListener next = downloadListenerLoadBalancer.getNext();
            if (next == null) {
                throw new NullPointerException("no downloader");
            }
            return next.onDownload(task, request);
        });
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
