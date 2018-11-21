package site.zido.elise.scheduler;

import site.zido.elise.EventListener;
import site.zido.elise.http.Request;
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
     * If you need to download, you can call this method (usually after the analysis is completed)
     *
     * @param request the request
     */
    void pushRequest(Task task, Request request);

    void addEventListener(EventListener listener);

    void cancel(boolean ifRunning);

    boolean cancel(Task task, boolean ifRunning);

    boolean pause(Task task);

    void recover(Task task);
}
