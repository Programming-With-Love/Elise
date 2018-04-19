package com.hnqc.ironhand.spider.scheduler;

import com.hnqc.ironhand.spider.Task;

/**
 * monitorable scheduler.
 *
 * @author zido
 * @date 2018/04/19
 */
public interface MonitorableScheduler extends Scheduler {

    int getLeftRequestsCount(Task task);

    int getTotalRequestsCount(Task task);
}
