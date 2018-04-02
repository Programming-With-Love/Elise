package com.hnqc.ironhand.spider.scheduler;

import com.hnqc.ironhand.spider.Request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class QueueScheduler implements Scheduler {

    private BlockingQueue<Request> queue = new LinkedBlockingDeque<>();

    @Override
    public void push(Request request) {
        queue.add(request);
    }

    @Override
    public Request poll(Request request) {
        return queue.poll();
    }
}
