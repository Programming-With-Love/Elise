package com.hnqc.ironhand.spider.distributed;

import com.hnqc.ironhand.spider.distributed.message.MessageManager;
import com.hnqc.ironhand.spider.distributed.message.ThreadMessageManager;
import com.hnqc.ironhand.spider.distributed.pipeline.MappedPageModelPipeline;
import com.hnqc.ironhand.spider.distributed.pipeline.PageModelPipeline;
import com.hnqc.ironhand.spider.downloader.Downloader;
import com.hnqc.ironhand.spider.scheduler.QueueScheduler;
import com.hnqc.ironhand.spider.scheduler.Scheduler;

/**
 * build spider.
 * <p>
 * It is not a simple builder model,
 * but a crawler instance management container,
 * which is asynchronous and can easily construct a variety of client-side classes.
 *
 * @author zido
 * @date 2018/04/17
 */
public class SpiderBuilder {
    private PageModelPipeline pageModelPipeline = new MappedPageModelPipeline();
    private Downloader downloader;
    private Scheduler scheduler = new QueueScheduler();
    private MessageManager messageManager;

    public SpiderBuilder() {

    }

    public SpiderBuilder pipeline(PageModelPipeline pipeline) {
        this.pageModelPipeline = pipeline;
        return this;
    }

    public SpiderBuilder downloader(Downloader downloader) {
        this.downloader = downloader;
        return this;
    }

    public SpiderBuilder scheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    public SpiderBuilder messageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
        return this;
    }

    public DsSpider.Holder build() {
        if (messageManager == null) {
            messageManager = new ThreadMessageManager();
        }
        return new DsSpider.Holder(pageModelPipeline, messageManager, downloader, scheduler);
    }
}
