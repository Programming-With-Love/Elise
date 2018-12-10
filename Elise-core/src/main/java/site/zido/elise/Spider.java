package site.zido.elise;

import site.zido.elise.custom.Config;
import site.zido.elise.events.EventSupport;
import site.zido.elise.select.configurable.ResponseHandler;

/**
 * The Spider Interface.
 * <p>
 * Is the core of the operation of the entire crawler.
 *
 * @author zido
 */
public interface Spider extends EventSupport {

    /**
     * Of operator.
     *
     * @param handler the handler
     * @param config  the config
     * @return the operator
     */
    Operator of(ResponseHandler handler, Config config);

    /**
     * Of operator.
     *
     * @param handler the handler
     * @return the operator
     */
    Operator of(ResponseHandler handler);


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
