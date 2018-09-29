package site.zido.elise.scheduler;

import site.zido.elise.Page;
import site.zido.elise.Request;
import site.zido.elise.ResultItem;
import site.zido.elise.Task;

import java.util.concurrent.*;

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

    private ExecutorService childExecutor;

    private RequestManager requestManager;

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

    private void start() {
        while (!Thread.currentThread().isInterrupted()) {
            Request request;
            try {
                request = requestManager.nextRequest();
            } catch (InterruptedException e) {
                logger.debug("interrupted when get request");
                continue;
            }
            DownloadListener next = downloadListenerLoadBalancer.getNext();
            if (next == null) {
                throw new NullPointerException("no downloader");
            }
            childExecutor.submit(() -> next.onDownload(request));

        }
    }

    private void stop() {

    }

    @Override
    public ResultItem process(Request request, Page page) {
        AnalyzerListener next = analyzerListenerLoadBalancer.getNext();
        if (next == null) {
            throw new NullPointerException("no analyzer");
        }
        return next.onProcess(request, page);
    }

    @Override
    protected Future<ResultItem> pushWhenNoDuplicate(Request request) {
        requestManager.pushRequest(request);
        //TODO how to fix it?
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
