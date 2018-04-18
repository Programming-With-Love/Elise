package com.hnqc.ironhand.spider.distributed.downloader;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.distributed.DistributedTask;
import com.hnqc.ironhand.spider.distributed.configurable.ConfigurableModelExtractor;
import com.hnqc.ironhand.spider.downloader.AbstractDownloader;

import java.util.List;

/**
 * AbstractAsyncDownloader
 *
 * @author zido
 * @date 2018/04/18
 */
public abstract class AbstractAsyncDownloader extends AbstractDownloader {

    /**
     * download function,it's async
     *
     * @param request   request
     * @param task      task
     * @param extractor extractor
     */
    public abstract void download(Request request, Task task, List<ConfigurableModelExtractor> extractor);

    @Override
    public Page download(Request request, Task task) {
        if (task instanceof DistributedTask) {
            List<ConfigurableModelExtractor> extractors = ((DistributedTask) task).getModelExtractors();
            download(request, task, extractors);
        }
        return null;
    }
}
