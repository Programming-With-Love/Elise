package site.zido.elise.distributed.pojo;

import site.zido.elise.DefaultTask;
import site.zido.elise.http.impl.DefaultResponse;
import site.zido.elise.http.impl.DefaultRequest;

/**
 * 种子，信息载体
 *
 * @author zido
 */
public class Seed {
    private DefaultTask task;
    private DefaultRequest request;
    private DefaultResponse response;

    public Seed(DefaultTask task, DefaultRequest request, DefaultResponse response) {
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

    public DefaultRequest getRequest() {
        return request;
    }

    public Seed setRequest(DefaultRequest request) {
        this.request = request;
        return this;
    }

    public DefaultResponse getResponse() {
        return response;
    }

    public Seed setResponse(DefaultResponse response) {
        this.response = response;
        return this;
    }
}
