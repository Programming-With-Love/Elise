package com.hnqc.ironhand.spider.distributed;

import com.hnqc.ironhand.spider.*;
import com.hnqc.ironhand.spider.distributed.configurable.DefRootExtractor;
import com.hnqc.ironhand.spider.distributed.configurable.PageModelExtractor;
import com.hnqc.ironhand.spider.distributed.downloader.AsyncWithMessageDownloader;
import com.hnqc.ironhand.spider.distributed.message.MessageManager;
import com.hnqc.ironhand.spider.distributed.pipeline.MappedPageModelPipeline;
import com.hnqc.ironhand.spider.distributed.pipeline.ModelPipeline;
import com.hnqc.ironhand.spider.distributed.pipeline.PageModelCollectorPipeline;
import com.hnqc.ironhand.spider.distributed.pipeline.PageModelPipeline;
import com.hnqc.ironhand.spider.distributed.processor.MappedModelPageProcessor;
import com.hnqc.ironhand.spider.downloader.Downloader;
import com.hnqc.ironhand.spider.pipeline.CollectorPipeline;
import com.hnqc.ironhand.spider.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * distributed spider launcher,it extends from Spider
 * <p>
 * the instance of {@link Holder} prepare to launch application
 * <p>
 * Forced to use asynchronous download implementation class to provide download function
 *
 * @author zido
 * @date 2018/04/16
 */
public class DsSpider extends Spider implements IDsSpider {

    private final static Logger logger = LoggerFactory.getLogger(DsSpider.class);

    private DefRootExtractor defRootExtractor;

    public DsSpider(Task task, PageModelPipeline pageModelPipeline, Downloader downloader, DefRootExtractor defRootExtractor) {
        this(task.getSite(), pageModelPipeline, downloader, defRootExtractor);
        super.setId(task.getId());
    }

    protected DsSpider(MappedModelPageProcessor modelPageProcessor, Downloader downloader) {
        super(modelPageProcessor);
        this.setDownloader(downloader);
    }

    public DsSpider(Site site, PageModelPipeline pageModelPipeline, Downloader downloader, DefRootExtractor defRootExtractor) {
        this(new MappedModelPageProcessor(site, new PageModelExtractor(defRootExtractor)), downloader);
        ModelPipeline modelPipeline = new ModelPipeline();
        this.defRootExtractor = defRootExtractor;
        super.addPipeline(modelPipeline);
    }

    @Override
    protected CollectorPipeline getCollectorPipeline() {
        return new PageModelCollectorPipeline<>(this.defRootExtractor, new MappedPageModelPipeline());
    }

    @Override
    public void run() {
        Scheduler scheduler = getScheduler();
        Request request = scheduler.poll(this);
        processRequest(request);
    }

    @Override
    public void run(Request request, Page page) {
        if (page.isDownloadSuccess()) {
            onDownloadSuccess(request, page);
        } else {
            onDownloaderFail(request);
        }
    }

    public static SpiderBuilder builder() {
        return new SpiderBuilder();
    }

    /**
     * hold some options,it cannot be directly instantiated
     * <p>
     * If you want to start a brand new spider you must add the url {@link #addUrl(String...)}
     *
     * @author zido
     * @date 2018/41/17
     */
    public static class Holder {
        private PageModelPipeline pageModelPipeline;
        private String[] initialUrl;
        private Scheduler scheduler;
        private MessageManager messageManager;
        private Downloader downloader;

        /**
         * spider holder,this holder will be downloader and analyzer
         *
         * @param pageModelPipeline pipeline {@link PageModelPipeline}
         * @param messageManager    the message manager
         * @param downloader        downloader
         * @param scheduler         scheduler
         */
        public Holder(PageModelPipeline pageModelPipeline,
                      MessageManager messageManager,
                      Downloader downloader,
                      Scheduler scheduler) {
            this.pageModelPipeline = pageModelPipeline;
            this.messageManager = messageManager;
            this.downloader = downloader;
            this.scheduler = scheduler;

        }

        /**
         * spider holder,this holder will be downloader
         *
         * @param downloader downloader
         */
        public Holder(Downloader downloader) {
            this(null, null, downloader, null);
        }

        /**
         * spider holder,this holder will be analyzer
         *
         * @param pageModelPipeline pipeline
         * @param manager           manager
         * @param scheduler         scheduler
         */
        public Holder(PageModelPipeline pageModelPipeline, MessageManager manager, Scheduler scheduler) {
            this(pageModelPipeline, manager, null, scheduler);
        }

        public Holder setDownloader(AsyncWithMessageDownloader downloader) {
            this.downloader = downloader;
            return this;
        }


        /**
         * initial url
         *
         * @param initialUrl initial url
         * @return this
         */
        public Holder addUrl(String... initialUrl) {
            this.initialUrl = initialUrl;
            return this;
        }

        public Holder setScheduler(Scheduler scheduler) {
            this.scheduler = scheduler;
            return this;
        }

        public void run(Task task, DefRootExtractor def) {
            DsSpider spider = new DsSpider(task, pageModelPipeline, downloader, def);
            spider.setScheduler(scheduler);
            spider.addUrl(initialUrl).run();
        }

        public void run(Task task, Request request, Page page, DefRootExtractor def) {
            DsSpider spider = new DsSpider(task, pageModelPipeline, downloader, def);
            spider.setScheduler(scheduler);
            spider.run(request, page);
        }
    }

    @Override
    protected void processRequest(Request request) {
        getDownloader().download(request, this);
    }
}
