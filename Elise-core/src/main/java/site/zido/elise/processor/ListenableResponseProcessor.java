package site.zido.elise.processor;

/**
 * The interface Listenable response handler.
 *
 * @author zido
 */
public interface ListenableResponseProcessor extends ResponseProcessor {
    /**
     * Add event listener.
     *
     * @param listener the listener
     */
    void addEventListener(ProcessorEventListener listener);

    /**
     * Remove event listener.
     *
     * @param listener the listener
     */
    void removeEventListener(ProcessorEventListener listener);
}
