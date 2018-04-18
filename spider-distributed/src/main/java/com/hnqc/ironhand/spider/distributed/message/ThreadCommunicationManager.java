package com.hnqc.ironhand.spider.distributed.message;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.distributed.configurable.ConfigurableModelExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

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
 * In order to achieve sharing, its message center is actually globally static.
 * <p>
 * it also provides additional string-based methods for sending messages,{@link #send(String, Task, Request, Page, ConfigurableModelExtractor)}.
 * the page can be null.
 * the method is to select the specific onDownload method by judging the type.
 * <p>
 * Even if the internal download listener and analysis listener are all null,
 * it can still be used as a sending client to send messages.
 *
 * @author zido
 * @date 2018/04/16
 */
public class ThreadCommunicationManager implements CommunicationManager {
    public final static String TYPE_MESSAGE_DOWNLOAD = "download";

    public final static String TYPE_MESSAGE_ANALYZER = "analyzer";


    private final static String KEY_TYPE = "type";


    private final static String DATA_MESSAGE_REQUEST = "request";

    private final static String DATA_MESSAGE_TASK = "task";

    private final static String DATA_MESSAGE_PAGE = "page";

    private final static String DATA_MESSAGE_EXTRACTOR = "extractor";

    //---- containers ----//

    /**
     * message queue
     */
    private final static Queue<Map<String, Object>> QUEUE = new ConcurrentLinkedDeque<>();

    /**
     * thread pool
     */
    private final static ThreadPoolExecutor EXECUTOR;

    private static Map<String, LoadBalancer> BALANCER_CONTAINER = new ConcurrentHashMap<>();
    //---- containers end ----//

    private DownloadListener downloadListener;
    private AnalyzerListener analyzerListener;
    /**
     * logger
     */
    private final static Logger logger = LoggerFactory.getLogger(ThreadCommunicationManager.class);

    static {
        BALANCER_CONTAINER.put(TYPE_MESSAGE_DOWNLOAD, new SimpleLoadBalancer());
        BALANCER_CONTAINER.put(TYPE_MESSAGE_ANALYZER, new SimpleLoadBalancer());
        logger.debug("thread message manager started to initialize");
        EXECUTOR = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("thread message manager");
                    return thread;
                });
        ThreadPoolExecutor childExecutor = new ThreadPoolExecutor(5, 5, 0, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("thread child message manager");
                    return thread;
                });
        EXECUTOR.execute(() -> {
            logger.debug("thread listening...");
            while (!Thread.currentThread().isInterrupted()) {
                Map<String, Object> poll = QUEUE.poll();
                if (poll != null) {
                    String type = (String) poll.get(KEY_TYPE);
                    Task task = (Task) poll.get(DATA_MESSAGE_TASK);
                    Request request = (Request) poll.get(DATA_MESSAGE_REQUEST);
                    ConfigurableModelExtractor extractor = (ConfigurableModelExtractor) poll.get(DATA_MESSAGE_EXTRACTOR);
                    if (TYPE_MESSAGE_ANALYZER.equals(type)) {
                        if (BALANCER_CONTAINER == null) {
                            QUEUE.offer(poll);
                            logger.warn("no balancer in {},a message had be offered", TYPE_MESSAGE_ANALYZER);
                            continue;
                        }
                        LoadBalancer loadBalancer = BALANCER_CONTAINER.get(TYPE_MESSAGE_ANALYZER);

                        Page page = (Page) poll.get(DATA_MESSAGE_PAGE);

                        AnalyzerListener next = (AnalyzerListener) loadBalancer.getNext();
                        childExecutor.execute(() -> next.onProcess(task, request, page, extractor));
                    } else if (TYPE_MESSAGE_DOWNLOAD.equals(type)) {
                        if (BALANCER_CONTAINER == null) {
                            QUEUE.offer(poll);
                            logger.warn("no balancer in {},a message had be offered", TYPE_MESSAGE_DOWNLOAD);
                            continue;
                        }
                        LoadBalancer loadBalancer = BALANCER_CONTAINER.get(TYPE_MESSAGE_DOWNLOAD);
                        DownloadListener next = (DownloadListener) loadBalancer.getNext();
                        childExecutor.execute(() -> next.onDownload(task, request, extractor));
                    }
                }
            }
        });
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
    public void send(String type, Task task, Request request, Page page, ConfigurableModelExtractor extractor) {
        Map<String, Object> map = new HashMap<>(4);
        if (request != null) {
            map.put(DATA_MESSAGE_REQUEST, request);
        }
        if (page != null) {
            map.put(DATA_MESSAGE_PAGE, page);
        }
        map.put(DATA_MESSAGE_TASK, task);
        map.put(KEY_TYPE, type);
        map.put(DATA_MESSAGE_EXTRACTOR, extractor);
        QUEUE.add(map);
    }

    @Override
    public void registerAnalyzer(AnalyzerListener listener) {
        LoadBalancer loadBalancer = BALANCER_CONTAINER.get(TYPE_MESSAGE_ANALYZER);
        loadBalancer.addAndRemove(listener, analyzerListener);
        this.analyzerListener = listener;
    }

    @Override
    public void registerDownloader(DownloadListener listener) {
        LoadBalancer loadBalancer = BALANCER_CONTAINER.get(TYPE_MESSAGE_DOWNLOAD);
        loadBalancer.addAndRemove(listener, downloadListener);
        this.downloadListener = listener;
    }

    @Override
    public void process(Task task, Request request, Page page, ConfigurableModelExtractor extractor) {
        send(TYPE_MESSAGE_ANALYZER, task, request, page, extractor);
    }

    @Override
    public void download(Task task, Request request, ConfigurableModelExtractor extractor) {
        send(TYPE_MESSAGE_DOWNLOAD, task, request, null, extractor);
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
