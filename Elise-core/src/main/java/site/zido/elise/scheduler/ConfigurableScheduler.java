package site.zido.elise.scheduler;

import site.zido.elise.downloader.Downloader;
import site.zido.elise.processor.ResponseHandler;


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

    public void setDownloader(Downloader downloader) {
        this.downloader = downloader;
    }

    public void setResponseHandler(ResponseHandler responseHandler) {
        this.ResponseHandler = responseHandler;
    }

    public void setCountManager(CountManager countManager) {
        this.countManager = countManager;
    }

    public void setDuplicationProcessor(DuplicationProcessor duplicationProcessor) {
        this.duplicationProcessor = duplicationProcessor;
    }
}
