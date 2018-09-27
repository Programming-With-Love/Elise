package site.zido.elise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.downloader.AutoSwitchDownloader;
import site.zido.elise.downloader.Downloader;
import site.zido.elise.matcher.NumberExpressMatcher;
import site.zido.elise.pipeline.ConsolePipeline;
import site.zido.elise.pipeline.Pipeline;
import site.zido.elise.processor.ExtractorPageProcessor;
import site.zido.elise.processor.PageProcessor;
import site.zido.elise.scheduler.SimpleTaskScheduler;
import site.zido.elise.scheduler.TaskScheduler;
import site.zido.elise.task.DefaultMemoryTaskManager;
import site.zido.elise.task.TaskManager;
import site.zido.elise.utils.Asserts;
import site.zido.elise.utils.UrlUtils;
import site.zido.elise.utils.ValidateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * the main spider
 *
 * @author zido
 */
public class Spider {
    private static Logger logger = LoggerFactory.getLogger(Spider.class);

    public static Spider defaults() {
        return defaults(1);
    }

    public static Spider defaults(int threadNum) {
        Spider spider = new Spider(new SimpleTaskScheduler(threadNum));
        spider.setDownloader(new AutoSwitchDownloader());
        spider.addPipeline(new ConsolePipeline());
        spider.setPageProcessor(new ExtractorPageProcessor());
        spider.setTaskManager(new DefaultMemoryTaskManager());
        return spider;
    }

    private Downloader downloader;
    private List<Pipeline> pipelines = new ArrayList<>();
    private PageProcessor pageProcessor;
    private DefaultSpiderListenProcessor processor = new DefaultSpiderListenProcessor();

    private TaskManager taskManager;

    private TaskScheduler manager;

    private Spider(TaskScheduler manager) {
        this.manager = manager;
    }

    class DefaultSpiderListenProcessor implements TaskScheduler.DownloadListener, TaskScheduler.AnalyzerListener {

        @Override
        public void onDownload(long taskId, Request request) {
            Task task = taskManager.getTask(taskId);
            Site site = task.getSite();
            if (site.getDomain() == null && request != null && request.getUrl() != null) {
                site.setDomain(UrlUtils.getDomain(request.getUrl()));
            }
            Page page = downloader.download(request, task);
            manager.process(taskId, request, page);
        }

        @Override
        public void onProcess(long taskId, Request request, Page page) {
            Task task = taskManager.getTask(taskId);
            if (page.isDownloadSuccess()) {
                Site site = task.getSite();
                String codeAccepter = site.getCodeAccepter();
                NumberExpressMatcher matcher = new NumberExpressMatcher(codeAccepter);
                if (matcher.matches(page.getStatusCode())) {
                    List<ResultItem> resultItems = pageProcessor.process(task, page, manager);
                    if (!ValidateUtils.isEmpty(resultItems)) {
                        for (ResultItem resultItem : resultItems) {
                            if (resultItem != null) {
                                resultItem.setRequest(request);
                                for (Pipeline pipeline : pipelines) {
                                    try {
                                        pipeline.process(resultItem, task);
                                    } catch (Throwable e) {
                                        logger.error("pipeline have made a exception", e);
                                    }
                                }
                            } else {
                                logger.info("page not find anything, page {}", request.getUrl());
                            }
                        }
                    }
                    sleep(site.getSleepTime());
                    return;
                }
            }
            Site site = task.getSite();
            if (site.getCycleRetryTimes() == 0) {
                sleep(site.getSleepTime());
            } else {
                // for cycle retry
                doCycleRetry(task, request);
            }


        }
    }

    protected void preStart() {
        Asserts.notNull(downloader);
        Asserts.notNull(pageProcessor);
        Asserts.notEmpty(pipelines, "pipelines can not be null or empty");
        manager.registerDownloader(processor);
        manager.registerAnalyzer(processor);
    }

    private void doCycleRetry(Task task, Request request) {
        Object cycleTriedTimesObject = request.getExtra(Request.CYCLE_TRIED_TIMES);
        if (cycleTriedTimesObject == null) {
            manager.pushRequest(task.getId(), new Request(request).putExtra(Request.CYCLE_TRIED_TIMES, 1));
        } else {
            int cycleTriedTimes = (Integer) cycleTriedTimesObject;
            cycleTriedTimes++;
            if (cycleTriedTimes < task.getSite().getCycleRetryTimes()) {
                manager.pushRequest(task.getId(), new Request(request).putExtra(Request.CYCLE_TRIED_TIMES, cycleTriedTimes));
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
        Asserts.notEmpty(urls);
        preStart();
        for (String url : urls) {
            manager.pushRequest(taskManager.lastTask().getId(), new Request(url));
        }
        return this;
    }

    public Spider addTask(Task task) {
        Asserts.notNull(task);
        preStart();
        taskManager.addTask(task);
        return this;
    }

    public Spider addUrl(Task task, String... urls) {
        Asserts.notNull(task);
        Asserts.notEmpty(urls);
        preStart();
        taskManager.addTask(task);
        for (String url : urls) {
            manager.pushRequest(task.getId(), new Request(url));
        }
        return this;
    }

    public Spider addPipeline(Pipeline pipeline) {
        Asserts.notNull(pipeline);
        pipelines.add(pipeline);
        return this;
    }

    public List<Pipeline> getPipelines() {
        return pipelines;
    }

    public Spider setDownloader(Downloader downloader) {
        this.downloader = downloader;
        return this;
    }

    public Spider setPageProcessor(PageProcessor pageProcessor) {
        this.pageProcessor = pageProcessor;
        return this;
    }

    public Spider setTaskManager(TaskManager manager) {
        this.taskManager = manager;
        return this;
    }

}
