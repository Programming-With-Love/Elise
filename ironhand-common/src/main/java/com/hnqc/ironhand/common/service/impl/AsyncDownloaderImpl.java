package com.hnqc.ironhand.common.service.impl;

import com.hnqc.ironhand.common.pojo.Seed;
import com.hnqc.ironhand.common.sender.DownLoadSender;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.distributed.AbsAsyncDownloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AsyncDownloaderImpl extends AbsAsyncDownloader {
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
