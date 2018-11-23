package site.zido.elise.scheduler;

import site.zido.elise.http.impl.DefaultRequest;
import site.zido.elise.http.impl.DefaultResponse;
import site.zido.elise.Task;

import java.io.Serializable;

public class Seed implements Serializable {
    private static final long serialVersionUID = 6615813166213363435L;
    private Task task;
    private DefaultRequest request;
    private DefaultResponse response;

    public Seed(Task task, DefaultRequest request, DefaultResponse response) {
        this.task = task;
        this.request = request;
        this.response = response;
    }

    public Seed(Task task, DefaultRequest request) {
        this.task = task;
        this.request = request;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public DefaultRequest getRequest() {
        return request;
    }

    public void setRequest(DefaultRequest request) {
        this.request = request;
    }

    public DefaultResponse getResponse() {
        return response;
    }

    public void setResponse(DefaultResponse response) {
        this.response = response;
    }
}
