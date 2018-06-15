package site.zido.elise;

/**
 * RequestPutter
 *
 * @author zido
 */
public interface RequestPutter {
    /**
     * Push request.
     *
     * @param task    the task
     * @param request the request
     */
    void pushRequest(Task task, Request request);
}
