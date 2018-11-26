package site.zido.elise.scheduler;

import site.zido.elise.downloader.Downloader;
import site.zido.elise.processor.ResponseHandler;


/**
 * The type Configurable scheduler.
 *
 * @author zido
 */
public abstract class BaseConfigurableScheduler extends AbstractScheduler {
    private Downloader downloader;
    private ResponseHandler responsehandler;
    private CountManager countManager;
    private DuplicationProcessor duplicationProcessor;

    @Override
    public Downloader getDownloader() {
        return downloader;
    }

    /**
     * Sets downloader.
     *
     * @param downloader the downloader
     */
    public void setDownloader(Downloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public ResponseHandler getResponsehandler() {
        return responsehandler;
    }

    /**
     * Sets response handler.
     *
     * @param responsehandler the response handler
     */
    public void setResponsehandler(ResponseHandler responsehandler) {
        this.responsehandler = responsehandler;
    }

    @Override
    public CountManager getCountManager() {
        return countManager;
    }

    /**
     * Sets count manager.
     *
     * @param countManager the count manager
     */
    public void setCountManager(CountManager countManager) {
        this.countManager = countManager;
    }

    @Override
    public DuplicationProcessor getDuplicationProcessor() {
        return duplicationProcessor;
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
