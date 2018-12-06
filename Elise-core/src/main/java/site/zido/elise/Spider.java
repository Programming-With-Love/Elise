package site.zido.elise;

import site.zido.elise.custom.Config;
import site.zido.elise.events.EventSupport;
import site.zido.elise.select.configurable.ModelExtractor;

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
    Operator of(ModelExtractor extractor, Config config);

    Operator of(ModelExtractor extractor);


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
