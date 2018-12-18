package site.zido.elise.task.api;

import site.zido.elise.task.api.SelectableResponse;

/**
 * the response handler
 *
 * @author zido
 */
@FunctionalInterface
public interface ResponseHandler {
    /**
     * handle response,build a extractor by java api
     *
     * @param response response
     */
    void onHandle(SelectableResponse response);
}
