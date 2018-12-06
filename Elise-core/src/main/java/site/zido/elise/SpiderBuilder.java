package site.zido.elise;

import site.zido.elise.custom.Config;
import site.zido.elise.custom.GlobalConfig;
import site.zido.elise.custom.GlobalConfigBuilder;
import site.zido.elise.downloader.DefaultDownloaderFactory;
import site.zido.elise.downloader.DownloaderFactory;
import site.zido.elise.downloader.HttpClientDownloaderFactory;
import site.zido.elise.events.EventListener;
import site.zido.elise.processor.DefaultResponseHandler;
import site.zido.elise.processor.MemorySaver;
import site.zido.elise.processor.ResponseHandler;
import site.zido.elise.processor.Saver;
import site.zido.elise.scheduler.*;

import java.util.HashSet;
import java.util.Set;

public class SpiderBuilder {
    private Set<EventListener> listeners = new HashSet<>();
    private ResponseHandler responseHandler;
    private Saver saver;
    private CountManager countManager;
    private DuplicationProcessor duplicationProcessor;
    private DownloaderFactory downloaderFactory;
    private int threadNum;
    private Config globalConfig;

    protected SpiderBuilder() {
        super();
    }

    public static SpiderBuilder create() {
        return new SpiderBuilder();
    }

    public SpiderBuilder addEventListener(EventListener listener) {
        listeners.add(listener);
        return this;
    }

    public SpiderBuilder setResponseHandler(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
        return this;
    }

    public SpiderBuilder setSaver(Saver saver) {
        this.saver = saver;
        return this;
    }

    public SpiderBuilder setCountManager(CountManager countManager) {
        this.countManager = countManager;
        return this;
    }

    public SpiderBuilder setDuplicationProcessor(DuplicationProcessor duplicationProcessor) {
        this.duplicationProcessor = duplicationProcessor;
        return this;
    }

    public SpiderBuilder setDownloaderFactory(DownloaderFactory downloaderFactory) {
        this.downloaderFactory = downloaderFactory;
        return this;
    }

    public SpiderBuilder setThreadNum(int threadNum) {
        this.threadNum = threadNum;
        return this;
    }

    public SpiderBuilder config(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        return this;
    }

    public Spider build() {
        if (saver == null) {
            saver = new MemorySaver();
        }
        if (responseHandler == null) {
            responseHandler = new DefaultResponseHandler(saver);
        }
        if (countManager == null) {
            countManager = new DefaultMemoryCountManager();
        }
        if (duplicationProcessor == null) {
            duplicationProcessor = new HashSetDeduplicationProcessor();
        }
        if (downloaderFactory == null) {
            downloaderFactory = new DefaultDownloaderFactory();
        }
        if (downloaderFactory instanceof DefaultDownloaderFactory) {
            final Set<String> sets = ((DefaultDownloaderFactory) downloaderFactory).keySet();
            if (sets.isEmpty()) {
                ((DefaultDownloaderFactory) downloaderFactory).registerFactory("httpclient", new HttpClientDownloaderFactory());
            }
        }
        DefaultTaskScheduler scheduler;
        if (threadNum > 0) {
            scheduler = new DefaultTaskScheduler(threadNum);
        } else {
            scheduler = new DefaultTaskScheduler();
        }
        if (globalConfig == null) {
            globalConfig = GlobalConfigBuilder.defaults();
        }
        scheduler.setConfig(globalConfig);
        scheduler.setResponseHandler(responseHandler);
        scheduler.setCountManager(countManager);
        scheduler.setDuplicationProcessor(duplicationProcessor);
        scheduler.setDownloaderFactory(downloaderFactory);
        if (!this.listeners.isEmpty()) {
            for (EventListener listener : listeners) {
                scheduler.addEventListener(listener);
            }
        }
        return scheduler;
    }

    public static Spider defaults() {
        return create().build();
    }
}
