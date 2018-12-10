package site.zido.elise.select.configurable;

import site.zido.elise.select.api.SelectableResponse;

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
