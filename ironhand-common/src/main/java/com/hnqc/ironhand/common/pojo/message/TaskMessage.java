package com.hnqc.ironhand.common.pojo.message;

import com.hnqc.ironhand.common.constants.Status;

public class TaskMessage {
    private Long scheduleId;
    private Long taskId;
    private Status status;

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
