package com.hnqc.ironhand.spider.distributed;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;

/**
 * 分布式spider接口
 *
 * @author zido
 * @date 2018/58/16
 */
public interface IDsSpider {
    /**
     * 无参运行，一般是首次运行
     */
    void run();

    /**
     * 带参运行，处理请求和具体页面
     *
     * @param request 请求
     * @param page    页面
     */
    void run(Request request, Page page);

}
