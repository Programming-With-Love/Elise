package com.hnqc.ironhand.spider.distributed;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.downloader.Downloader;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象异步下载类，下载模式更改，不再同步返回page，而是有具体下载方案进行通知
 *
 * @author zido
 * @date 2018/40/12
 */
public abstract class AbstractAsyncDownloader implements Downloader {

    /**
     * 异步下载
     *
     * @param request 请求
     * @param task    任务
     */
    public abstract void asyncDownload(Request request, Task task);

    @Override
    public Page download(Request request, Task task) {
        asyncDownload(request, task);
        return null;
    }

    /**
     * 默认不支持设置线程数量，子类可重写以实现支持线程数量设置
     *
     * @param threadNum 线程数量
     */
    @Override
    public void setThread(int threadNum) {
        throw new UnsupportedOperationException();
    }
}
