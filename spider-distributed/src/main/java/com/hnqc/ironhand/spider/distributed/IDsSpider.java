package com.hnqc.ironhand.spider.distributed;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;

public interface IDsSpider {
    void run();

    void run(Request request, Page page);

}
