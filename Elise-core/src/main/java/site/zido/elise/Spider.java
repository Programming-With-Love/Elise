package site.zido.elise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.downloader.AutoSwitchDownloader;
import site.zido.elise.downloader.Downloader;
import site.zido.elise.processor.DefaultPageProcessor;
import site.zido.elise.processor.MemorySaver;
import site.zido.elise.processor.PageProcessor;
import site.zido.elise.processor.Saver;
import site.zido.elise.scheduler.DefaultTaskScheduler;
import site.zido.elise.scheduler.TaskScheduler;
import site.zido.elise.select.CompilerException;
import site.zido.elise.select.NumberExpressMatcher;
import site.zido.elise.task.DefaultMemoryTaskManager;
import site.zido.elise.task.TaskManager;
import site.zido.elise.utils.Asserts;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * the main spider
 *
 * @author zido
 */
public class Spider {
    private static Logger LOGGER = LoggerFactory.getLogger(Spider.class);
    private final static int STATE_STOP = 0;
    private final static int STATE_PRE_STOP = -1;
    private final static int STATE_PRE_START = 1;
    private final static int STATE_STARTED = 2;
    private Downloader downloader;
    private PageProcessor pageProcessor;
    private DefaultSpiderListenProcessor processor = new DefaultSpiderListenProcessor();
    private TaskManager taskManager;
    private TaskScheduler scheduler;
    private AtomicInteger RUN_STATE = new AtomicInteger(STATE_STOP);

    private Spider(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public static Spider defaults() {
        return defaults(1);
    }

    public static Spider defaults(int threadNum) {
        Spider spider = new Spider(new DefaultTaskScheduler(threadNum));
        spider.setDownloader(new AutoSwitchDownloader());
        spider.setPageProcessor(new DefaultPageProcessor(new MemorySaver()));
        spider.setTaskManager(new DefaultMemoryTaskManager());
        return spider;
    }

    protected Spider preStart() {
        Asserts.notNull(downloader);
        Asserts.notNull(pageProcessor);
        scheduler.setAnalyzer(processor);
        return this;
    }

    private void doCycleRetry(Task task, Request request) {
        Object cycleTriedTimesObject = request.getExtra(Request.CYCLE_TRIED_TIMES);
        if (cycleTriedTimesObject == null) {
            scheduler.pushRequest(task, new Request(request).putExtra(Request.CYCLE_TRIED_TIMES, 1));
        } else {
            int cycleTriedTimes = (Integer) cycleTriedTimesObject;
            cycleTriedTimes++;
            if (cycleTriedTimes < task.getSite().getCycleRetryTimes()) {
                scheduler.pushRequest(task, new Request(request).putExtra(Request.CYCLE_TRIED_TIMES, cycleTriedTimes));
            }
        }
        sleep(task.getSite().getRetrySleepTime());
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            LOGGER.error("Thread interrupted when sleep", e);
        }
    }

    /**
     * Add urls to select. <br>
     *
     * @param url url
     * @return this
     */
    public Spider addUrl(String url) {
        Asserts.hasLength(url);
        preStart();
        Request request = new Request(url);
        scheduler.pushRequest(taskManager.lastTask(), request);
        return this;
    }

    public Spider addTask(Task task) {
        Asserts.notNull(task);
        preStart();
        taskManager.addTask(task);
        return this;
    }

    public Spider stop() {
        return this;
    }

    public Spider addUrl(Task task, String url) {
        Asserts.notNull(task);
        Asserts.hasLength(url);
        preStart();
        taskManager.addTask(task);
        Request request = new Request(url);
        scheduler.pushRequest(task, request);
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

    class DefaultSpiderListenProcessor implements TaskScheduler.AnalyzerListener {

        @Override
        public void onProcess(Task task, Request request, Page page) {
            if (page.isDownloadSuccess()) {
                Site site = task.getSite();
                String codeAccepter = site.getCodeAccepter();
                NumberExpressMatcher matcher;
                try {
                    matcher = new NumberExpressMatcher(codeAccepter);
                } catch (CompilerException e) {
                    throw new RuntimeException(e);
                }
                if (matcher.matches(page.getStatusCode())) {
                    List<String> links = pageProcessor.process(task, page);
                    for (String link : links) {
                        scheduler.pushRequest(task, new Request(link));
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

}
