package com.hnqc.ironhand.common.pojo;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.DistributedTask;

/**
 * 种子，信息载体
 *
 * @author zido
 * @date 2018/04/16
 */
public class Seed {
    private Task task;
    private Request request;
    private Page page;

    public Task getTask() {
        return task;
    }

    public Seed setTask(Task task) {
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
