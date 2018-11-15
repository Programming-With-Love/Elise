package site.zido.elise;

import site.zido.elise.downloader.AutoSwitchDownloader;
import site.zido.elise.processor.DefaultPageProcessor;
import site.zido.elise.processor.MemorySaver;
import site.zido.elise.scheduler.DefaultTaskScheduler;
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

    public static Spider defaults() {
        return defaults(1);
    }

    public static Spider defaults(int threadNum) {
        Spider spider = new Spider();
        final DefaultTaskScheduler scheduler = new DefaultTaskScheduler(threadNum);
        spider.scheduler = scheduler;
        scheduler.setDownloader(new AutoSwitchDownloader());
        scheduler.setProcessor(new DefaultPageProcessor(new MemorySaver()));
        spider.taskManager = new DefaultMemoryTaskManager();
        return spider;
    }

    /**
     * Add urls to select. <br>
     *
     * @param url url
     * @return this
     */
    public Spider addUrl(String url) {
        Asserts.hasLength(url);
        Request request = new Request(url);
        scheduler.pushRequest(taskManager.lastTask(), request);
        return this;
    }

    public Spider addTask(Task task) {
        Asserts.notNull(task);
        taskManager.addTask(task);
        return this;
    }

    public Spider addUrl(Task task, String url) {
        Asserts.notNull(task);
        Asserts.hasLength(url);
        taskManager.addTask(task);
        Request request = new Request(url);
        scheduler.pushRequest(task, request);
        return this;
    }

    public Spider addEventListener(EventListener eventListener) {
        scheduler.addEventListener(eventListener);
        return this;
    }
}
