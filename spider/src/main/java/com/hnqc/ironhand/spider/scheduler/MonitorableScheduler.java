package com.hnqc.ironhand.spider.scheduler;

import com.hnqc.ironhand.spider.Task;

public interface MonitorableScheduler extends Scheduler {
    int getLeftRequestsCount(Task task);

    int getTotalRequestsCount(Task task);
}
