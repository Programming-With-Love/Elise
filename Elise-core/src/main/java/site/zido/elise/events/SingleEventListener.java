package site.zido.elise.events;

import site.zido.elise.http.Request;
import site.zido.elise.http.Response;

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
     */
    default void onSuccess() {
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
