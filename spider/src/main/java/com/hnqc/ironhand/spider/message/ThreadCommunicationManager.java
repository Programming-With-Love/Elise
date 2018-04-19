package com.hnqc.ironhand.spider.message;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This message manager provides thread-level messaging that is implemented from {@link CommunicationManager}.
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
public class ThreadCommunicationManager extends AbstractContainerManager implements CommunicationManager {

    private final static String KEY_TYPE = "type";


    private final static String DATA_MESSAGE_REQUEST = "request";

    private final static String DATA_MESSAGE_TASK = "task";

    private final static String DATA_MESSAGE_PAGE = "page";

    //---- containers ----//

    /**
     * message queue
     */
    private final Queue<Map<String, Object>> QUEUE = new ConcurrentLinkedQueue<>();
    private final ThreadPoolExecutor childExecutor;

    //---- containers end ----//
    /**
     * logger
     */
    private final static Logger logger = LoggerFactory.getLogger(ThreadCommunicationManager.class);

    public ThreadCommunicationManager(int blockSize, LoadBalancer loadBalancer) {
        this(new ThreadPoolExecutor(blockSize, blockSize, 0, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("thread child message manager");
                    return thread;
                }), loadBalancer);

    }

    public ThreadCommunicationManager() {
        this(5, new SimpleLoadBalancer());
    }

    public ThreadCommunicationManager(LoadBalancer loadBalancer) {
        this(5, loadBalancer);
    }

    public ThreadCommunicationManager(ThreadPoolExecutor childExecutor, LoadBalancer loadBalancer) {
        super(loadBalancer);
        this.childExecutor = childExecutor;
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
        Map<String, Object> map = new HashMap<>(4);
        if (request != null) {
            map.put(DATA_MESSAGE_REQUEST, request);
        }
        if (page != null) {
            map.put(DATA_MESSAGE_PAGE, page);
        }
        map.put(DATA_MESSAGE_TASK, task);
        map.put(KEY_TYPE, type);
        QUEUE.add(map);
    }

    @Override
    public void registerAnalyzer(AnalyzerListener listener) {
        register(TYPE_MESSAGE_ANALYZER, listener);
    }

    @Override
    public void registerDownloader(DownloadListener listener) {
        register(TYPE_MESSAGE_DOWNLOAD, listener);
    }

    @Override
    public void removeAnalyzer(AnalyzerListener analyzerListener) {
        remove(TYPE_MESSAGE_ANALYZER, analyzerListener);
    }

    @Override
    public void removeDownloader(DownloadListener downloadListener) {
        remove(TYPE_MESSAGE_DOWNLOAD, downloadListener);
    }

    @Override
    public void start() {
        super.start();
        logger.debug("thread listening...");
        while (!Thread.currentThread().isInterrupted() && running()) {
            Map<String, Object> poll = QUEUE.poll();
            if (poll != null) {
                String type = (String) poll.get(KEY_TYPE);
                Task task = (Task) poll.get(DATA_MESSAGE_TASK);
                Request request = (Request) poll.get(DATA_MESSAGE_REQUEST);
                if (TYPE_MESSAGE_ANALYZER.equals(type)) {
                    if (balancerContainer == null) {
                        QUEUE.offer(poll);
                        logger.warn("no balancer in {},a message had be offered", TYPE_MESSAGE_ANALYZER);
                        continue;
                    }
                    Page page = (Page) poll.get(DATA_MESSAGE_PAGE);
                    AnalyzerListener next = (AnalyzerListener) getTargetByType(TYPE_MESSAGE_ANALYZER);
                    if (next == null) {
                        QUEUE.offer(poll);
                        logger.warn("no analyzer in container,a message had be offered");
                        continue;
                    }
                    childExecutor.execute(() -> next.onProcess(task, request, page));
                } else if (TYPE_MESSAGE_DOWNLOAD.equals(type)) {
                    if (balancerContainer == null) {
                        QUEUE.offer(poll);
                        logger.warn("no balancer in {},a message had be offered", TYPE_MESSAGE_DOWNLOAD);
                        continue;
                    }
                    DownloadListener next = (DownloadListener) getTargetByType(TYPE_MESSAGE_DOWNLOAD);
                    if (next == null) {
                        QUEUE.offer(poll);
                        logger.warn("no downloader in container,a message had be offered");
                        continue;
                    }
                    childExecutor.execute(() -> next.onDownload(task, request));
                }
            }
        }
    }

    @Override
    public void process(Task task, Request request, Page page) {
        send(TYPE_MESSAGE_ANALYZER, task, request, page);
    }

    @Override
    public void download(Task task, Request request) {
        DownloadListener next = (DownloadListener) getTargetByType(TYPE_MESSAGE_DOWNLOAD);
        if (next == null) {
            throw new NullPointerException();
        }
        send(TYPE_MESSAGE_DOWNLOAD, task, request, null);
    }

    @Override
    public int blockSize() {
        return QUEUE.size();
    }

    @Override
    public boolean empty() {
        return QUEUE.isEmpty();
    }
}
