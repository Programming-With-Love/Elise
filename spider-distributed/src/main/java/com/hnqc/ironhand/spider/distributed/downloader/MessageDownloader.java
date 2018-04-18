package com.hnqc.ironhand.spider.distributed.downloader;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.distributed.configurable.ConfigurableModelExtractor;
import com.hnqc.ironhand.spider.distributed.message.CommunicationManager;
import com.hnqc.ironhand.spider.downloader.Downloader;
import com.hnqc.ironhand.spider.downloader.HttpClientDownloader;

import java.util.List;

/**
 * A downloader implemented using the message mechanism will no longer return data synchronously,
 * so calling this class's download method must return null.
 * <p>
 * Internally registered as a downloader to the message manager, not manually registered externally (although this does not have much effect).
 * Internally send download completed message through message manager
 *
 * @author zido
 * @date 2018/40/12
 */
public class MessageDownloader extends AbstractAsyncDownloader implements CommunicationManager.DownloadListener {
    private CommunicationManager manager;
    private Downloader downloader;

    @Override
    public void onDownload(Task task, Request request, List<ConfigurableModelExtractor> extractors) {
        Page page = downloader.download(request, task);
        manager.process(task, request, page, extractors);
    }

    public MessageDownloader(Downloader downloader, CommunicationManager manager) {
        this.downloader = downloader;
        this.manager = manager;
        manager.registerDownloader(this);
    }

    public MessageDownloader(CommunicationManager manager) {
        this(new HttpClientDownloader(), manager);
    }

    @Override
    public void setThread(int threadNum) {
        downloader.setThread(threadNum);
    }

    @Override
    public void download(Request request, Task task, List<ConfigurableModelExtractor> extractors) {
        manager.download(task, request, extractors);
    }
}
