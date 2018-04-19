package com.hnqc.ironhand.downloader;

import com.hnqc.ironhand.Page;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;

/**
 * 下载器接口
 *
 * @author zido
 * @date 2018/04/16
 */
public interface Downloader {
    /**
     * 下载
     *
     * @param request 请求
     * @param task    任务
     * @return 下载后的页面
     */
    Page download(Request request, Task task);

    /**
     * 设置下载线程
     * 分布式下载时不支持此方法
     *
     * @param threadNum 线程数量
     */
    void setThread(int threadNum);
}
