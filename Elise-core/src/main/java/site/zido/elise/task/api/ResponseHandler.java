package site.zido.elise.task.api;

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
