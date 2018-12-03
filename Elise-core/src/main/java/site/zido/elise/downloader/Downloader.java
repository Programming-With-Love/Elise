package site.zido.elise.downloader;

import site.zido.elise.Task;
import site.zido.elise.http.Request;
import site.zido.elise.http.Response;

/**
 * downloader interface
 *
 * @author zido
 */
public interface Downloader {
    /**
     * download by task and request
     *
     * @param task    the task
     * @param request the request
     * @return the response
     */
    Response download(Task task, Request request);

}
