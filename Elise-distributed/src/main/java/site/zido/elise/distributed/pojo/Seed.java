package site.zido.elise.distributed.pojo;

import site.zido.elise.DefaultExtractorTask;
import site.zido.elise.Page;
import site.zido.elise.Request;

/**
 * 种子，信息载体
 *
 * @author zido
 */
public class Seed {
    private DefaultExtractorTask task;
    private Request request;
    private Page page;

    public Seed(DefaultExtractorTask task, Request request, Page page) {
        this.task = task;
        this.request = request;
        this.page = page;
    }

    public Seed() {

    }

    public DefaultExtractorTask getTask() {
        return task;
    }

    public Seed setTask(DefaultExtractorTask task) {
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
