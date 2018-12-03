package site.zido.elise.distributed.pojo;

import site.zido.elise.DefaultTask;
import site.zido.elise.http.Request;
import site.zido.elise.http.Response;
import site.zido.elise.http.impl.DefaultResponse;

/**
 * 种子，信息载体
 *
 * @author zido
 */
public class Seed {
    private DefaultTask task;
    private Request request;
    private Response response;

    public Seed(DefaultTask task, Request request, Response response) {
        this.task = task;
        this.request = request;
        this.response = response;
    }

    public Seed() {

    }

    public DefaultTask getTask() {
        return task;
    }

    public Seed setTask(DefaultTask task) {
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

    public Response getResponse() {
        return response;
    }

    public Seed setResponse(DefaultResponse response) {
        this.response = response;
        return this;
    }
}
