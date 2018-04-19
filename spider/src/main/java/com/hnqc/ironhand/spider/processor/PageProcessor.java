package com.hnqc.ironhand.spider.processor;

import com.hnqc.ironhand.spider.*;
import com.hnqc.ironhand.spider.extractor.ModelExtractor;
import com.hnqc.ironhand.spider.scheduler.Scheduler;

/**
 * the page processor
 *
 * @author zido
 * @date 2018/04/18
 */
public interface PageProcessor {
    /**
     * process the page, extract urls to fetch, extract the data and store.
     *
     * @param task      task.
     * @param page      page.
     * @param putter put request holder
     * @return results
     */
    ResultItem process(Task task, Page page, RequestPutter putter);
}
