package com.hnqc.ironhand.schedule.serivice;

import com.hnqc.ironhand.common.pojo.entity.Task;

public interface ITaskService {
    void addTask(Task task);

    Task getTask(Long id);
}
