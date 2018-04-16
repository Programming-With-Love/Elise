package com.hnqc.ironhand.spider.distributed.downloader;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.distributed.AbstractAsyncDownloader;
import com.hnqc.ironhand.spider.distributed.DsSpider;
import com.hnqc.ironhand.spider.distributed.MessageManager;
import com.hnqc.ironhand.spider.downloader.HttpClientDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程异步下载实现类
 *
 * @author zido
 * @date 2018/04/16
 */
public class ThreadAsyncDownloader extends AbstractAsyncDownloader {
    private static Logger logger = LoggerFactory.getLogger(ThreadAsyncDownloader.class);
    private static MessageManager manager = new MessageManager();
    private HttpClientDownloader downloader = new HttpClientDownloader();
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(1,
            1,
            0,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(),
            r -> {
                Thread thread = new Thread(r);
                thread.setName("thread-async-downloader");
                return thread;
            });

    public ThreadAsyncDownloader(int threadNum) {
        this.setThread(threadNum);
    }

    public ThreadAsyncDownloader() {
    }

    @Override
    public void setThread(int threadNum) {
        executor.setCorePoolSize(threadNum);
        executor.setMaximumPoolSize(threadNum);
    }

    @Override
    public void asyncDownload(Request request, Task task) {
        executor.execute(() -> {
            logger.debug("url:[{}] start download", request.getUrl());
            Page page = downloader.download(request, task);
            logger.debug("url:[{}] finish download", request.getUrl());
            manager.send("analyzer", request, page, task);
        });
    }
}
