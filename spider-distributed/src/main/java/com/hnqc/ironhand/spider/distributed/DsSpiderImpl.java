package com.hnqc.ironhand.spider.distributed;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Site;
import com.hnqc.ironhand.spider.Spider;
import com.hnqc.ironhand.spider.configurable.ConfigurablePageProcessor;
import com.hnqc.ironhand.spider.configurable.ExtractRule;
import com.hnqc.ironhand.spider.pipeline.Pipeline;
import com.hnqc.ironhand.spider.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class DsSpiderImpl implements IDsSpider {

    private final static Logger logger = LoggerFactory.getLogger(DsSpiderImpl.class);
    private List<ExtractRule> extractRules;
    private transient Spider spider;

    private Long ID;
    private Site site;
    private boolean spawnUrl = true;
    private Date startTime;


    public DsSpiderImpl(List<Pipeline> pipelines, Scheduler scheduler, AbsAsyncDownloader downloader) {
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

    public DsSpiderImpl setDownloader(AbsAsyncDownloader downloader) {
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
        spider.setID(ID);
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
        logger.info("Spider {} started!", spider.getID());
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

    public Long getID() {
        return ID;
    }

    public DsSpiderImpl setID(Long ID) {
        this.ID = ID;
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
