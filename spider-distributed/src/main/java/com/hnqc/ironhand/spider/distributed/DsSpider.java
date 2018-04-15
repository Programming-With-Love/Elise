package com.hnqc.ironhand.spider.distributed;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Site;
import com.hnqc.ironhand.spider.Spider;
import com.hnqc.ironhand.spider.distributed.configurable.DefRootExtractor;
import com.hnqc.ironhand.spider.distributed.configurable.PageModelExtractor;
import com.hnqc.ironhand.spider.distributed.pipeline.MappedPageModelPipeline;
import com.hnqc.ironhand.spider.distributed.pipeline.ModelPipeline;
import com.hnqc.ironhand.spider.distributed.pipeline.PageModelCollectorPipeline;
import com.hnqc.ironhand.spider.distributed.pipeline.PageModelPipeline;
import com.hnqc.ironhand.spider.distributed.processor.MappedModelPageProcessor;
import com.hnqc.ironhand.spider.pipeline.CollectorPipeline;
import com.hnqc.ironhand.spider.processor.PageProcessor;
import com.hnqc.ironhand.spider.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DsSpider
 *
 * @author zido
 * @date 2018/04/16
 */
public class DsSpider extends Spider implements IDsSpider {

    private final static Logger logger = LoggerFactory.getLogger(DsSpider.class);

    private MappedModelPageProcessor modelPageProcessor;

    private ModelPipeline modelPipeline;

    private PageModelPipeline pageModelPipeline;

    private List<DefRootExtractor> defRootExtractors = new ArrayList<>();

    protected DsSpider(MappedModelPageProcessor modelPageProcessor) {
        super(modelPageProcessor);
        this.modelPageProcessor = modelPageProcessor;
    }

    public DsSpider(PageProcessor pageProcessor) {
        super(pageProcessor);
    }

    public DsSpider(Site site, PageModelPipeline pageModelPipeline, DefRootExtractor... defRootExtractor) {
        this(new MappedModelPageProcessor(site,
                Arrays.stream(defRootExtractor).map(PageModelExtractor::new).collect(Collectors.toList())
        ));
        this.modelPipeline = new ModelPipeline();
        for (DefRootExtractor def : defRootExtractor) {
            if (pageModelPipeline != null) {
                this.modelPipeline.putPageModelPipeline(def.getName(), pageModelPipeline);
            }
            defRootExtractors.add(def);
        }
    }

    @Override
    protected CollectorPipeline getCollectorPipeline() {
        return new PageModelCollectorPipeline<>(defRootExtractors.get(0), new MappedPageModelPipeline());
    }

    @Override
    public void run() {
        logger.info("Spider {} started!", getId());
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

    @Override
    protected void processRequest(Request request) {
        getDownloader().download(request, this);
    }
}
