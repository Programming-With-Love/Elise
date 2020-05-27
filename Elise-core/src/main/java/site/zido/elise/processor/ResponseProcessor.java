package site.zido.elise.processor;

import site.zido.elise.http.Response;
import site.zido.elise.select.SelectorMatchException;
import site.zido.elise.task.Task;

import java.util.Set;

/**
 * the page processor
 *
 * @author zido
 */
public interface ResponseProcessor {
    /**
     * process the response, extract urls to fetch, extract the data and store.
     *
     * @param task     task.
     * @param response response.
     * @return results set
     * @throws SelectorMatchException when selector matcher error
     */
    Set<String> process(Task task, Response response) throws SelectorMatchException;
}
