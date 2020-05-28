package site.zido.elise;

import site.zido.elise.custom.Config;
import site.zido.elise.events.EventSupport;
import site.zido.elise.task.api.ResponseHandler;

/**
 * The Spider Interface.
 * <p>
 * Is the core of the operation of the entire crawler.
 *
 * @author zido
 */
public interface Spider extends EventSupport {

    /**
     * create a new task by response handler api.
     *
     * @param handler the handler
     * @param config  the config
     * @return the operator
     */
    Operator of(ResponseHandler handler, Config config);

    /**
     * create a new task by response handler api.
     *
     * @param handler the handler
     * @return the operator
     */
    default Operator of(ResponseHandler handler) {
        return of(handler, null);
    }
    /**
     * Cancel the spider.The Spider will no longer accept any new tasks/requests.
     *
     * @param ifRunning If true,the Spider will wait until the existing task is completed before ending the crawler.                  and else,will end all tasks immediately.
     */
    void cancel(boolean ifRunning);

    /**
     * Pause the spider.
     *
     * @return the boolean
     */
    boolean pause();

    /**
     * Recover the spider.
     */
    void recover();
}
