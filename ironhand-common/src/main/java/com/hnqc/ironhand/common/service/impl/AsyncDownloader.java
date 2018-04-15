package com.hnqc.ironhand.common.service.impl;

import com.hnqc.ironhand.common.pojo.Seed;
import com.hnqc.ironhand.common.sender.DownLoadSender;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.distributed.AbstractAsyncDownloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 异步下载实现
 *
 * @author zido
 * @date 2018/04/15
 */
@Component
public class AsyncDownloader extends AbstractAsyncDownloader {
    private DownLoadSender downLoadSender;

    @Override
    public void asyncDownload(Request request, Task task) {
        downLoadSender.send(new Seed(request, task));
    }

    @Autowired
    public void setDownLoadSender(DownLoadSender downLoadSender) {
        this.downLoadSender = downLoadSender;
    }
}
