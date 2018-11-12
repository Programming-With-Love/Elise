package site.zido.elise.scheduler;

import site.zido.elise.Page;
import site.zido.elise.Request;
import site.zido.elise.Task;
import site.zido.elise.downloader.Downloader;
import site.zido.elise.processor.CrawlResult;

/**
 * This message manager provides thread-level messaging that is implemented from {@link TaskScheduler}.
 *
 * @author zido
 */
public class SyncTaskScheduler extends AbstractDuplicateRemovedScheduler {

    private Downloader downloader;
    private AnalyzerListener analyzerListener;

    public SyncTaskScheduler() {
        this(new HashSetDeduplicationProcessor());
    }

    public SyncTaskScheduler(DuplicationProcessor duplicationProcessor) {
        super(duplicationProcessor);
    }

    @Override
    public CrawlResult processPage(Task task, Request request, Page page) {
        return analyzerListener.onProcess(task, request, page);
    }

    @Override
    protected CrawlResult pushWhenNoDuplicate(Task task, Request request) {
        Page page = downloader.download(task, request);
        return analyzerListener.onProcess(task, request, page);
    }

    @Override
    public void setAnalyzer(TaskScheduler.AnalyzerListener listener) {
        this.analyzerListener = listener;
    }

}
