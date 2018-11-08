package site.zido.elise.scheduler;

import site.zido.elise.Page;
import site.zido.elise.Request;
import site.zido.elise.Task;

import java.io.Serializable;

public class Seed implements Serializable {
    private static final long serialVersionUID = 6615813166213363435L;
    private Task task;
    private Request request;

    public Seed(Task task, Request request, Page page) {
        this.task = task;
        this.request = request;
    }

    public Seed(Task task, Request request) {
        this.task = task;
        this.request = request;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

}
