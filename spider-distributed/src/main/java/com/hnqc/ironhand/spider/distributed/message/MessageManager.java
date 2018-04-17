package com.hnqc.ironhand.spider.distributed.message;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.distributed.configurable.DefRootExtractor;

/**
 * the interface of message manager,it provide message service.
 * <p>
 * Each client corresponds to a messageManager, and multiple clients should instantiate multiple message managers.
 * <p>
 * In theory, the client is based on statelessness, either as a download client, an analytics client, or both.
 * Just simply register as the appropriate module
 * {@link #registerAnalyzer(AnalyzerListener)}
 * {@link #registerDownloader(DownloadListener)}
 *
 * @author zido
 * @date 2018/04/17
 */
public interface MessageManager {
    /**
     * analyzer listener,this interface should called by download service.
     * <p>
     * all downloaded page should be submitted to the analysis module for analysis.
     *
     * @author zido
     * @date 2018/04/17
     */
    interface AnalyzerListener {
        /**
         * message listener.
         *
         * @param task      the task {@link Task}
         *                  Use {@link com.hnqc.ironhand.spider.distributed.DistributedTask} more to provide a better distributed messaging mechanism,
         *                  significantly reducing the amount of data.
         *
         * @param request   request container
         * @param page      page container
         * @param extractor extractor definition
         */
        void callback(Task task, Request request, Page page, DefRootExtractor extractor);
    }

    /**
     * download listener.
     *
     * @author zido
     * @date 2018/59/17
     */
    interface DownloadListener {
        /**
         * message listener.
         *
         * @param task      the task {@link Task}
         *                  Use {@link com.hnqc.ironhand.spider.distributed.DistributedTask} more to provide a better distributed messaging mechanism,
         *                  significantly reducing the amount of data.
         * @param request   request container
         * @param extractor extractor definition
         */
        void callback(Task task, Request request, DefRootExtractor extractor);
    }

    /**
     * register as an Analysis Client.
     *
     * @param listener When the download is complete, this interface is called
     */
    void registerAnalyzer(AnalyzerListener listener);

    /**
     * register as an Download Client.
     *
     * @param listener When the analysis is complete, this interface is called
     */
    void registerDownloader(DownloadListener listener);

    /**
     * If the download client download is completed,
     * this method can be called to send the download completion message and pass the download page to the analysis client.
     * Next, please rest assured that the task scheduling to the manager,
     * manager will select the appropriate scheduling program and asynchronous callback callback to other clients (of course, may also be yourself, if you are also an analysis client)
     *
     * @param task    the task information
     * @param request the request
     * @param page    page
     */
    void downloadOver(Task task, Request request, Page page,DefRootExtractor extractor);

    /**
     * If you need to download, you can call this method (usually after the analysis is completed)
     *
     * @param task    the task information
     * @param request the request
     */
    void analyzerOver(Task task, Request request,DefRootExtractor extractor);

    /**
     * See how many messages are in the message container
     *
     * @return the size of message container
     */
    int blockSize();

    /**
     * Check if the message container is empty
     *
     * @return true/false
     */
    default boolean empty() {
        return blockSize() == 0;
    }
}
