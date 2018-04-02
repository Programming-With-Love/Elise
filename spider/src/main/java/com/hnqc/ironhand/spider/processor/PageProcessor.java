package com.hnqc.ironhand.spider.processor;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Site;

public interface PageProcessor {
    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page page
     */
    public void process(Page page);

    /**
     * get the site settings
     *
     * @return site
     * @see Site
     */
    public Site getSite();
}
