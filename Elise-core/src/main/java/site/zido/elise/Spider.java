package site.zido.elise;

import site.zido.elise.downloader.DefaultDownloaderFactory;
import site.zido.elise.http.Request;
import site.zido.elise.http.impl.DefaultRequest;
import site.zido.elise.processor.DefaultResponseHandler;
import site.zido.elise.processor.MemorySaver;
import site.zido.elise.scheduler.DefaultMemoryCountManager;
import site.zido.elise.scheduler.DefaultTaskScheduler;
import site.zido.elise.scheduler.HashSetDeduplicationProcessor;
import site.zido.elise.scheduler.TaskScheduler;
import site.zido.elise.task.DefaultMemoryTaskManager;
import site.zido.elise.task.TaskManager;
import site.zido.elise.utils.Asserts;

/**
 * the main spider
 *
 * @author zido
 */
public class Spider {
    private TaskManager taskManager;
    private TaskScheduler scheduler;

    private Spider() {
    }

    /**
     * Defaults spider.
     *
     * @return the spider
     */
    public static Spider defaults() {
        return defaults(Runtime.getRuntime().availableProcessors() * 2);
    }

    /**
     * Defaults spider.
     *
     * @param threadNum the thread num
     * @return the spider
     */
    public static Spider defaults(int threadNum) {
        Spider spider = new Spider();
        final DefaultTaskScheduler scheduler = new DefaultTaskScheduler(threadNum);
        spider.scheduler = scheduler;
        scheduler.setDownloaderFactory(new DefaultDownloaderFactory());
        scheduler.setResponseHandler(new DefaultResponseHandler(new MemorySaver()));
        scheduler.setDuplicationProcessor(new HashSetDeduplicationProcessor());
        scheduler.setCountManager(new DefaultMemoryCountManager());
        spider.taskManager = new DefaultMemoryTaskManager();
        return spider;
    }

    /**
     * Add urls to select. <br>
     *
     * @param url url
     * @return this spider
     */
    public Spider addUrl(String url) {
        Asserts.hasLength(url);
        Request request = new DefaultRequest(url);
        scheduler.pushRequest(taskManager.lastTask(), request);
        return this;
    }

    /**
     * Add task spider.
     *
     * @param task the task
     * @return the spider
     */
    public Spider addTask(Task task) {
        Asserts.notNull(task);
        taskManager.addTask(task);
        return this;
    }

    /**
     * Add url spider.
     *
     * @param task the task
     * @param url  the url
     * @return the spider
     */
    public Spider addUrl(Task task, String url) {
        Asserts.notNull(task);
        Asserts.hasLength(url);
        taskManager.addTask(task);
        Request request = new DefaultRequest(url);
        scheduler.pushRequest(task, request);
        return this;
    }

    /**
     * Add event listener spider.
     *
     * @param eventListener the event listener
     * @return the spider
     */
    public Spider addEventListener(EventListener eventListener) {
        scheduler.addEventListener(eventListener);
        return this;
    }

    /**
     * Cancel spider.
     *
     * @param ifRunning the if running
     * @return the spider
     */
    public Spider cancel(boolean ifRunning) {
        scheduler.cancel(ifRunning);
        return this;
    }

    /**
     * Cancel spider.
     *
     * @param task      the task
     * @param ifRunning the if running
     * @return the spider
     */
    public Spider cancel(Task task, boolean ifRunning) {
        scheduler.cancel(task, ifRunning);
        return this;
    }

    /**
     * Pause spider.
     *
     * @param task the task
     * @return the spider
     */
    public Spider pause(Task task) {
        scheduler.pause(task);
        return this;
    }

    /**
     * Recover spider.
     *
     * @param task the task
     * @return the spider
     */
    public Spider recover(Task task) {
        scheduler.recover(task);
        return this;
    }

}
