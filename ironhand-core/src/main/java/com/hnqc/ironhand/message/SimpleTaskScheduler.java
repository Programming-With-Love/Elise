package com.hnqc.ironhand.message;

import com.hnqc.ironhand.Page;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;
import com.hnqc.ironhand.scheduler.HashSetDeduplicationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Communication Manager,it's a sync message manager
 *
 * @author zido
 * @date 2018/04/19
 */
public class SimpleTaskScheduler extends AbstractDuplicateRemovedScheduler {

    private static Logger logger = LoggerFactory.getLogger(SimpleTaskScheduler.class);
    private ExecutorContainer container;

    public SimpleTaskScheduler() {
        super(new HashSetDeduplicationProcessor());
        this.container = new ExecutorContainer();
    }


    @Override
    public void registerAnalyzer(AnalyzerListener listener) {
        container.register(TYPE_MESSAGE_ANALYZER, listener);
    }

    @Override
    public void registerDownloader(DownloadListener listener) {
        container.register(TYPE_MESSAGE_DOWNLOAD, listener);
    }

    @Override
    public void removeAnalyzer(AnalyzerListener analyzerListener) {
        container.remove(TYPE_MESSAGE_ANALYZER, analyzerListener);
    }

    @Override
    public void removeDownloader(DownloadListener downloadListener) {
        container.remove(TYPE_MESSAGE_DOWNLOAD, downloadListener);
    }

    @Override
    public void process(Task task, Request request, Page page) {
        ((AnalyzerListener) container.getTargetByType(TYPE_MESSAGE_ANALYZER)).onProcess(task, request, page);
    }

    @Override
    protected void pushWhenNoDuplicate(Request request, Task task) {
        ((DownloadListener) container.getTargetByType(TYPE_MESSAGE_DOWNLOAD)).onDownload(task, request);
    }
}
