package com.hnqc.ironhand.common.pojo;

import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.distributed.DistributedTask;

/**
 * 种子，信息载体
 *
 * @author zido
 * @date 2018/04/16
 */
public class Seed {
    private Request request;
    private DistributedTask task;

    public Seed() {

    }

    public Seed(Request request, Task task) {
        this.request = request;
        if (task instanceof DistributedTask) {
            this.task = (DistributedTask) task;
        } else {
            this.task = new DistributedTask(task);
        }
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public DistributedTask getTask() {
        return task;
    }

    public void setTask(DistributedTask task) {
        this.task = task;
    }
}
