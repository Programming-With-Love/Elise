package site.zido.elise.scheduler;

import site.zido.elise.Page;
import site.zido.elise.Request;
import site.zido.elise.Task;
import site.zido.elise.downloader.Downloader;
import site.zido.elise.processor.BlankCrawResult;
import site.zido.elise.processor.CrawlResult;
import site.zido.elise.utils.ModuleNamedDefaultThreadFactory;

import java.util.concurrent.*;

/**
 * This default task scheduler provides thread-level task scheduling that is implemented from {@link TaskScheduler}.
 *
 * @author zido
 */
public class DefaultTaskScheduler extends AbstractDuplicateRemovedScheduler {

    private Downloader downloader;
    private AnalyzerListener analyzerListener;
    private final ThreadPoolExecutor executor;

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

    @Override
    public CrawlResult processPage(Task task, Request request, Page page) {
        return analyzerListener.onProcess(task, request, page);
    }

    @Override
    protected CrawlResult pushWhenNoDuplicate(Task task, Request request) {
        FutureTask<Page> future = (FutureTask<Page>) executor.submit(() -> downloader.download(task, request));
        try {
            Page page = future.get();

            return analyzerListener.onProcess(task, request, page);
        } catch (InterruptedException e) {
            //如果线程中断，停止解析，并返回空解析结果
            return new BlankCrawResult();
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public void setAnalyzer(TaskScheduler.AnalyzerListener listener) {
        this.analyzerListener = listener;
    }

}
