package site.zido.elise.scheduler;

import site.zido.elise.CrawlResult;
import site.zido.elise.Page;
import site.zido.elise.Request;
import site.zido.elise.Task;

/**
 * This message manager provides thread-level messaging that is implemented from {@link TaskScheduler}.
 *
 * @author zido
 */
public class SyncTaskScheduler extends AbstractDuplicateRemovedScheduler {

    private DownloadListener downloadListener;
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
        return downloadListener.onDownload(task, request);
    }

    @Override
    public void setAnalyzer(TaskScheduler.AnalyzerListener listener) {
        this.analyzerListener = listener;
    }

    @Override
    public void setDownloader(TaskScheduler.DownloadListener listener) {
        this.downloadListener = listener;
    }
}
