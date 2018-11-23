package site.zido.elise.downloader;

import site.zido.elise.http.impl.DefaultRequest;
import site.zido.elise.http.impl.DefaultResponse;
import site.zido.elise.Task;

/**
 * downloader interface
 *
 * @author zido
 */
public interface Downloader {
    /**
     * 下载
     *
     * @param request 请求
     * @param task    任务
     * @return 下载后的页面
     */
    DefaultResponse download(Task task, DefaultRequest request);

    /**
     * 设置下载线程
     *
     * @param threadNum 线程数量
     */
    void setThread(int threadNum);
}
