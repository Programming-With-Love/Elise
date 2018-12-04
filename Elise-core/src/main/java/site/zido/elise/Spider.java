package site.zido.elise;

import site.zido.elise.events.EventSupport;
import site.zido.elise.select.configurable.DefRootExtractor;

/**
 * The interface Spider.
 *
 * @author zido
 */
public interface Spider extends EventSupport {
    /**
     * Of operator.
     *
     * @param extractor the extractor
     * @return the operator
     */
    Operator of(DefRootExtractor extractor);

    /**
     * Cancel.
     *
     * @param ifRunning the if running
     */
    void cancel(boolean ifRunning);

    /**
     * Pause boolean.
     *
     * @return the boolean
     */
    boolean pause();

    /**
     * Recover.
     */
    void recover();
}
