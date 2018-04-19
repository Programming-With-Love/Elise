package com.hnqc.ironhand.message;

import com.hnqc.ironhand.Page;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Communication Manager,it's a sync message manager
 *
 * @author zido
 * @date 2018/04/19
 */
public class SimpleCommunicationManager extends AbstractContainerManager implements CommunicationManager {

    private static Logger logger = LoggerFactory.getLogger(SimpleCommunicationManager.class);

    public SimpleCommunicationManager(LoadBalancer loadBalancer) {
        super(loadBalancer);
    }

    public SimpleCommunicationManager() {
        super(new SimpleLoadBalancer());
    }


    @Override
    public void registerAnalyzer(AnalyzerListener listener) {
        register(TYPE_MESSAGE_ANALYZER, listener);
    }

    @Override
    public void registerDownloader(DownloadListener listener) {
        register(TYPE_MESSAGE_DOWNLOAD, listener);
    }

    @Override
    public void removeAnalyzer(AnalyzerListener analyzerListener) {
        remove(TYPE_MESSAGE_ANALYZER, analyzerListener);
    }

    @Override
    public void removeDownloader(DownloadListener downloadListener) {
        remove(TYPE_MESSAGE_DOWNLOAD, downloadListener);
    }

    @Override
    public void start() {
        super.start();
        logger.debug("simple message manager started to initialize,i'm sync");
    }

    @Override
    public void process(Task task, Request request, Page page) {
        ((AnalyzerListener) getTargetByType(TYPE_MESSAGE_ANALYZER)).onProcess(task, request, page);
    }

    @Override
    public void download(Task task, Request request) {
        ((DownloadListener) getTargetByType(TYPE_MESSAGE_DOWNLOAD)).onDownload(task, request);
    }

    @Override
    public int blockSize() {
        return 0;
    }
}
