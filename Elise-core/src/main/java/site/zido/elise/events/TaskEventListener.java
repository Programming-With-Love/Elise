package site.zido.elise.events;

import site.zido.elise.http.Request;
import site.zido.elise.http.Response;
import site.zido.elise.task.Task;

public interface TaskEventListener extends EventListener {
    /**
     * On download success.
     *
     * @param task     the task
     * @param request  the request
     * @param response the response
     */
    default void onDownloadSuccess(Task task, Request request, Response response) {
    }

    /**
     * On download error.
     *
     * @param task     the task
     * @param request  the request
     * @param response the response
     */
    default void onDownloadError(Task task, Request request, Response response) {
    }

    /**
     * On success.
     *
     * @param task the task
     */
    default void onSuccess(Task task) {
    }

    /**
     * On pause.
     *
     * @param task the task
     */
    default void onPause(Task task) {
    }

    /**
     * On recover.
     *
     * @param task the task
     */
    default void onRecover(Task task) {
    }

    /**
     * On cancel.
     *
     * @param task the task
     */
    default void onCancel(Task task) {
    }
}
