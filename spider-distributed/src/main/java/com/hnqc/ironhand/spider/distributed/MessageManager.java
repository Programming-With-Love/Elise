package com.hnqc.ironhand.spider.distributed;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * 消息管理器
 *
 * @author zido
 * @date 2018/04/16
 */
public class MessageManager {

    private Map<String, MessageListener> messageContainer = new ConcurrentHashMap<>();
    private Queue<Map<String, Object>> queue = new ConcurrentLinkedDeque<>();

    public interface MessageListener {
        /**
         * 监听接口
         *
         * @param request 请求
         * @param page    页面
         * @param task    任务
         */
        void listen(Request request, Page page, Task task);
    }

    public MessageManager() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("messageManager");
                    return thread;
                });
        executor.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                Map<String, Object> poll = queue.poll();
                if (poll != null) {
                    String type = (String) poll.get("type");
                    MessageListener messageListener = messageContainer.get(type);
                    if (messageListener != null) {
                        messageListener.listen((Request) poll.get("request"), (Page) poll.get("page"), (Task) poll.get("task"));
                    }
                }
            }
        });
    }

    public void send(String type, Request request, Page page, Task task) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("request", request);
        map.put("page", page);
        map.put("task", task);
        map.put("type", type);
        queue.add(map);
    }

    public void addListener(String type, MessageListener listener) {
        this.messageContainer.put(type, listener);
    }

    public void removeListener(String type, MessageListener listener) {
        this.messageContainer.remove(type);
    }
}
