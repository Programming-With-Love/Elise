package site.zido.elise.scheduler;

import site.zido.elise.downloader.Downloader;
import site.zido.elise.processor.ResponseHandler;


/**
 * The type Configurable scheduler.
 *
 * @author zido
 */
public abstract class ConfigurableScheduler extends AbstractScheduler {
    private Downloader downloader;
    private ResponseHandler ResponseHandler;
    private CountManager countManager;
    private DuplicationProcessor duplicationProcessor;

    @Override
    public Downloader getDownloader() {
        return downloader;
    }

    @Override
    public ResponseHandler getResponseHandler() {
        return ResponseHandler;
    }

    @Override
    public CountManager getCountManager() {
        return countManager;
    }

    @Override
    public DuplicationProcessor getDuplicationProcessor() {
        return duplicationProcessor;
    }

    /**
     * Sets downloader.
     *
     * @param downloader the downloader
     */
    public void setDownloader(Downloader downloader) {
        this.downloader = downloader;
    }

    /**
     * Sets response handler.
     *
     * @param responseHandler the response handler
     */
    public void setResponseHandler(ResponseHandler responseHandler) {
        this.ResponseHandler = responseHandler;
    }

    /**
     * Sets count manager.
     *
     * @param countManager the count manager
     */
    public void setCountManager(CountManager countManager) {
        this.countManager = countManager;
    }

    /**
     * Sets duplication processor.
     *
     * @param duplicationProcessor the duplication processor
     */
    public void setDuplicationProcessor(DuplicationProcessor duplicationProcessor) {
        this.duplicationProcessor = duplicationProcessor;
    }
}
