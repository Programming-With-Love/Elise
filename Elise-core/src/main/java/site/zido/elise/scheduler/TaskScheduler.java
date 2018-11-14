package site.zido.elise.scheduler;

import site.zido.elise.Page;
import site.zido.elise.Request;
import site.zido.elise.Task;

/**
 * the interface of message manager,it provide message service.
 * <p>
 * Each client corresponds to a messageManager, and multiple clients should instantiate multiple message managers.
 * <p>
 * In theory, the client is based on statelessness, either as a download client, an analytics client, or both.
 * Just simply register as the appropriate module
 *
 * @author zido
 */
public interface TaskScheduler {

    /**
     * register as an Analysis Client.
     *
     * @param listener When the download is complete, this interface is called
     */
    void setAnalyzer(AnalyzerListener listener);

    /**
     * If the download client download is completed,
     * this method can be called to send the download completion message and pass the download page to the analysis client.
     * Next, please rest assured that the task scheduling to the manager,
     * <p>
     * manager will select the appropriate scheduling program
     * and call onDownload{@link DownloadListener} to other clients
     * (of course, may to call yourself also, if you are also an analysis client)
     *  @param request the request
     * @param page    page
     */
    void processPage(Task task, Request request, Page page);

    /**
     * If you need to download, you can call this method (usually after the analysis is completed)
     *
     * @param request the request
     */
    void pushRequest(Task task, Request request);

    /**
     * analyzer listener,this interface should called by download service.
     * <p>
     * all downloaded page should be submitted to the analysis module for analysis.
     *
     * @author zido
     */
    interface AnalyzerListener {
        /**
         * message listener.
         *  @param task
         * @param request request container
         * @param page    page container
         */
        void onProcess(Task task, Request request, Page page);
    }

    /**
     * download listener.
     *
     * @author zido
     */
    interface DownloadListener {
        /**
         * message listener.
         *
         * @param request request container
         */
        Page onDownload(Task task, Request request);
    }
}
