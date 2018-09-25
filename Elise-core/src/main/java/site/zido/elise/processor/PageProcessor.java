package site.zido.elise.processor;

import site.zido.elise.Page;
import site.zido.elise.RequestPutter;
import site.zido.elise.ResultItem;
import site.zido.elise.Task;
import site.zido.elise.scheduler.TaskScheduler;

import java.util.List;

/**
 * the page processor
 *
 * @author zido
 */
public interface PageProcessor {
    /**
     * process the page, extract urls to fetch, extract the data and store.
     *
     * @param task   task.
     * @param page   page.
     * @param putter put request holder
     * @return results
     */
    List<ResultItem> process(Task task, Page page, TaskScheduler putter);
}
