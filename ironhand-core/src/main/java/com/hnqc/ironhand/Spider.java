package com.hnqc.ironhand;

import com.hnqc.ironhand.downloader.Downloader;
import com.hnqc.ironhand.message.TaskScheduler;
import com.hnqc.ironhand.pipeline.ConsolePipeline;
import com.hnqc.ironhand.pipeline.Pipeline;
import com.hnqc.ironhand.processor.PageProcessor;
import com.hnqc.ironhand.thread.CountableThreadPool;
import com.hnqc.ironhand.utils.UrlUtils;
import com.hnqc.ironhand.utils.ValidateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * com.hnqc.ironhand.spider
 *
 * @author zido
 */
public class Spider implements TaskScheduler.DownloadListener,
        TaskScheduler.AnalyzerListener,
        RequestPutter,
        Runnable {
    private Downloader downloader;
    private List<Pipeline> pipelines = new ArrayList<>();
    private PageProcessor pageProcessor;
    private static Logger logger = LoggerFactory.getLogger(Spider.class);
    private CountableThreadPool threadPool;
    private ExecutorService executorService;

    private int threadNum = 1;

    private List<SpiderListener> spiderListeners;

    private TaskScheduler manager;

    @Override
    public void pushRequest(Task task, Request request) {
        try {
            manager.download(task, request);
        } catch (NullPointerException e) {
            Site site = task.getSite();
            if (site.getCycleRetryTimes() == 0) {
                logger.error("no downloader in container,will sleep {} seconds,please add downloader", site.getSleepTime());
                sleep(site.getSleepTime());
            } else {
                logger.error("no downloader in container,will retry {} times,please add downloader", site.getRetrySleepTime());
                // for cycle retry
                doCycleRetry(task, request);
            }
        }
    }

    private void pushRequest(Task task, List<Request> request) {
        for (Request r : request) {
            pushRequest(task, r);
        }
    }

    /**
     * create a spider with pageProcessor.
     *
     * @param pageProcessor pageProcessor
     */
    public Spider(TaskScheduler manager,
                  PageProcessor pageProcessor,
                  Downloader downloader,
                  Pipeline... pipeline) {
        this.pageProcessor = pageProcessor;
        this.downloader = downloader;
        if (pipeline != null) {
            this.pipelines.addAll(Arrays.asList(pipeline));
        }
        this.manager = manager;
    }

    public Spider(TaskScheduler manager) {
        this.manager = manager;
    }

    public Spider setExecuterService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    @Override
    public void onProcess(Task task, Request request, Page page) {
        if (page.isDownloadSuccess()) {
            Site site = task.getSite();
            ResultItem resultItem = pageProcessor.process(task, page, this);
            if (resultItem != null) {
                if (site.getAcceptStatCode().contains(page.getStatusCode())) {
                    if (!resultItem.isSkip()) {
                        resultItem.setRequest(request);
                        for (Pipeline pipeline : pipelines) {
                            pipeline.process(resultItem, task);
                        }
                    }
                } else {
                    logger.info("page status code error, page {} , code: {}", request.getUrl(), page.getStatusCode());
                }

            } else {
                logger.info("page not find anything, page {}", request.getUrl());
            }

            sleep(site.getSleepTime());
            onSuccess(request);
        } else {
            Site site = task.getSite();
            if (site.getCycleRetryTimes() == 0) {
                sleep(site.getSleepTime());
            } else {
                // for cycle retry
                doCycleRetry(task, request);
            }
        }
    }

    @Override
    public void onDownload(Task task, Request request) {
        Site site = task.getSite();
        if (site.getDomain() == null && request != null && request.getUrl() != null) {
            site.setDomain(UrlUtils.getDomain(request.getUrl()));
        }
        Page page = downloader.download(request, task);
        manager.process(task, request, page);

    }

    @Override
    public void run() {
        if (downloader != null) {
            manager.registerDownloader(this);
            downloader.setThread(threadNum);
        }
        if (pageProcessor != null) {
            manager.registerAnalyzer(this);
        }
        if (pipelines.isEmpty()) {
            pipelines.add(new ConsolePipeline());
        }
        manager.listen();
    }

    public Spider runAsync() {
        if (threadPool == null || threadPool.isShutdown()) {
            if (executorService != null && !executorService.isShutdown()) {
                threadPool = new CountableThreadPool(threadNum, executorService);
            } else {
                threadPool = new CountableThreadPool(threadNum);
            }
        }
        threadPool.execute(this);
        return this;
    }

    public Spider start() {
        return runAsync();
    }

    public void stop() {
        manager.removeAnalyzer(this);
        manager.removeDownloader(this);
    }

    private void onError(Request request) {
        if (!ValidateUtils.isEmpty(spiderListeners)) {
            for (SpiderListener spiderListener : spiderListeners) {
                spiderListener.onError(request);
            }
        }
    }

    private void onSuccess(Request request) {
        if (!ValidateUtils.isEmpty(spiderListeners)) {
            for (SpiderListener spiderListener : spiderListeners) {
                spiderListener.onSuccess(request);
            }
        }
    }

    private void doCycleRetry(Task task, Request request) {
        Object cycleTriedTimesObject = request.getExtra(Request.CYCLE_TRIED_TIMES);
        if (cycleTriedTimesObject == null) {
            pushRequest(task, new Request(request).putExtra(Request.CYCLE_TRIED_TIMES, 1));
        } else {
            int cycleTriedTimes = (Integer) cycleTriedTimesObject;
            cycleTriedTimes++;
            if (cycleTriedTimes < task.getSite().getCycleRetryTimes()) {
                pushRequest(task, new Request(request).putExtra(Request.CYCLE_TRIED_TIMES, cycleTriedTimes));
            }
        }
        sleep(task.getSite().getRetrySleepTime());
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            logger.error("Thread interrupted when sleep", e);
        }
    }

    /**
     * Add urls to crawl. <br>
     *
     * @param urls urls
     * @return this
     */
    public Spider addUrl(String... urls) {
        for (String url : urls) {
            pushRequest(new Site().toTask(), new Request(url));
        }
        return this;
    }

    public Spider addUrl(Task task, String... urls) {
        for (String url : urls) {
            pushRequest(task, new Request(url));
        }
        return this;
    }

    /**
     * add a pipeline for Spider
     *
     * @param pipeline pipeline
     * @return this
     * @see Pipeline
     * @since 0.2.1
     */
    public Spider addPipeline(Pipeline pipeline) {
        this.pipelines.add(pipeline);
        return this;
    }

    /**
     * set pipelines for Spider
     *
     * @param pipelines pipelines
     * @return this
     * @see Pipeline
     * @since 0.4.1
     */
    public Spider setPipelines(List<Pipeline> pipelines) {
        this.pipelines = pipelines;
        return this;
    }

    /**
     * clear the pipelines set
     *
     * @return this
     */
    public Spider clearPipeline() {
        pipelines = new ArrayList<>();
        return this;
    }

    /**
     * set the downloader of spider
     *
     * @param downloader downloader
     * @return this
     * @see Downloader
     */
    public Spider setDownloader(Downloader downloader) {
        this.downloader = downloader;
        return this;
    }

    public Spider setSpiderListeners(List<SpiderListener> spiderListeners) {
        this.spiderListeners = spiderListeners;
        return this;
    }

    public Spider setPageProcessor(PageProcessor pageProcessor) {
        this.pageProcessor = pageProcessor;
        return this;
    }

}
