package site.zido.elise.events;

import site.zido.elise.http.Request;
import site.zido.elise.http.Response;

/**
 * The interface Single event listener.
 *
 * @author zido
 */
public interface SingleEventListener extends SingleProcessorEventListener {
    /**
     * On download success.
     *
     * @param request  the request
     * @param response the response
     */
    default void onDownloadSuccess(Request request, Response response) {
    }

    /**
     * On download error.
     *
     * @param request  the request
     * @param response the response
     */
    default void onDownloadError(Request request, Response response) {
    }

    /**
     * On success.
     *
     * @param name the name
     */
    default void onSuccess(String name) {
    }

    /**
     * On recover.
     */
    default void onRecover() {
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
