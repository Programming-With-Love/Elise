package site.zido.elise.scheduler;

import site.zido.elise.http.Response;
import site.zido.elise.http.Request;
import site.zido.elise.Task;

import java.io.Serializable;

public class Seed implements Serializable {
    private static final long serialVersionUID = 6615813166213363435L;
    private Task task;
    private Request request;
    private Response response;

    public Seed(Task task, Request request, Response response) {
        this.task = task;
        this.request = request;
        this.response = response;
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

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
