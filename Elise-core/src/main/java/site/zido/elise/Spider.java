package site.zido.elise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.downloader.AutoSwitchDownloader;
import site.zido.elise.downloader.Downloader;
import site.zido.elise.select.NumberExpressMatcher;
import site.zido.elise.processor.ExtractorPageProcessor;
import site.zido.elise.processor.PageProcessor;
import site.zido.elise.saver.MemorySaver;
import site.zido.elise.saver.Saver;
import site.zido.elise.scheduler.SimpleTaskScheduler;
import site.zido.elise.scheduler.TaskScheduler;
import site.zido.elise.task.DefaultMemoryTaskManager;
import site.zido.elise.task.TaskManager;
import site.zido.elise.utils.Asserts;
import site.zido.elise.utils.UrlUtils;
import site.zido.elise.utils.ValidateUtils;

import java.util.List;
import java.util.concurrent.Future;

/**
 * the main spider
 *
 * @author zido
 */
public class Spider {
    private static Logger logger = LoggerFactory.getLogger(Spider.class);
    private Downloader downloader;
    private Saver saver;
    private PageProcessor pageProcessor;
    private DefaultSpiderListenProcessor processor = new DefaultSpiderListenProcessor();
    private TaskManager taskManager;
    private TaskScheduler manager;
    private RequestPutter putter = new Putter();

    private Spider(TaskScheduler manager) {
        this.manager = manager;
    }

    public static Spider defaults() {
        return defaults(1);
    }

    public static Spider defaults(int threadNum) {
        Spider spider = new Spider(new SimpleTaskScheduler(threadNum));
        spider.setDownloader(new AutoSwitchDownloader());
        spider.setSaver(new MemorySaver());
        spider.setPageProcessor(new ExtractorPageProcessor());
        spider.setTaskManager(new DefaultMemoryTaskManager());
        return spider;
    }

    protected void preStart() {
        Asserts.notNull(downloader);
        Asserts.notNull(pageProcessor);
        Asserts.notNull(saver, "saver can not be null");
        manager.registerDownloader(processor);
        manager.registerAnalyzer(processor);
    }

    private void doCycleRetry(Task task, Request request) {
        Object cycleTriedTimesObject = request.getExtra(Request.CYCLE_TRIED_TIMES);
        if (cycleTriedTimesObject == null) {
            manager.pushRequest(task, new Request(request).putExtra(Request.CYCLE_TRIED_TIMES, 1));
        } else {
            int cycleTriedTimes = (Integer) cycleTriedTimesObject;
            cycleTriedTimes++;
            if (cycleTriedTimes < task.getSite().getCycleRetryTimes()) {
                manager.pushRequest(task, new Request(request).putExtra(Request.CYCLE_TRIED_TIMES, cycleTriedTimes));
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
     * Add urls to select. <br>
     *
     * @param url url
     * @return this
     */
    public Future<CrawlResult> addUrl(String url) {
        Asserts.hasLength(url);
        preStart();
        Request request = new Request(url);
        return manager.pushRequest(taskManager.lastTask(), request);
    }

    public Spider addTask(Task task) {
        Asserts.notNull(task);
        preStart();
        taskManager.addTask(task);
        return this;
    }

    public Future<CrawlResult> addUrl(Task task, String url) {
        Asserts.notNull(task);
        Asserts.hasLength(url);
        preStart();
        taskManager.addTask(task);
        Request request = new Request(url);
        return manager.pushRequest(task, request);
    }

    public Spider setSaver(Saver saver) {
        Asserts.notNull(saver);
        this.saver = saver;
        return this;
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

    private class Putter implements RequestPutter {

        @Override
        public Future<CrawlResult> pushRequest(Task task, Request request) {
            return manager.pushRequest(task, request);
        }
    }

    class DefaultSpiderListenProcessor implements TaskScheduler.DownloadListener, TaskScheduler.AnalyzerListener {

        @Override
        public CrawlResult onDownload(Task task, Request request) {
            Site site = task.getSite();
            if (site.getDomain() == null && request.getUrl() != null) {
                site.setDomain(UrlUtils.getDomain(request.getUrl()));
            }
            Page page = downloader.download(request, task);
            return manager.process(task, request, page);
        }

        @Override
        public CrawlResult onProcess(Task task, Request request, Page page) {
            if (page.isDownloadSuccess()) {
                Site site = task.getSite();
                String codeAccepter = site.getCodeAccepter();
                NumberExpressMatcher matcher = new NumberExpressMatcher(codeAccepter);
                if (matcher.matches(page.getStatusCode())) {
                    List<ResultItem> resultItems = pageProcessor.process(task, page, putter);
                    if (!ValidateUtils.isEmpty(resultItems)) {
                        for (ResultItem resultItem : resultItems) {
                            if (resultItem != null) {
                                resultItem.setRequest(request);
                                try {
                                    saver.save(resultItem, task);
                                } catch (Throwable e) {
                                    logger.error("saver have made a exception", e);
                                }
                            } else {
                                logger.info("page not find anything, page {}", request.getUrl());
                            }
                        }
                    }
                    sleep(site.getSleepTime());
                    return new CrawlResult(task, saver);
                }
            }
            Site site = task.getSite();
            if (site.getCycleRetryTimes() == 0) {
                sleep(site.getSleepTime());
            } else {
                // for cycle retry
                doCycleRetry(task, request);
            }
            return CrawlResult.blank();
        }
    }

}
