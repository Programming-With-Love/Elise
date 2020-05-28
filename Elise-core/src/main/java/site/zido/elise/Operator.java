package site.zido.elise;

import site.zido.elise.events.SingleEventListener;
import site.zido.elise.http.Request;

import java.util.concurrent.TimeUnit;

/**
 * The interface Operator.
 *
 * @author zido
 */
public interface Operator {
    /**
     * Cancel task of the operator.The Spider will no longer accept any new requests of the task.
     *
     * @param ifRunning If true,the task will wait until the existing request is completed before ending the crawler.                  and else,will end all request immediately.
     * @return the operator
     */
    Operator cancel(boolean ifRunning);

    /**
     * Pause task of the operator.And Spider will no long accept any new requests of the task.
     *
     * @return the boolean
     */
    Operator pause();

    /**
     * recover task of the operator.And the spider will re-accept the new request of the task.
     *
     * @return the operator
     */
    Operator recover();

    /**
     * waiting until the task success or cancel
     *
     * @return this operator
     * @throws InterruptedException thread interrupted
     */
    Operator block() throws InterruptedException;

    /**
     * 限时阻塞
     *
     * @param time 阻塞时间
     * @param unit 单位
     * @return 操作句柄
     * @throws InterruptedException ex
     */
    Operator block(long time, TimeUnit unit) throws InterruptedException;

    /**
     * Add url operator.
     *
     * @param url the url
     * @return the operator
     */
    Operator execute(String... url);

    /**
     * Execute operator.
     *
     * @param request the request
     * @return the operator
     */
    Operator execute(Request request);

    /**
     * Add event listener.
     *
     * @param listener the listener
     * @return the operator
     */
    Operator addEventListener(SingleEventListener listener);
}
