package com.hnqc.ironhand.spider.distributed.downloader;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.distributed.configurable.DefRootExtractor;
import com.hnqc.ironhand.spider.distributed.message.MessageManager;
import com.hnqc.ironhand.spider.downloader.Downloader;
import com.hnqc.ironhand.spider.downloader.HttpClientDownloader;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * An asynchronous download class implemented using the message mechanism will no longer return data synchronously,
 * so calling this class's download method must return null.
 * <p>
 * Internally registered as a downloader to the message manager, not manually registered externally (although this does not have much effect).
 * Internally send download completed message through message manager
 *
 * @author zido
 * @date 2018/40/12
 */
public class AsyncWithMessageDownloader implements Downloader, MessageManager.DownloadListener {
    private MessageManager manager;
    private ThreadPoolExecutor executor;
    private Downloader downloader;

    @Override
    public void callback(Task task, Request request, DefRootExtractor extractor) {
        executor.execute(() -> {
            Page page = downloader.download(request, task);
            AsyncWithMessageDownloader.this.manager.downloadOver(task, request, page, extractor);
        });
    }

    public AsyncWithMessageDownloader(Downloader downloader, MessageManager manager, int threadNum) {
        this.manager = manager;
        executor = new ThreadPoolExecutor(threadNum,
                threadNum,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("thread-async-downloader");
                    return thread;
                });
        this.downloader = downloader;
        manager.registerDownloader(this);
    }

    public AsyncWithMessageDownloader(MessageManager manager, int threadNum) {
        this(new HttpClientDownloader(), manager, threadNum);
    }

    public AsyncWithMessageDownloader(Downloader downloader, MessageManager manager) {
        this(downloader, manager, 1);
    }

    public AsyncWithMessageDownloader(MessageManager manager) {
        this(manager, 1);
    }

    @Override
    public Page download(Request request, Task task) {
        //TODO download function
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
