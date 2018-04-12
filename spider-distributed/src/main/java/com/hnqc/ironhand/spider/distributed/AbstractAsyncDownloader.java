package com.hnqc.ironhand.spider.distributed;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.downloader.Downloader;

/**
 * 抽象异步下载类
 *
 * @author zido
 * @date 2018/40/12
 */
public abstract class AbstractAsyncDownloader implements Downloader {

    public abstract void asyncDownload(Request request, Task task);

    @Override
    public Page download(Request request, Task task) {
        asyncDownload(request, task);
        return null;
    }

    @Override
    public void setThread(int threadNum) {
        throw new UnsupportedOperationException();
    }
}
