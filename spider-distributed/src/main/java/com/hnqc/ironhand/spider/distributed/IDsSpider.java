package com.hnqc.ironhand.spider.distributed;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;

/**
 * distributed spider interface
 *
 * @author zido
 * @date 2018/58/16
 */
public interface IDsSpider {
    /**
     * when you first run, you need to call this method
     */
    void run();

    /**
     * When the page download is complete, you need to call this method
     *
     * @param request request contains url and more
     * @param page    page contains html and more
     */
    void run(Request request, Page page);

}
