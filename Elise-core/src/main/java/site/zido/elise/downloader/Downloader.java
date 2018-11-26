package site.zido.elise.downloader;

import site.zido.elise.Task;
import site.zido.elise.http.Request;
import site.zido.elise.http.impl.DefaultResponse;

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
    DefaultResponse download(Task task, Request request);

    /**
     * set thread number
     *
     * @param threadNum the thread number
     */
    void setThread(int threadNum);
}
