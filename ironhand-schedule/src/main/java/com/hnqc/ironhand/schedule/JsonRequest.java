package com.hnqc.ironhand.schedule;

import com.hnqc.ironhand.DistributedTask;
import com.hnqc.ironhand.Request;

/**
 * JsonRequest
 *
 * @author zido
 * @date 2018/04/27
 */
public class JsonRequest {
    private DistributedTask task;
    private Request request;

    public DistributedTask getTask() {
        return task;
    }

    public JsonRequest setTask(DistributedTask task) {
        this.task = task;
        return this;
    }

    public Request getRequest() {
        return request;
    }

    public JsonRequest setRequest(Request request) {
        this.request = request;
        return this;
    }
}
