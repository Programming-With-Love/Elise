package com.hnqc.ironhand.spider.downloader;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;

/**
 * 下载器接口
 *
 * @author zido
 * @date 2018/04/16
 */
public interface Downloader {
    Page download(Request request, Task task);

    void setThread(int threadNum);
}
