package site.zido.elise;

import site.zido.elise.http.impl.DefaultRequest;
import site.zido.elise.http.impl.DefaultResponse;
import site.zido.elise.processor.ProcessorEventListener;

/**
 * The interface Event listener.
 *
 * @author zido
 */
public interface EventListener extends ProcessorEventListener, java.util.EventListener {
    /**
     * On download success.
     *
     * @param task     the task
     * @param request  the request
     * @param response the response
     */
    default void onDownloadSuccess(Task task, DefaultRequest request, DefaultResponse response) {
    }

    /**
     * On download error.
     *
     * @param task     the task
     * @param request  the request
     * @param response the response
     */
    default void onDownloadError(Task task, DefaultRequest request, DefaultResponse response) {
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

    /**
     * On pause.
     */
    default void onPause() {
    }

    /**
     * On cancel.
     */
    default void onCancel() {
    }
}
