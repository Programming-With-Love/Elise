package com.hnqc.ironhand.spider.processor;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.ResultItem;
import com.hnqc.ironhand.spider.Site;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.extractor.ModelExtractor;

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
     * @param task task.
     * @param page page.
     * @return results
     */
    ResultItem process(Task task, Page page);
}
