package site.zido.elise.processor;

import site.zido.elise.Page;
import site.zido.elise.Task;

/**
 * the page processor
 *
 * @author zido
 */
public interface PageProcessor {
    /**
     * process the page, extract urls to fetch, extract the data and store.
     *
     * @param task task.
     * @param page page.
     * @return results
     */
    ItemLinksModel process(Task task, Page page);
}
