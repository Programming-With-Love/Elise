package com.hnqc.ironhand.spider.scheduler;

import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler{

    private BlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();

    @Override
    public void pushWhenNoDuplicate(Request request, Task task) {
        queue.add(request);
    }

    @Override
    public Request poll(Task task) {
        return queue.poll();
    }

    @Override
    public int getLeftRequestsCount(Task task) {
        return queue.size();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return getDuplicateRemover().getTotalRequestsCount(task);
    }
}
