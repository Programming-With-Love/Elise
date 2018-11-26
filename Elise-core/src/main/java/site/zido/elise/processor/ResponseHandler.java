package site.zido.elise.processor;

import site.zido.elise.Task;
import site.zido.elise.http.impl.DefaultResponse;

import java.util.Set;

/**
 * the page processor
 *
 * @author zido
 */
public interface ResponseHandler {
    /**
     * process the response, extract urls to fetch, extract the data and store.
     *
     * @param task     task.
     * @param response response.
     * @return results set
     */
    Set<String> process(Task task, DefaultResponse response);
}
