package com.hnqc.ironhand.message;

import com.hnqc.ironhand.Page;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;

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
public interface TaskScheduler {
    String TYPE_MESSAGE_DOWNLOAD = "__download__";

    String TYPE_MESSAGE_ANALYZER = "__analyzer__";

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
         * @param task    the task {@link Task}
         *                Use {@link Task} more to provide a better messaging mechanism,
         *                significantly reducing the amount of data.
         * @param request request container
         * @param page    page container
         */
        void onProcess(Task task, Request request, Page page);
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
         * @param task    the task {@link Task}
         *                Use {@link Task more to provide a better messaging mechanism,
         *                significantly reducing the amount of data.
         * @param request request container
         */
        void onDownload(Task task, Request request);
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
     * remove analyzer from container
     *
     * @param analyzerListener analyzer listener
     */
    void removeAnalyzer(AnalyzerListener analyzerListener);

    /**
     * remove downloader from container
     *
     * @param downloadListener downloader listener
     */
    void removeDownloader(DownloadListener downloadListener);

    /**
     * If the download client download is completed,
     * this method can be called to send the download completion message and pass the download page to the analysis client.
     * Next, please rest assured that the task scheduling to the manager,
     * <p>
     * manager will select the appropriate scheduling program
     * and call onDownload{@link DownloadListener} to other clients
     * (of course, may also be yourself, if you are also an analysis client)
     *
     * @param task    the task information
     * @param request the request
     * @param page    page
     */
    void process(Task task, Request request, Page page);

    /**
     * If you need to download, you can call this method (usually after the analysis is completed)
     *
     * @param task    the task information
     * @param request the request
     */
    void download(Task task, Request request);
}
