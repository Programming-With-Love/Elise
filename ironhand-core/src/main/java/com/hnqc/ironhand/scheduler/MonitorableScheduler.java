package com.hnqc.ironhand.scheduler;

import com.hnqc.ironhand.Task;

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
