package site.zido.elise.client;

import site.zido.elise.E;
import site.zido.elise.Spider;
import site.zido.elise.client.scheduler.MemerySpiderContext;
import site.zido.elise.client.scheduler.MultiThreadTaskScheduler;
import site.zido.elise.custom.Config;
import site.zido.elise.custom.GlobalConfig;
import site.zido.elise.custom.GlobalConfigBuilder;
import site.zido.elise.downloader.DefaultDownloaderFactory;
import site.zido.elise.downloader.DownloaderFactory;
import site.zido.elise.downloader.HttpClientDownloaderFactory;
import site.zido.elise.events.EventListener;
import site.zido.elise.processor.BlankSaver;
import site.zido.elise.processor.DefaultResponseProcessor;
import site.zido.elise.processor.ResponseProcessor;
import site.zido.elise.processor.Saver;
import site.zido.elise.scheduler.*;
import site.zido.elise.select.*;

import java.util.HashSet;
import java.util.Set;

/**
 * the default Spider builder.
 *
 * @author zido
 */
public class SpiderBuilder {
    private Set<EventListener> listeners = new HashSet<>();
    private ResponseProcessor responseProcessor;
    private Saver saver;
    private CountManager countManager;
    private DuplicationProcessor duplicationProcessor;
    private DownloaderFactory downloaderFactory;
    private int threadNum;
    private Config globalConfig;
    private SpiderContext spiderContext;

    /**
     * Instantiates a new Spider builder.
     */
    protected SpiderBuilder() {
        super();
    }

    /**
     * Create spider builder.
     *
     * @return the spider builder
     */
    public static SpiderBuilder create() {
        return new SpiderBuilder();
    }

    /**
     * Defaults spider.
     *
     * @return the spider
     */
    public static Spider defaults() {
        return create().build();
    }

    /**
     * Add event listener.
     *
     * @param listener the listener
     * @return the spider builder
     */
    public SpiderBuilder addEventListener(EventListener listener) {
        listeners.add(listener);
        return this;
    }

    /**
     * Sets response processor.
     *
     * @param responseProcessor the response processor
     * @return the response processor
     */
    public SpiderBuilder setResponseProcessor(ResponseProcessor responseProcessor) {
        this.responseProcessor = responseProcessor;
        return this;
    }

    /**
     * Sets saver.
     *
     * @param saver the saver
     * @return the saver
     */
    public SpiderBuilder setSaver(Saver saver) {
        this.saver = saver;
        return this;
    }

    /**
     * Sets count manager.
     *
     * @param countManager the count manager
     * @return the count manager
     */
    public SpiderBuilder setCountManager(CountManager countManager) {
        this.countManager = countManager;
        return this;
    }

    /**
     * Sets duplication processor.
     *
     * @param duplicationProcessor the duplication processor
     * @return the duplication processor
     */
    public SpiderBuilder setDuplicationProcessor(DuplicationProcessor duplicationProcessor) {
        this.duplicationProcessor = duplicationProcessor;
        return this;
    }

    /**
     * Sets downloader factory.
     *
     * @param downloaderFactory the downloader factory
     * @return the downloader factory
     */
    public SpiderBuilder setDownloaderFactory(DownloaderFactory downloaderFactory) {
        this.downloaderFactory = downloaderFactory;
        return this;
    }

    /**
     * Sets thread num.
     *
     * @param threadNum the thread num
     * @return the thread num
     */
    public SpiderBuilder setThreadNum(int threadNum) {
        this.threadNum = threadNum;
        return this;
    }

    /**
     * Config spider builder.
     *
     * @param globalConfig the global config
     * @return the spider builder
     */
    public SpiderBuilder config(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        return this;
    }

    public SpiderBuilder setContext(SpiderContext spiderContext){
        this.spiderContext = spiderContext;
        return this;
    }

    /**
     * Build spider.
     *
     * @return the spider
     */
    public Spider build() {
        if (saver == null) {
            saver = new BlankSaver();
        }
        if(spiderContext == null){
            spiderContext = new MemerySpiderContext();
        }
        if (responseProcessor == null) {
            responseProcessor = new DefaultResponseProcessor(saver);
            DefaultResponseProcessor drp = (DefaultResponseProcessor) responseProcessor;
            drp.registerSelector(E.Action.XPATH_SELECTOR,new XpathSelectHandler());
            drp.registerSelector(E.Action.MATCH_LINK,new RegexSelectHandler());
            drp.registerSelector(E.Action.CSS_SELECTOR,new CssSelectHandler());
            drp.registerSelector(E.Action.LINK_SELECTOR,new LinkSelectHandler("a:href"));
            drp.registerSelector(E.Action.MATCH_NUMBER,new NumberMatcherSelectHandler());
            drp.registerSelector(E.Action.SELECT_ORIGIN,new OriginSelectorHandler());
            drp.registerSelector(E.Action.SELECT_URL,new OriginSelectorHandler());
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
        MultiThreadTaskScheduler scheduler;
        if (threadNum > 0) {
            scheduler = new MultiThreadTaskScheduler(threadNum);
        } else {
            scheduler = new MultiThreadTaskScheduler();
        }
        if (globalConfig == null) {
            globalConfig = GlobalConfigBuilder.defaults();
        }
        scheduler.setConfig(globalConfig);
        scheduler.setResponseProcessor(responseProcessor);
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
}
