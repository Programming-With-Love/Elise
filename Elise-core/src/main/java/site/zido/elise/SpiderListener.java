package site.zido.elise;

/**
 * spider listener
 *
 * @author zido
 */
public interface SpiderListener {
    /**
     * On success.
     *
     * @param request the request
     */
    void onSuccess(Request request);

    /**
     * On error.
     *
     * @param request the request
     */
    void onError(Request request);
}
