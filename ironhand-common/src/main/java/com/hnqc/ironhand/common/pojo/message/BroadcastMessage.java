package com.hnqc.ironhand.common.pojo.message;

import com.hnqc.ironhand.common.constants.Status;

public class BroadcastMessage {
    private Long scheduleId;
    private Status status;

    @Override
    public String toString() {
        return "BroadcastMessage{" +
                "scheduleId=" + scheduleId +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BroadcastMessage that = (BroadcastMessage) o;

        return getScheduleId().equals(that.getScheduleId()) && getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        int result = getScheduleId().hashCode();
        result = 31 * result + getStatus().hashCode();
        return result;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
