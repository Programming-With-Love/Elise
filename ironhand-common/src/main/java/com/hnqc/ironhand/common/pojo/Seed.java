package com.hnqc.ironhand.common.pojo;

import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.distributed.DistributedTask;
import com.hnqc.ironhand.spider.distributed.DsSpiderImpl;

import javax.persistence.Entity;

/**
 * 下载种子
 */
public class Seed {
    private Request request;
    private DistributedTask task;
    private DsSpiderImpl dsSpider;

    public Seed() {

    }

    public Seed(DsSpiderImpl dsSpider) {
        this.dsSpider = dsSpider;
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

    public DsSpiderImpl getDsSpider() {
        return dsSpider;
    }

    public void setDsSpider(DsSpiderImpl dsSpider) {
        this.dsSpider = dsSpider;
    }
}
