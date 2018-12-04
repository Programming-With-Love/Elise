package site.zido.elise;

import site.zido.elise.select.configurable.DefRootExtractor;

public interface Spider {
    Spider crawl(DefRootExtractor extractor, String... url);

    /**
     * Add event listener.
     *
     * @param listener the listener
     */
    void addEventListener(EventListener listener);

    /**
     * Cancel.
     *
     * @param ifRunning the if running
     */
    void cancel(boolean ifRunning);

    /**
     * Cancel boolean.
     *
     * @param task      the task
     * @param ifRunning the if running
     * @return the boolean
     */
    boolean cancel(Task task, boolean ifRunning);

    /**
     * Pause boolean.
     *
     * @param task the task
     * @return the boolean
     */
    boolean pause(Task task);

    /**
     * Recover.
     *
     * @param task the task
     */
    void recover(Task task);
}
