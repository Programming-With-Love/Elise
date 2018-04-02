package com.hnqc.ironhand.spider.scheduler;

import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;

public interface Scheduler {
    void push(Request request, Task task);

    Request poll(Task task);
}
