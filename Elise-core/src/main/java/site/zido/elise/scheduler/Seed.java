package site.zido.elise.scheduler;

import site.zido.elise.http.impl.DefaultRequest;
import site.zido.elise.http.impl.DefaultResponse;
import site.zido.elise.Task;

import java.io.Serializable;

/**
 * The type Seed.
 *
 * @author zido
 */
public class Seed implements Serializable {
    private static final long serialVersionUID = 6615813166213363435L;
    private Task task;
    private DefaultRequest request;
    private DefaultResponse response;

    /**
     * Instantiates a new Seed.
     *
     * @param task     the task
     * @param request  the request
     * @param response the response
     */
    public Seed(Task task, DefaultRequest request, DefaultResponse response) {
        this.task = task;
        this.request = request;
        this.response = response;
    }

    /**
     * Instantiates a new Seed.
     *
     * @param task    the task
     * @param request the request
     */
    public Seed(Task task, DefaultRequest request) {
        this.task = task;
        this.request = request;
    }

    /**
     * Gets task.
     *
     * @return the task
     */
    public Task getTask() {
        return task;
    }

    /**
     * Sets task.
     *
     * @param task the task
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * Gets request.
     *
     * @return the request
     */
    public DefaultRequest getRequest() {
        return request;
    }

    /**
     * Sets request.
     *
     * @param request the request
     */
    public void setRequest(DefaultRequest request) {
        this.request = request;
    }

    /**
     * Gets response.
     *
     * @return the response
     */
    public DefaultResponse getResponse() {
        return response;
    }

    /**
     * Sets response.
     *
     * @param response the response
     */
    public void setResponse(DefaultResponse response) {
        this.response = response;
    }
}
