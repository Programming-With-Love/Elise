package com.hnqc.ironhand.message;

import com.hnqc.ironhand.Page;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;
import com.hnqc.ironhand.scheduler.DuplicationProcessor;
import com.hnqc.ironhand.scheduler.HashSetDeduplicationProcessor;
import com.hnqc.ironhand.scheduler.Seed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * This message manager provides thread-level messaging that is implemented from {@link TaskScheduler}.
 * <p>
 * This class is thread-safe. In addition to the efficiency issues,
 * you can rest assured that other concerns can be used with confidence.
 * <p>
 * This is the default stand-alone task scheduling management,
 * is thread-level and very simple, follow-up may add a better scheduling management solution,
 * currently only provides a simple implementation.
 * <p>
 * it based on {@link ThreadPoolExecutor}
 * <p>
 * it also provides additional string-based methods for sending messages,{@link #send(String, Task, Request, Page)}.
 * the page can be null.
 * the method is to select the specific onDownload method by judging the type.
 * <p>
 * Even if the internal download listener and analysis listener are all null,
 * it can still be used as a sending client to send messages.
 *
 * @author zido
 * @date 2018/04/16
 */
public class ThreadTaskScheduler extends AbstractDuplicateRemovedScheduler implements MonitorableScheduler {


    private SimpleMessageManager container;
    /**
     * logger
     */
    private final static Logger logger = LoggerFactory.getLogger(ThreadTaskScheduler.class);

    public ThreadTaskScheduler(DuplicationProcessor duplicationProcessor) {
        this(duplicationProcessor, new SimpleLoadBalancer());

    }

    public ThreadTaskScheduler() {
        this(new HashSetDeduplicationProcessor());
    }

    public ThreadTaskScheduler(DuplicationProcessor duplicationProcessor,
                               LoadBalancer loadBalancer) {
        super(duplicationProcessor);
        container = new SimpleMessageManager();
    }

    /**
     * Send messages according to type, if there is no related kind of container, it will not have any effect.
     * <p>
     * Note: It will not be backlog of message queue.
     *
     * @param type    {@link #TYPE_MESSAGE_DOWNLOAD} and {@link #TYPE_MESSAGE_ANALYZER}
     * @param task    task
     * @param request request
     * @param page    page
     */
    public void send(String type, Task task, Request request, Page page) {
        container.send(type, new Seed(task, request, page));
    }

    @Override
    public void process(Task task, Request request, Page page) {
        send(TYPE_MESSAGE_ANALYZER, task, request, page);
    }

    @Override
    protected void pushWhenNoDuplicate(Request request, Task task) {
        send(TYPE_MESSAGE_DOWNLOAD, task, request, null);
    }

    @Override
    public int clientSize(String type) {
        return container.clientSize(type);
    }

    @Override
    public int blockSize() {
        return container.blockSize();
    }

    @Override
    public void registerAnalyzer(TaskScheduler.AnalyzerListener listener) {
        container.register(TYPE_MESSAGE_ANALYZER, listener);
    }

    @Override
    public void registerDownloader(TaskScheduler.DownloadListener listener) {
        container.register(TYPE_MESSAGE_DOWNLOAD, listener);
    }

    @Override
    public void removeAnalyzer(TaskScheduler.AnalyzerListener analyzerListener) {
        container.remove(TYPE_MESSAGE_ANALYZER, analyzerListener);
    }

    @Override
    public void removeDownloader(TaskScheduler.DownloadListener downloadListener) {
        container.remove(TYPE_MESSAGE_DOWNLOAD, downloadListener);
    }
}
