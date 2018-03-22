package com.hnqc.ironhand.schedule.serivice;

import com.hnqc.ironhand.common.pojo.entity.Scheduler;

public interface ITaskService {
    void addTask(Scheduler scheduler);

    Scheduler getTask(Long id);
}
