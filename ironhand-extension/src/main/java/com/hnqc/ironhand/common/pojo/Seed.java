package com.hnqc.ironhand.common.pojo;

import com.hnqc.ironhand.DistributedTask;
import com.hnqc.ironhand.Page;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;

/**
 * 种子，信息载体
 *
 * @author zido
 * @date 2018/04/16
 */
public class Seed {
    private DistributedTask task;
    private Request request;
    private Page page;

    public Seed(DistributedTask task, Request request, Page page) {
        this.task = task;
        this.request = request;
        this.page = page;
    }

    public Seed() {

    }

    public DistributedTask getTask() {
        return task;
    }

    public Seed setTask(DistributedTask task) {
        this.task = task;
        return this;
    }

    public Request getRequest() {
        return request;
    }

    public Seed setRequest(Request request) {
        this.request = request;
        return this;
    }

    public Page getPage() {
        return page;
    }

    public Seed setPage(Page page) {
        this.page = page;
        return this;
    }
}
