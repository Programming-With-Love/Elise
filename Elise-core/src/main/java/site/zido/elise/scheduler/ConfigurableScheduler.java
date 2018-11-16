package site.zido.elise.scheduler;

import site.zido.elise.downloader.Downloader;
import site.zido.elise.processor.PageProcessor;


public abstract class ConfigurableScheduler extends AbstractScheduler {
    private Downloader downloader;
    private PageProcessor processor;
    private CountManager countManager;
    private DuplicationProcessor duplicationProcessor;

    @Override
    public Downloader getDownloader() {
        return downloader;
    }

    @Override
    public PageProcessor getProcessor() {
        return processor;
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

    public void setProcessor(PageProcessor processor) {
        this.processor = processor;
    }

    public void setCountManager(CountManager countManager) {
        this.countManager = countManager;
    }

    public void setDuplicationProcessor(DuplicationProcessor duplicationProcessor) {
        this.duplicationProcessor = duplicationProcessor;
    }
}
