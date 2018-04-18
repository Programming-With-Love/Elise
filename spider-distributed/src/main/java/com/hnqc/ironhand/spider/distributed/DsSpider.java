package com.hnqc.ironhand.spider.distributed;

import com.hnqc.ironhand.spider.*;
import com.hnqc.ironhand.spider.distributed.configurable.DefRootExtractor;
import com.hnqc.ironhand.spider.distributed.configurable.ConfigurableModelExtractor;
import com.hnqc.ironhand.spider.distributed.message.CommunicationManager;
import com.hnqc.ironhand.spider.distributed.message.ThreadCommunicationManager;
import com.hnqc.ironhand.spider.distributed.pipeline.MappedPageModelPipeline;
import com.hnqc.ironhand.spider.distributed.pipeline.ModelPipeline;
import com.hnqc.ironhand.spider.distributed.pipeline.PageModelCollectorPipeline;
import com.hnqc.ironhand.spider.distributed.pipeline.PageModelPipeline;
import com.hnqc.ironhand.spider.processor.ExtractorPageProcessor;
import com.hnqc.ironhand.spider.downloader.Downloader;
import com.hnqc.ironhand.spider.pipeline.CollectorPipeline;
import com.hnqc.ironhand.spider.scheduler.Scheduler;

import java.util.List;

/**
 * distributed spider launcher,it extends from Spider{@link Spider}
 * <p>
 * Provides distributed crawler services, supports multi-threaded download
 * {@link com.hnqc.ironhand.spider.distributed.downloader.AbstractAsyncDownloader} {@link com.hnqc.ironhand.spider.downloader.HttpClientDownloader}
 * , multi-thread analysis, distributed deployment, multi-end + multi-threaded operation.
 * <p>
 * it has a powerful configurability and powerful crawlable programming capabilities{@link ConfigurableModelExtractor},
 * provides a message communication{@link CommunicationManager}
 * <p>
 * {@link com.hnqc.ironhand.spider.distributed.message.LoadBalancer}
 * <p>
 * mechanism, can quickly expand the distribution / Multi-threaded message communication like{@link ThreadCommunicationManager},
 * task scheduling{@link Scheduler},
 * result content saving{@link com.hnqc.ironhand.spider.pipeline.Pipeline},
 * grabbing content configuration{@link DefRootExtractor}
 *
 * @author zido
 * @date 2018/04/16
 */
public class DsSpider extends Spider implements PageExtractorTask {

    private List<ConfigurableModelExtractor> extractors;

    /**
     * construct analyzer client
     *
     * @param task              task
     * @param pageModelPipeline page model pipeline
     * @param downloader        downloader
     */
    public DsSpider(PageExtractorTask task, PageModelPipeline pageModelPipeline, Downloader downloader) {
        this(pageModelPipeline, downloader, task.getModelExtractors());
        super.setId(task.getId());
    }

    protected DsSpider(Downloader downloader) {
        this.setDownloader(downloader);
    }

    public DsSpider(PageModelPipeline pageModelPipeline, Downloader downloader, List<ConfigurableModelExtractor> extractors) {
        super(new ExtractorPageProcessor(extractors));
        setDownloader(downloader);
        ModelPipeline modelPipeline = new ModelPipeline();
        for (ConfigurableModelExtractor def : extractors) {
            if (pageModelPipeline != null) {
                modelPipeline.putPageModelPipeline(def.getModelExtractor().getName(), pageModelPipeline);
            }
        }
        addPipeline(modelPipeline);
        this.extractors = extractors;
    }

    @Override
    protected CollectorPipeline getCollectorPipeline() {
        return new PageModelCollectorPipeline<>(extractors.get(0), new MappedPageModelPipeline());
    }

    @Override
    public List<ConfigurableModelExtractor> getModelExtractors() {
        return extractors;
    }

    @Override
    protected void processRequest(Request request) {
        getDownloader().download(request, new DistributedTask(this));
    }
}
