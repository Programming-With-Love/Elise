package site.zido.elise;

import site.zido.elise.events.SingleEventSupport;

public interface Operator extends SingleEventSupport {
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

    Operator addUrl(String... url);
}
