package com.hnqc.ironhand.spider.distributed;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Site;
import com.hnqc.ironhand.spider.Spider;
import com.hnqc.ironhand.spider.distributed.configurable.ConfigurablePageProcessor;
import com.hnqc.ironhand.spider.distributed.configurable.ExtractRule;
import com.hnqc.ironhand.spider.distributed.downloader.AsyncWithMessageDownloader;
import com.hnqc.ironhand.spider.pipeline.Pipeline;
import com.hnqc.ironhand.spider.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * configurable distributed spider,it's unstable and will abandoned
 *
 * @author zido
 * @date 2018/46/17
 * @deprecated {@link DsSpider} provide better and more comprehensive functionality
 */
public class DsSpiderImpl implements IDsSpider {

    private final static Logger logger = LoggerFactory.getLogger(DsSpiderImpl.class);
    private List<ExtractRule> extractRules;
    private transient Spider spider;

    private Long id;
    private Site site;
    private boolean spawnUrl = true;
    private Date startTime;


    public DsSpiderImpl(List<Pipeline> pipelines, Scheduler scheduler, AsyncWithMessageDownloader downloader) {
        if (pipelines == null) {
            throw new NullPointerException();
        }
        spider = new Spider();
        spider.setPipelines(pipelines);
        spider.setScheduler(scheduler);
        spider.setDownloader(downloader);
    }

    public DsSpiderImpl() {
        this.spider = new Spider();
    }

    public DsSpiderImpl addPipeline(Pipeline pipeline) {
        spider.addPipeline(pipeline);
        return this;
    }

    public DsSpiderImpl setScheduler(Scheduler scheduler) {
        spider.setScheduler(scheduler);
        return this;
    }

    public DsSpiderImpl setDownloader(AsyncWithMessageDownloader downloader) {
        spider.setDownloader(downloader);
        return this;
    }

    public DsSpiderImpl setPageProcessor(Site site, List<ExtractRule> extractRules) {
        this.setSite(site);
        this.extractRules = extractRules;
        return this;
    }

    public DsSpiderImpl setPageProcessor(ConfigurablePageProcessor pageProcessor) {
        this.setSite(pageProcessor.getSite());
        this.extractRules = pageProcessor.getExtractRules();
        return this;
    }

    public void init() {
        spider.setId(id);
        if (getStartTime() == null) {
            Date date = new Date();
            spider.setStartTime(date);
            this.setStartTime(date);
        }
        spider.setPageProcessor(new ConfigurablePageProcessor(site, extractRules));
        spider.setSite(site);
        spider.setSpawnUrl(spawnUrl);
    }

    @Override
    public void run() {
        init();
        logger.info("Spider {} started!", spider.getId());
        Scheduler scheduler = spider.getScheduler();
        Request request = scheduler.poll(spider);
        processRequest(request);
    }

    @Override
    public void run(Request request, Page page) {
        init();
        if (page.isDownloadSuccess()) {
            spider.onDownloadSuccess(request, page);
        } else {
            spider.onDownloaderFail(request);
        }
    }

    private void processRequest(Request request) {
        spider.getDownloader().download(request, spider);
    }

    public void setExtractRules(List<ExtractRule> extractRules) {
        this.extractRules = extractRules;
    }

    public List<ExtractRule> getExtractRules() {
        return extractRules;
    }

    public Long getId() {
        return id;
    }

    public DsSpiderImpl setId(Long id) {
        this.id = id;
        return this;
    }

    public Site getSite() {
        return site;
    }

    public DsSpiderImpl setSite(Site site) {
        this.site = site;
        return this;
    }

    public boolean isSpawnUrl() {
        return spawnUrl;
    }

    public DsSpiderImpl setSpawnUrl(boolean spawnUrl) {
        this.spawnUrl = spawnUrl;
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    public DsSpiderImpl setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }
}
